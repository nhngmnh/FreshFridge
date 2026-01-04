package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.Category;
import hust.project.freshfridge.infrastructure.persistence.model.CategoryModel;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toDomain(CategoryModel model);
    CategoryModel toModel(Category entity);
}
