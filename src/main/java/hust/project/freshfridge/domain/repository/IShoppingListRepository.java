package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.ShoppingList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IShoppingListRepository {
    ShoppingList save(ShoppingList list);
    Optional<ShoppingList> findById(Long id);
    List<ShoppingList> findByGroupId(Long groupId);
    List<ShoppingList> findByGroupId(Long groupId, String status, String name);
    List<ShoppingList> findByAssignedToUserId(Long userId);
    List<ShoppingList> findByShoppingDateAndStatus(LocalDate date, String status);
    void deleteById(Long id);
}
