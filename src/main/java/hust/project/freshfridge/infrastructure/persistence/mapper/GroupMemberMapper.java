package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.GroupMember;
import hust.project.freshfridge.infrastructure.persistence.model.GroupMemberModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMemberMapper {
    GroupMember toDomain(GroupMemberModel model);
    GroupMemberModel toModel(GroupMember entity);
}
