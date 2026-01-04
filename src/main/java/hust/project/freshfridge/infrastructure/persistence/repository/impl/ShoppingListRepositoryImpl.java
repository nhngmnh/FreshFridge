package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.ShoppingList;
import hust.project.freshfridge.domain.repository.IShoppingListRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.ShoppingListMapper;
import hust.project.freshfridge.infrastructure.persistence.model.ShoppingListModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.ShoppingListJpaRepository;
import hust.project.freshfridge.infrastructure.persistence.specification.ShoppingListSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ShoppingListRepositoryImpl implements IShoppingListRepository {

    private final ShoppingListJpaRepository shoppingListJpaRepository;
    private final ShoppingListMapper shoppingListMapper;

    @Override
    public ShoppingList save(ShoppingList list) {
        ShoppingListModel model = shoppingListMapper.toModel(list);
        ShoppingListModel savedModel = shoppingListJpaRepository.save(model);
        return shoppingListMapper.toDomain(savedModel);
    }

    @Override
    public Optional<ShoppingList> findById(Long id) {
        return shoppingListJpaRepository.findById(id)
                .map(shoppingListMapper::toDomain);
    }

    @Override
    public List<ShoppingList> findByGroupId(Long groupId) {
        return shoppingListJpaRepository.findByGroupIdOrderByCreatedAtDesc(groupId).stream()
                .map(shoppingListMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingList> findByGroupId(Long groupId, String status, String name) {
        Specification<ShoppingListModel> spec = ShoppingListSpecification.buildSpec(groupId, status, name);
        return shoppingListJpaRepository.findAll(spec).stream()
                .map(shoppingListMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingList> findByAssignedToUserId(Long userId) {
        return shoppingListJpaRepository.findByAssignedToUserIdOrderByShoppingDateAsc(userId).stream()
                .map(shoppingListMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingList> findByShoppingDateAndStatus(LocalDate date, String status) {
        return shoppingListJpaRepository.findByShoppingDateAndStatusOrderByIdAsc(date, status).stream()
                .map(shoppingListMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        shoppingListJpaRepository.deleteById(id);
    }
}
