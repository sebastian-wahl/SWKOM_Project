package at.fhtw.swen3.controller.rest;


import at.fhtw.swen3.controller.ParcelApi;
import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.services.ParcelService;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.Parcel;
import at.fhtw.swen3.services.dto.TrackingInformation;
import at.fhtw.swen3.services.mapper.ParcelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Generated;
import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-13T12:19:08.753753Z[Etc/UTC]")
@Controller
@Slf4j
public class ParcelApiController implements ParcelApi {

    @Autowired
    private ParcelService parcelService;

    @Autowired
    private NativeWebRequest request;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Void> reportParcelDelivery(String trackingId) {
        parcelService.reportParcelDelivery(trackingId);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Void> reportParcelHop(String trackingId, String code) {
        parcelService.reportParcelHop(trackingId, code);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<NewParcelInfo> submitParcel(Parcel parcel) {
        ParcelEntity parcelEntity = ParcelMapper.INSTANCE.fromDto(parcel);

        NewParcelInfo newParcelInfo = ParcelMapper.INSTANCE.toNewParcelInfoDto(parcelService.submitParcel(parcelEntity));

        return Optional.ofNullable(newParcelInfo)
                .map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<TrackingInformation> trackParcel(String trackingId) {
        return ResponseEntity.of(parcelService.trackParcel(trackingId).map(parcelEntity -> ParcelMapper.INSTANCE.toTrackingInformationDto(parcelEntity)));
    }

    @Override
    public ResponseEntity<NewParcelInfo> transitionParcel(String trackingId, Parcel parcel) {
        ParcelEntity parcelEntity = ParcelMapper.INSTANCE.fromDto(trackingId, parcel);
        NewParcelInfo newParcelInfo = ParcelMapper.INSTANCE.toNewParcelInfoDto(parcelService.transitionParcel(parcelEntity));

        return ResponseEntity.ok(newParcelInfo);
    }

}
