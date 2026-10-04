package app.api.patient;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import app.api.patient.dtos.PatientUpdateDto;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(PatientUpdateDto dto, @MappingTarget PatientEntity entity);
}
