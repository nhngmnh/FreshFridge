package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.Recipe;
import hust.project.freshfridge.infrastructure.persistence.model.RecipeModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeModel toModel(Recipe entity);
    Recipe toDomain(RecipeModel model);
}
