package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.entity.Category;
import hust.project.freshfridge.domain.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryUseCase {

    private final ICategoryRepository categoryRepository;

    public CategoryListResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        
        List<CategoryResponse> responses = categories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return CategoryListResponse.builder()
                .categories(responses)
                .total(responses.size())
                .build();
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .build();
    }
}
