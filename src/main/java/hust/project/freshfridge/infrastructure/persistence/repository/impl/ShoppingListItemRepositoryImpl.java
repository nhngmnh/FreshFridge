package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.ShoppingListItem;
import hust.project.freshfridge.domain.repository.IShoppingListItemRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.ShoppingListItemMapper;
import hust.project.freshfridge.infrastructure.persistence.model.ShoppingListItemModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.ShoppingListItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ShoppingListItemRepositoryImpl implements IShoppingListItemRepository {

    private final ShoppingListItemJpaRepository shoppingListItemJpaRepository;
    private final ShoppingListItemMapper shoppingListItemMapper;

    @Override
    public ShoppingListItem save(ShoppingListItem item) {
        ShoppingListItemModel model = shoppingListItemMapper.toModel(item);
        ShoppingListItemModel savedModel = shoppingListItemJpaRepository.save(model);
        return shoppingListItemMapper.toDomain(savedModel);
    }

    @Override
    public List<ShoppingListItem> saveAll(List<ShoppingListItem> items) {
        List<ShoppingListItemModel> models = items.stream()
                .map(shoppingListItemMapper::toModel)
                .collect(Collectors.toList());
        List<ShoppingListItemModel> savedModels = shoppingListItemJpaRepository.saveAll(models);
        return savedModels.stream()
                .map(shoppingListItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShoppingListItem> findById(Long id) {
        return shoppingListItemJpaRepository.findById(id)
                .map(shoppingListItemMapper::toDomain);
    }

    @Override
    public List<ShoppingListItem> findByShoppingListId(Long listId) {
        return shoppingListItemJpaRepository.findByShoppingListIdOrderByCreatedAtAsc(listId).stream()
                .map(shoppingListItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ShoppingListItem> findByShoppingListIdAndFoodId(Long listId, Long foodId) {
        return shoppingListItemJpaRepository.findByShoppingListIdAndFoodId(listId, foodId)
                .map(shoppingListItemMapper::toDomain);
    }

    @Override
    public boolean existsByShoppingListIdAndFoodId(Long listId, Long foodId) {
        return shoppingListItemJpaRepository.existsByShoppingListIdAndFoodId(listId, foodId);
    }

    @Override
    public void deleteById(Long id) {
        shoppingListItemJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByShoppingListId(Long listId) {
        shoppingListItemJpaRepository.deleteByShoppingListId(listId);
    }
}
