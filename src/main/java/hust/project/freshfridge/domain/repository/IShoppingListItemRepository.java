package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.ShoppingListItem;

import java.util.List;
import java.util.Optional;

public interface IShoppingListItemRepository {
    ShoppingListItem save(ShoppingListItem item);
    List<ShoppingListItem> saveAll(List<ShoppingListItem> items);
    Optional<ShoppingListItem> findById(Long id);
    List<ShoppingListItem> findByShoppingListId(Long listId);
    Optional<ShoppingListItem> findByShoppingListIdAndFoodId(Long listId, Long foodId);
    boolean existsByShoppingListIdAndFoodId(Long listId, Long foodId);
    void deleteById(Long id);
    void deleteByShoppingListId(Long listId);
}
