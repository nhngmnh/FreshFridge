package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.Food;
import hust.project.freshfridge.infrastructure.persistence.model.FoodModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FoodMapper {
    Food toDomain(FoodModel model);
    FoodModel toModel(Food entity);
}
