package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.KitchenItem;
import hust.project.freshfridge.infrastructure.persistence.model.KitchenItemModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KitchenItemMapper {
    KitchenItemModel toModel(KitchenItem entity);
    KitchenItem toDomain(KitchenItemModel model);
}
