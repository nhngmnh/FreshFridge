package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.Recipe;
import hust.project.freshfridge.domain.repository.IRecipeRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.RecipeMapper;
import hust.project.freshfridge.infrastructure.persistence.model.RecipeModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.RecipeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements IRecipeRepository {

    private final RecipeJpaRepository recipeJpaRepository;
    private final RecipeMapper recipeMapper;

    @Override
    public Recipe save(Recipe recipe) {
        RecipeModel model = recipeMapper.toModel(recipe);
        RecipeModel savedModel = recipeJpaRepository.save(model);
        return recipeMapper.toDomain(savedModel);
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return recipeJpaRepository.findById(id)
                .map(recipeMapper::toDomain);
    }

    @Override
    public List<Recipe> findByGroupId(Long groupId) {
        return recipeJpaRepository.findByGroupId(groupId).stream()
                .map(recipeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Recipe> findSystemRecipes() {
        return recipeJpaRepository.findSystemRecipes().stream()
                .map(recipeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Recipe> findByGroupIdOrSystem(Long groupId) {
        return recipeJpaRepository.findByGroupIdOrSystem(groupId).stream()
                .map(recipeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Recipe> searchByName(String name, Long groupId) {
        return recipeJpaRepository.searchByName(name, groupId).stream()
                .map(recipeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Recipe> findAllWithFilters(Long groupId, String name) {
        if (groupId != null) {
            return recipeJpaRepository.findAllWithFilters(groupId, name).stream()
                    .map(recipeMapper::toDomain)
                    .collect(Collectors.toList());
        } else {
            return recipeJpaRepository.findSystemRecipesWithFilters(name).stream()
                    .map(recipeMapper::toDomain)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteById(Long id) {
        recipeJpaRepository.deleteById(id);
    }
}
