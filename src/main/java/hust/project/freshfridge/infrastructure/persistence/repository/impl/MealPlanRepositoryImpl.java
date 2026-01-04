package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.MealPlan;
import hust.project.freshfridge.domain.repository.IMealPlanRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.MealPlanMapper;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.MealPlanJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MealPlanRepositoryImpl implements IMealPlanRepository {

    private final MealPlanJpaRepository jpaRepository;
    private final MealPlanMapper mapper;

    @Override
    public MealPlan save(MealPlan mealPlan) {
        var model = mapper.toModel(mealPlan);
        var saved = jpaRepository.save(model);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<MealPlan> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<MealPlan> findByGroupId(Long groupId) {
        return mapper.toDomainList(
                jpaRepository.findByGroupIdOrderByMealDateDescMealNameAsc(groupId));
    }

    @Override
    public List<MealPlan> findByGroupIdAndMealDate(Long groupId, LocalDate mealDate) {
        return mapper.toDomainList(
                jpaRepository.findByGroupIdAndMealDateOrderByMealNameAsc(groupId, mealDate));
    }

    @Override
    public List<MealPlan> findByGroupIdAndMealDateBetween(Long groupId, LocalDate startDate, LocalDate endDate) {
        return mapper.toDomainList(
                jpaRepository.findByGroupIdAndMealDateBetweenOrderByMealDateAscMealNameAsc(groupId, startDate, endDate));
    }

    @Override
    public List<MealPlan> findByGroupIdAndMealDateAndMealName(Long groupId, LocalDate mealDate, String mealName) {
        return mapper.toDomainList(
                jpaRepository.findByGroupIdAndMealDateAndMealNameOrderByIdAsc(groupId, mealDate, mealName));
    }

    @Override
    public List<MealPlan> findByMealDate(LocalDate mealDate) {
        return mapper.toDomainList(
                jpaRepository.findByMealDateOrderByGroupIdAscMealNameAsc(mealDate));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
