package at.fhtw.swen3.services.impl;


import at.fhtw.swen3.persistence.entity.ParcelEntity;
import at.fhtw.swen3.services.ParcelApi;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.Parcel;
import at.fhtw.swen3.services.dto.TrackingInformation;
import at.fhtw.swen3.services.exception.ValidationException;
import at.fhtw.swen3.services.mapper.ParcelMapper;
import at.fhtw.swen3.services.validation.EntityValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-13T12:19:08.753753Z[Etc/UTC]")
@Controller
@Slf4j
public class ParcelApiController implements ParcelApi {

    @Autowired
    private EntityValidatorService entityValidatorService;

    private final NativeWebRequest request;

    @Autowired
    public ParcelApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> reportParcelDelivery(String trackingId) {
        return ParcelApi.super.reportParcelDelivery(trackingId);
    }

    @Override
    public ResponseEntity<Void> reportParcelHop(String trackingId, String code) {
        return ParcelApi.super.reportParcelHop(trackingId, code);
    }

    @Override
    public ResponseEntity<NewParcelInfo> submitParcel(Parcel parcel) {
        ParcelMapper parcelMapper = ParcelMapper.INSTANCE;
        ParcelEntity parcelEntity = parcelMapper.fromDto(parcel);

        try {
            entityValidatorService.validate(parcelEntity);
        } catch (ValidationException e) {
            String message = e.getValidationMessages().stream().collect(Collectors.joining(";"));
            log.info(message);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        // TODO: persist entity

        NewParcelInfo newParcelInfo = parcelMapper.toNewParcelInfoDto(parcelEntity);
        return ResponseEntity.of(Optional.ofNullable(newParcelInfo));
    }

    @Override
    public ResponseEntity<TrackingInformation> trackParcel(String trackingId) {
        return ParcelApi.super.trackParcel(trackingId);
    }

    @Override
    public ResponseEntity<NewParcelInfo> transitionParcel(String trackingId, Parcel parcel) {
        return ParcelApi.super.transitionParcel(trackingId, parcel);
    }

}
