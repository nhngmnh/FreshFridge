package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.RecipeIngredient;
import hust.project.freshfridge.domain.repository.IRecipeIngredientRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.RecipeIngredientMapper;
import hust.project.freshfridge.infrastructure.persistence.model.RecipeIngredientModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.RecipeIngredientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RecipeIngredientRepositoryImpl implements IRecipeIngredientRepository {

    private final RecipeIngredientJpaRepository recipeIngredientJpaRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;

    @Override
    public RecipeIngredient save(RecipeIngredient ingredient) {
        RecipeIngredientModel model = recipeIngredientMapper.toModel(ingredient);
        RecipeIngredientModel savedModel = recipeIngredientJpaRepository.save(model);
        return recipeIngredientMapper.toDomain(savedModel);
    }

    @Override
    public List<RecipeIngredient> saveAll(List<RecipeIngredient> ingredients) {
        List<RecipeIngredientModel> models = ingredients.stream()
                .map(recipeIngredientMapper::toModel)
                .collect(Collectors.toList());
        List<RecipeIngredientModel> savedModels = recipeIngredientJpaRepository.saveAll(models);
        return savedModels.stream()
                .map(recipeIngredientMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RecipeIngredient> findById(Long id) {
        return recipeIngredientJpaRepository.findById(id)
                .map(recipeIngredientMapper::toDomain);
    }

    @Override
    public List<RecipeIngredient> findByRecipeId(Long recipeId) {
        return recipeIngredientJpaRepository.findByRecipeId(recipeId).stream()
                .map(recipeIngredientMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeIngredient> findByFoodId(Long foodId) {
        return recipeIngredientJpaRepository.findByFoodId(foodId).stream()
                .map(recipeIngredientMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeIngredient> findMainIngredientsByFoodId(Long foodId) {
        return recipeIngredientJpaRepository.findByFoodIdAndIsMainIngredientTrue(foodId).stream()
                .map(recipeIngredientMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteByRecipeId(Long recipeId) {
        recipeIngredientJpaRepository.deleteByRecipeId(recipeId);
    }

    @Override
    public void deleteById(Long id) {
        recipeIngredientJpaRepository.deleteById(id);
    }
}
