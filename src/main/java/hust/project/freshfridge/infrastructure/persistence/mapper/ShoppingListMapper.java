package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.ShoppingList;
import hust.project.freshfridge.infrastructure.persistence.model.ShoppingListModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShoppingListMapper {
    ShoppingListModel toModel(ShoppingList entity);
    ShoppingList toDomain(ShoppingListModel model);
}
