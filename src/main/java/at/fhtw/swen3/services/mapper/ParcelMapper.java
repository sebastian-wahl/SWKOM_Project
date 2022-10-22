package at.fhtw.swen3.services.mapper;

import at.fhtw.swen3.persistence.entity.Parcel;
import at.fhtw.swen3.services.dto.NewParcelInfoDto;
import at.fhtw.swen3.services.dto.ParcelDto;
import at.fhtw.swen3.services.dto.TrackingInformationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParcelMapper {

    ParcelMapper INSTANCE = Mappers.getMapper(ParcelMapper.class);

    Parcel fromDto(ParcelDto parcelDto, NewParcelInfoDto newParcelInfoDto, TrackingInformationDto trackingInformationDto);
    ParcelDto toParcelDto(Parcel parcel);
    NewParcelInfoDto toNewParcelInfoDto(Parcel parcel);
    TrackingInformationDto toTrackingInformationDto(Parcel parcel);
}
