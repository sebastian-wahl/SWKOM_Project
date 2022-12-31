package at.fhtw.swen3.services.impl;

import at.fhtw.swen3.gps.service.GeoEncodingService;
import at.fhtw.swen3.gps.service.models.Address;
import at.fhtw.swen3.gps.service.models.GeoEncodingCoordinate;
import at.fhtw.swen3.persistence.entities.*;
import at.fhtw.swen3.persistence.entities.enums.TrackingInformationState;
import at.fhtw.swen3.persistence.repositories.HopRepository;
import at.fhtw.swen3.persistence.repositories.ParcelRepository;
import at.fhtw.swen3.persistence.repositories.TruckRepository;
import at.fhtw.swen3.persistence.repositories.WarehouseNextHopsRepository;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.exception.BLException.BLNoTruckFound;
import at.fhtw.swen3.services.exception.BLException.BLParcelNotFound;
import at.fhtw.swen3.services.exception.BLException.BLSubmitParcelAddressIncorrect;
import at.fhtw.swen3.services.exception.BLException.BLWarehouseNextHopsNotFound;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import com.github.curiousoddman.rgxgen.RgxGen;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static at.fhtw.swen3.util.JTSUtil.geoEncodingCoordinateToGeometry;


@Service
@Slf4j
public class ParcelServiceImpl implements ParcelService {

    private final EntityValidatorService entityValidatorService;

    private final ParcelRepository parcelRepository;

    private final HopRepository hopRepository;

    private final GeoEncodingService geoEncodingService;

    private final TruckRepository truckRepository;

    private final WarehouseNextHopsRepository warehouseNextHopsRepository;

    @Autowired
    public ParcelServiceImpl(EntityValidatorService entityValidatorService, ParcelRepository parcelRepository,
                             HopRepository hopRepository, GeoEncodingService geoEncodingService, TruckRepository truckRepository,
                             WarehouseNextHopsRepository warehouseNextHopsRepository) {
        this.entityValidatorService = entityValidatorService;
        this.parcelRepository = parcelRepository;
        this.hopRepository = hopRepository;
        this.geoEncodingService = geoEncodingService;
        this.truckRepository = truckRepository;
        this.warehouseNextHopsRepository = warehouseNextHopsRepository;

    }

    @Override
    public void reportParcelDelivery(String trackingId) {
        log.debug("Reporting parcel delivery for id {}", trackingId);
        Optional<ParcelEntity> parcelOpt = parcelRepository.findFirstByTrackingId(trackingId);
        if (parcelOpt.isPresent()) {
            // ToDo check if last location is the arrival dest to validate the delivery
            String hopArrivalCode = parcelOpt.get().getVisitedHops().get(parcelOpt.get().getVisitedHops().size() - 1).getCode();
            hopRepository.findFirstByCode(hopArrivalCode).get().getLocationCoordinates();
            parcelOpt.get().getRecipient().getStreet();

            changeTrackingStateToDelivered(parcelOpt.get());
        } else {
            throw new BLParcelNotFound(trackingId);
        }
    }

    @Override
    public Optional<ParcelEntity> reportParcelHop(String trackingId, String code) {
        log.debug("Reporting parcel hop for hop with id {} and parcel with id {}", code, trackingId);
        Optional<ParcelEntity> parcelOpt = parcelRepository.findFirstByTrackingId(trackingId);

        Optional<HopEntity> hopOptional = hopRepository.findFirstByCode(code);
        // add hop do parcel
        if (parcelOpt.isPresent() && hopOptional.isPresent()) {
            ParcelEntity parcel = parcelOpt.get();

            HopArrivalEntity hopArrivalEntity = HopArrivalEntity.builder()
                    .dateTime(OffsetDateTime.now())
                    .code(hopOptional.get().getCode())
                    .description(hopOptional.get().getDescription())
                    .build();


            //parcel.getVisitedHops().add(hopArrivalEntity);
            parcelRepository.save(parcel);
            return Optional.of(parcel);
        }
        log.error("Reporting parcel not possible! Hop found: {}; Parcel found: {}", hopOptional.isPresent(), parcelOpt.isPresent());
        return Optional.empty();
    }

    @Override
    public ParcelEntity submitParcel(ParcelEntity parcel) {
        log.debug("Submitting new parcel");
        entityValidatorService.validate(parcel);
        log.debug("Parcel validated");

        Address senderAddress = Address.fromRecipient(parcel.getSender());
        Address recipientAddress = Address.fromRecipient(parcel.getRecipient());
        Optional<GeoEncodingCoordinate> recipientCoordinates = geoEncodingService.encodeAddress(recipientAddress);
        Optional<GeoEncodingCoordinate> senderCoordinates = geoEncodingService.encodeAddress(senderAddress);
        if (senderCoordinates.isEmpty()) {
            throw new BLSubmitParcelAddressIncorrect(senderAddress, null);
        }
        if (recipientCoordinates.isEmpty()) {
            throw new BLSubmitParcelAddressIncorrect(null, recipientAddress);

        }
        Optional<TruckEntity> nearestRecipientTruckOpt = this.truckRepository.findFirstNearestTruck((Point) geoEncodingCoordinateToGeometry(recipientCoordinates.get()));
        Optional<TruckEntity> nearestSenderTruckOpt = this.truckRepository.findFirstNearestTruck((Point) geoEncodingCoordinateToGeometry(senderCoordinates.get()));
        if (nearestRecipientTruckOpt.isEmpty()) {
            throw new BLNoTruckFound(recipientCoordinates.get());
        }
        if (nearestSenderTruckOpt.isEmpty()) {
            throw new BLNoTruckFound(senderCoordinates.get());
        }
        log.debug("Address validated");

        predictNextHops(parcel, nearestRecipientTruckOpt.get(), nearestSenderTruckOpt.get());
        ParcelEntity out = setTrackingIdAndSaveParcel(parcel);
        log.debug("Parcel saved successfully");
        return out;
    }

    private void predictNextHops(ParcelEntity parcel, TruckEntity nearestRecipientTruck, TruckEntity nearestSenderTruck) {
        List<WarehouseNextHopsEntity> recipientHopList = new ArrayList<>();
        List<HopArrivalEntity> hopArrivalList = new ArrayList<>();

        HopEntity tmpRecipient = nearestRecipientTruck;
        HopEntity tmpSender = nearestSenderTruck;

        // set date beforehand to set fixed start point
        OffsetDateTime dateTimeSender = OffsetDateTime.now();
        // initially add sender truck to hopArrivalList
        hopArrivalList.add(HopArrivalEntity.fromHop(tmpSender, dateTimeSender));
        while (true) {
            // get warehouse from nearestTruck
            Optional<WarehouseNextHopsEntity> warehouseNextHopsRecipient = warehouseNextHopsRepository.findByHop(tmpRecipient);
            Optional<WarehouseNextHopsEntity> warehouseNextHopsSender = warehouseNextHopsRepository.findByHop(tmpSender);
            if (warehouseNextHopsRecipient.isEmpty()) {
                throw new BLWarehouseNextHopsNotFound(nearestRecipientTruck.getCode());
            }
            if (warehouseNextHopsSender.isEmpty()) {
                throw new BLWarehouseNextHopsNotFound(nearestSenderTruck.getCode());
            }
            // calculate sender date
            dateTimeSender = dateTimeSender.plus(warehouseNextHopsSender.get().getTraveltimeMins(), ChronoUnit.MINUTES)
                    .plus(warehouseNextHopsSender.get().getHop().getProcessingDelayMins(), ChronoUnit.MINUTES);

            String recipientWarehouseCode = warehouseNextHopsRecipient.get().getWarehouse().getCode();
            String senderWarehouseCode = warehouseNextHopsSender.get().getWarehouse().getCode();
            if (recipientWarehouseCode.equals(senderWarehouseCode)) {
                // found common parent
                hopArrivalList.add(HopArrivalEntity.fromHop(warehouseNextHopsSender.get().getWarehouse(), dateTimeSender));
                recipientHopList.add(warehouseNextHopsRecipient.get());

                Collections.reverse(recipientHopList);
                OffsetDateTime dateTimeRecipient = dateTimeSender;
                for (WarehouseNextHopsEntity recipientNextHop : recipientHopList) {
                    dateTimeRecipient = dateTimeRecipient.plus(recipientNextHop.getTraveltimeMins(), ChronoUnit.MINUTES)
                            .plus(recipientNextHop.getWarehouse().getProcessingDelayMins(), ChronoUnit.MINUTES);
                    hopArrivalList.add(HopArrivalEntity.fromHop(recipientNextHop.getHop(), dateTimeRecipient));
                }

                log.debug("Found common parent");
                break;
            }
            recipientHopList.add(warehouseNextHopsRecipient.get());
            hopArrivalList.add(HopArrivalEntity.fromHop(warehouseNextHopsSender.get().getWarehouse(), dateTimeSender));

            // setting warehouses for the next iteration
            tmpRecipient = warehouseNextHopsRecipient.get().getWarehouse();
            tmpSender = warehouseNextHopsSender.get().getWarehouse();
        }

        // map hopEntity to hopArrivalEntity and set for futurehops
        parcel.setFutureHops(hopArrivalList);
        log.debug("Future hops set");
    }

    private ParcelEntity setTrackingIdAndSaveParcel(ParcelEntity parcel) {
        generateAndSetTrackingId(parcel);
        parcel.setState(TrackingInformationState.PICKUP);
        return parcelRepository.save(parcel);
    }

    /**
     * Recursively generates a unique tracking id for the given parcel
     */
    private void generateAndSetTrackingId(ParcelEntity parcel) {
        RgxGen rgxGen = new RgxGen(ParcelEntity.TRACKING_ID_PATTERN);
        String generatedTrackingId = rgxGen.generate();
        if (parcelRepository.findFirstByTrackingId(generatedTrackingId).isPresent()) {
            generateAndSetTrackingId(parcel);
        } else {
            parcel.setTrackingId(rgxGen.generate());
        }
    }

    @Override
    public Optional<ParcelEntity> trackParcel(String trackingId) {
        log.debug("Search parcel with tracking id {}", trackingId);
        return parcelRepository.findFirstByTrackingId(trackingId);
    }

    @Override
    public ParcelEntity transitionParcel(ParcelEntity parcel) {
        log.debug("Transition parcel with tracking id {}", parcel.getId());
        entityValidatorService.validate(parcel);
        return parcelRepository.save(parcel);
    }

    private void changeTrackingStateToDelivered(ParcelEntity parcelEntity) {
        parcelEntity.setState(TrackingInformationState.DELIVERED);
        parcelRepository.save(parcelEntity);
    }
}
