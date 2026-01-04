package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.KitchenItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface KitchenItemJpaRepository extends JpaRepository<KitchenItemModel, Long>, 
        JpaSpecificationExecutor<KitchenItemModel> {
    
    Optional<KitchenItemModel> findByFoodIdAndGroupId(Long foodId, Long groupId);
    
    List<KitchenItemModel> findByGroupId(Long groupId);
    
    boolean existsByFoodIdAndGroupId(Long foodId, Long groupId);

    @Query("SELECT k FROM KitchenItemModel k WHERE k.groupId = :groupId AND k.expiryDate IS NOT NULL AND k.expiryDate <= :thresholdDate ORDER BY k.expiryDate ASC")
    List<KitchenItemModel> findExpiringItems(@Param("groupId") Long groupId, @Param("thresholdDate") LocalDate thresholdDate);

    @Query("SELECT k FROM KitchenItemModel k JOIN FoodModel f ON k.foodId = f.id " +
           "WHERE k.groupId = :groupId " +
           "AND (:foodId IS NULL OR k.foodId = :foodId) " +
           "AND (:foodName IS NULL OR LOWER(CAST(f.name AS string)) LIKE LOWER(CONCAT('%', CAST(:foodName AS string), '%'))) " +
           "AND (:expiryBefore IS NULL OR k.expiryDate <= :expiryBefore) " +
           "AND (:categoryId IS NULL OR f.categoryId = :categoryId) " +
           "ORDER BY k.expiryDate ASC NULLS LAST")
    List<KitchenItemModel> findAllWithFilters(
            @Param("groupId") Long groupId,
            @Param("foodId") Long foodId,
            @Param("foodName") String foodName,
            @Param("expiryBefore") LocalDate expiryBefore,
            @Param("categoryId") Long categoryId);

    @Query("SELECT k FROM KitchenItemModel k JOIN FoodModel f ON k.foodId = f.id " +
           "WHERE k.groupId = :groupId " +
           "AND (:foodId IS NULL OR k.foodId = :foodId) " +
           "AND (:foodName IS NULL OR LOWER(CAST(f.name AS string)) LIKE LOWER(CONCAT('%', CAST(:foodName AS string), '%'))) " +
           "AND (:expiryBefore IS NULL OR k.expiryDate <= :expiryBefore) " +
           "AND (:categoryId IS NULL OR f.categoryId = :categoryId) " +
           "ORDER BY f.name ASC")
    List<KitchenItemModel> findAllWithFiltersSortByNameAsc(
            @Param("groupId") Long groupId,
            @Param("foodId") Long foodId,
            @Param("foodName") String foodName,
            @Param("expiryBefore") LocalDate expiryBefore,
            @Param("categoryId") Long categoryId);

    @Query("SELECT k FROM KitchenItemModel k JOIN FoodModel f ON k.foodId = f.id " +
           "WHERE k.groupId = :groupId " +
           "AND (:foodId IS NULL OR k.foodId = :foodId) " +
           "AND (:foodName IS NULL OR LOWER(CAST(f.name AS string)) LIKE LOWER(CONCAT('%', CAST(:foodName AS string), '%'))) " +
           "AND (:expiryBefore IS NULL OR k.expiryDate <= :expiryBefore) " +
           "AND (:categoryId IS NULL OR f.categoryId = :categoryId) " +
           "ORDER BY f.name DESC")
    List<KitchenItemModel> findAllWithFiltersSortByNameDesc(
            @Param("groupId") Long groupId,
            @Param("foodId") Long foodId,
            @Param("foodName") String foodName,
            @Param("expiryBefore") LocalDate expiryBefore,
            @Param("categoryId") Long categoryId);

    @Query("SELECT k FROM KitchenItemModel k JOIN FoodModel f ON k.foodId = f.id " +
           "WHERE k.groupId = :groupId " +
           "AND (:foodId IS NULL OR k.foodId = :foodId) " +
           "AND (:foodName IS NULL OR LOWER(CAST(f.name AS string)) LIKE LOWER(CONCAT('%', CAST(:foodName AS string), '%'))) " +
           "AND (:expiryBefore IS NULL OR k.expiryDate <= :expiryBefore) " +
           "AND (:categoryId IS NULL OR f.categoryId = :categoryId) " +
           "ORDER BY k.expiryDate DESC NULLS LAST")
    List<KitchenItemModel> findAllWithFiltersSortByExpiryDesc(
            @Param("groupId") Long groupId,
            @Param("foodId") Long foodId,
            @Param("foodName") String foodName,
            @Param("expiryBefore") LocalDate expiryBefore,
            @Param("categoryId") Long categoryId);

    @Query("SELECT k FROM KitchenItemModel k JOIN FoodModel f ON k.foodId = f.id " +
           "WHERE k.groupId = :groupId " +
           "AND (:foodId IS NULL OR k.foodId = :foodId) " +
           "AND (:foodName IS NULL OR LOWER(CAST(f.name AS string)) LIKE LOWER(CONCAT('%', CAST(:foodName AS string), '%'))) " +
           "AND (:expiryBefore IS NULL OR k.expiryDate <= :expiryBefore) " +
           "AND (:categoryId IS NULL OR f.categoryId = :categoryId) " +
           "ORDER BY k.createdAt ASC")
    List<KitchenItemModel> findAllWithFiltersSortByCreatedAtAsc(
            @Param("groupId") Long groupId,
            @Param("foodId") Long foodId,
            @Param("foodName") String foodName,
            @Param("expiryBefore") LocalDate expiryBefore,
            @Param("categoryId") Long categoryId);

    @Query("SELECT k FROM KitchenItemModel k JOIN FoodModel f ON k.foodId = f.id " +
           "WHERE k.groupId = :groupId " +
           "AND (:foodId IS NULL OR k.foodId = :foodId) " +
           "AND (:foodName IS NULL OR LOWER(CAST(f.name AS string)) LIKE LOWER(CONCAT('%', CAST(:foodName AS string), '%'))) " +
           "AND (:expiryBefore IS NULL OR k.expiryDate <= :expiryBefore) " +
           "AND (:categoryId IS NULL OR f.categoryId = :categoryId) " +
           "ORDER BY k.createdAt DESC")
    List<KitchenItemModel> findAllWithFiltersSortByCreatedAtDesc(
            @Param("groupId") Long groupId,
            @Param("foodId") Long foodId,
            @Param("foodName") String foodName,
            @Param("expiryBefore") LocalDate expiryBefore,
            @Param("categoryId") Long categoryId);

    // Count items by expiry status
    @Query("SELECT COUNT(k) FROM KitchenItemModel k WHERE k.groupId = :groupId AND k.expiryDate < :today")
    long countExpiredItems(@Param("groupId") Long groupId, @Param("today") LocalDate today);

    @Query("SELECT COUNT(k) FROM KitchenItemModel k WHERE k.groupId = :groupId AND k.expiryDate >= :today AND k.expiryDate <= :warningDate")
    long countExpiringItems(@Param("groupId") Long groupId, @Param("today") LocalDate today, @Param("warningDate") LocalDate warningDate);

    @Query("SELECT COUNT(k) FROM KitchenItemModel k WHERE k.groupId = :groupId AND (k.expiryDate IS NULL OR k.expiryDate > :warningDate)")
    long countGoodItems(@Param("groupId") Long groupId, @Param("warningDate") LocalDate warningDate);

    // Count items added in date range
    @Query("SELECT COUNT(k) FROM KitchenItemModel k WHERE k.groupId = :groupId AND k.createdAt >= :startDate AND k.createdAt < :endDate")
    long countItemsAddedBetween(@Param("groupId") Long groupId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
