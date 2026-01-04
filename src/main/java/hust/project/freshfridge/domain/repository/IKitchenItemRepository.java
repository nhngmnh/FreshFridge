package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.KitchenItem;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IKitchenItemRepository {
    KitchenItem save(KitchenItem item);
    Optional<KitchenItem> findById(Long id);
    Optional<KitchenItem> findByFoodIdAndGroupId(Long foodId, Long groupId);
    List<KitchenItem> findByGroupId(Long groupId);
    List<KitchenItem> findAll(Long groupId, Long foodId, String foodName, LocalDate expiryBefore,
                              Long categoryId, String sortBy, String sortOrder);
    List<KitchenItem> findExpiringItems(Long groupId, LocalDate thresholdDate);
    boolean existsByFoodIdAndGroupId(Long foodId, Long groupId);
    void deleteById(Long id);
}
