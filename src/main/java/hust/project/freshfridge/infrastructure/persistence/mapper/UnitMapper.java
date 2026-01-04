package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.Unit;
import hust.project.freshfridge.infrastructure.persistence.model.UnitModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UnitMapper {
    Unit toDomain(UnitModel model);
    UnitModel toModel(Unit entity);
}
