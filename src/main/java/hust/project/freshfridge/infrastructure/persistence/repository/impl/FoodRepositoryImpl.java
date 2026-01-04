package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.Food;
import hust.project.freshfridge.domain.repository.IFoodRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.FoodMapper;
import hust.project.freshfridge.infrastructure.persistence.model.FoodModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.FoodJpaRepository;
import hust.project.freshfridge.infrastructure.persistence.specification.FoodSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FoodRepositoryImpl implements IFoodRepository {

    private final FoodJpaRepository foodJpaRepository;
    private final FoodMapper foodMapper;

    @Override
    public Food save(Food food) {
        FoodModel model = foodMapper.toModel(food);
        FoodModel savedModel = foodJpaRepository.save(model);
        return foodMapper.toDomain(savedModel);
    }

    @Override
    public Optional<Food> findById(Long id) {
        return foodJpaRepository.findById(id)
                .map(foodMapper::toDomain);
    }

    @Override
    public Optional<Food> findByNameAndGroupId(String name, Long groupId) {
        return foodJpaRepository.findByNameAndGroupId(name, groupId)
                .map(foodMapper::toDomain);
    }

    @Override
    public List<Food> findAll(Long groupId, Long categoryId, String name) {
        Specification<FoodModel> spec = FoodSpecification.buildSpec(groupId, categoryId, name);
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        
        return foodJpaRepository.findAll(spec, sort).stream()
                .map(foodMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Food> findSystemFoods(Long categoryId, String name) {
        Specification<FoodModel> spec = FoodSpecification.buildSystemFoodSpec(categoryId, name);
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        
        return foodJpaRepository.findAll(spec, sort).stream()
                .map(foodMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndGroupId(String name, Long groupId) {
        return foodJpaRepository.existsByNameAndGroupId(name, groupId);
    }

    @Override
    public boolean existsSystemFoodByName(String name) {
        return foodJpaRepository.existsSystemFoodByName(name);
    }

    @Override
    public void deleteById(Long id) {
        foodJpaRepository.deleteById(id);
    }
}
