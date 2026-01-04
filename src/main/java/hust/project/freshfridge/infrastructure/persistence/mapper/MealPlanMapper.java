package hust.project.freshfridge.infrastructure.persistence.mapper;

import hust.project.freshfridge.domain.entity.MealPlan;
import hust.project.freshfridge.infrastructure.persistence.model.MealPlanModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MealPlanMapper {

    MealPlan toDomain(MealPlanModel model);

    MealPlanModel toModel(MealPlan domain);

    List<MealPlan> toDomainList(List<MealPlanModel> models);

    List<MealPlanModel> toModelList(List<MealPlan> domains);
}
