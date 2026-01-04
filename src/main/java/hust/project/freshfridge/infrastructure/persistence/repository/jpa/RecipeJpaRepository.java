package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeJpaRepository extends JpaRepository<RecipeModel, Long> {
    List<RecipeModel> findByGroupId(Long groupId);

    @Query("SELECT r FROM RecipeModel r WHERE r.isSystem = true")
    List<RecipeModel> findSystemRecipes();

    @Query("SELECT r FROM RecipeModel r WHERE r.groupId = :groupId OR r.isSystem = true")
    List<RecipeModel> findByGroupIdOrSystem(@Param("groupId") Long groupId);

    @Query("SELECT r FROM RecipeModel r WHERE (r.groupId = :groupId OR r.isSystem = true) " +
           "AND LOWER(CAST(r.name AS string)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<RecipeModel> searchByName(@Param("name") String name, @Param("groupId") Long groupId);

    @Query("SELECT r FROM RecipeModel r WHERE " +
           "(r.isSystem = true OR r.groupId = :groupId) " +
           "AND (:name IS NULL OR LOWER(CAST(r.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
           "ORDER BY r.createdAt DESC")
    List<RecipeModel> findAllWithFilters(@Param("groupId") Long groupId, @Param("name") String name);

    @Query("SELECT r FROM RecipeModel r WHERE r.isSystem = true " +
           "AND (:name IS NULL OR LOWER(CAST(r.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) " +
           "ORDER BY r.createdAt DESC")
    List<RecipeModel> findSystemRecipesWithFilters(@Param("name") String name);
}
