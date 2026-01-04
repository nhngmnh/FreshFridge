package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.ShoppingListItem;
import hust.project.freshfridge.infrastructure.persistence.model.ShoppingListItemModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShoppingListItemMapper {
    ShoppingListItemModel toModel(ShoppingListItem entity);
    ShoppingListItem toDomain(ShoppingListItemModel model);
}
