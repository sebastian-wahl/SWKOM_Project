package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entities.ParcelEntity;
import at.fhtw.swen3.services.dto.NewParcelInfo;
import at.fhtw.swen3.services.dto.Parcel;
import at.fhtw.swen3.services.dto.TrackingInformation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParcelMapper {

    ParcelMapper INSTANCE = Mappers.getMapper(ParcelMapper.class);

    ParcelEntity fromDto(Parcel parcel);
    Parcel toParcelDto(ParcelEntity parcel);
    NewParcelInfo toNewParcelInfoDto(ParcelEntity parcel);
    TrackingInformation toTrackingInformationDto(ParcelEntity parcel);
}
