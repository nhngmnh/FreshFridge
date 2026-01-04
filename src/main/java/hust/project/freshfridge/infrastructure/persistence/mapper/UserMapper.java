package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.User;
import hust.project.freshfridge.infrastructure.persistence.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toDomain(UserModel model);
    UserModel toModel(User entity);
}
