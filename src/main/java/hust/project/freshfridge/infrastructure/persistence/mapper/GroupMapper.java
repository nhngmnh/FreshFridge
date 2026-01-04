package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.Group;
import hust.project.freshfridge.infrastructure.persistence.model.GroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMapper {
    Group toDomain(GroupModel model);
    GroupModel toModel(Group entity);
}
