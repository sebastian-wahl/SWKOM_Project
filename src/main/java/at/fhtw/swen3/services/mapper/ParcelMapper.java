package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.Parcel;
import at.fhtw.swen3.services.dto.TrackingInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = RecipientMapper.class)
public interface ParcelMapper {

    ParcelMapper INSTANCE = Mappers.getMapper(ParcelMapper.class);

    @Mapping(target = "state", constant = "PICKUP")
    ParcelEntity fromDto(Parcel parcel);
    @Mapping(target = "state", constant = "PICKUP")
    ParcelEntity fromDto(String trackingId, Parcel parcel);
    Parcel toParcelDto(ParcelEntity parcel);
    NewParcelInfo toNewParcelInfoDto(ParcelEntity parcel);
    TrackingInformation toTrackingInformationDto(ParcelEntity parcel);
}
