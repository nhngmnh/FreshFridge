package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.SystemLog;
import hust.project.freshfridge.infrastructure.persistence.model.SystemLogModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SystemLogMapper {
    SystemLog toDomain(SystemLogModel model);
    SystemLogModel toModel(SystemLog entity);
}
