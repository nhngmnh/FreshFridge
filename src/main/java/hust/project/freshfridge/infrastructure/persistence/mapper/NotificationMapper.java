package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.Notification;
import hust.project.freshfridge.infrastructure.persistence.model.NotificationModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {
    Notification toDomain(NotificationModel model);
    NotificationModel toModel(Notification entity);
}
