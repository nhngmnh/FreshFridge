package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.ShoppingListItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListItemJpaRepository extends JpaRepository<ShoppingListItemModel, Long> {
    
    List<ShoppingListItemModel> findByShoppingListIdOrderByCreatedAtAsc(Long shoppingListId);
    
    Optional<ShoppingListItemModel> findByShoppingListIdAndFoodId(Long shoppingListId, Long foodId);
    
    boolean existsByShoppingListIdAndFoodId(Long shoppingListId, Long foodId);
    
    @Modifying
    @Query("DELETE FROM ShoppingListItemModel s WHERE s.shoppingListId = :listId")
    void deleteByShoppingListId(@Param("listId") Long listId);

    // Count purchased items for a group's shopping lists
    @Query("SELECT COUNT(i) FROM ShoppingListItemModel i JOIN ShoppingListModel l ON i.shoppingListId = l.id " +
           "WHERE l.groupId = :groupId AND i.isPurchased = true")
    long countPurchasedItemsByGroupId(@Param("groupId") Long groupId);

    // Find items by shopping list IDs
    List<ShoppingListItemModel> findByShoppingListIdIn(List<Long> shoppingListIds);
}
