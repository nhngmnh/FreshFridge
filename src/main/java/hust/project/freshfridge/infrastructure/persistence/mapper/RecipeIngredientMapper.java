package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.RecipeIngredient;
import hust.project.freshfridge.infrastructure.persistence.model.RecipeIngredientModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {
    RecipeIngredientModel toModel(RecipeIngredient entity);
    RecipeIngredient toDomain(RecipeIngredientModel model);
}
