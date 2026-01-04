package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.Category;
import hust.project.freshfridge.domain.repository.ICategoryRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.CategoryMapper;
import hust.project.freshfridge.infrastructure.persistence.model.CategoryModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements ICategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category save(Category category) {
        CategoryModel model = categoryMapper.toModel(category);
        CategoryModel savedModel = categoryJpaRepository.save(model);
        return categoryMapper.toDomain(savedModel);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id)
                .map(categoryMapper::toDomain);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name)
                .map(categoryMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll().stream()
                .map(categoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }
}
