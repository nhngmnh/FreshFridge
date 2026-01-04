package hust.project.freshfridge.infrastructure.persistence.repository.jpa;

import hust.project.freshfridge.infrastructure.persistence.model.FoodModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodJpaRepository extends JpaRepository<FoodModel, Long>, JpaSpecificationExecutor<FoodModel> {
    
    Optional<FoodModel> findByNameAndGroupId(String name, Long groupId);
    
    boolean existsByNameAndGroupId(String name, Long groupId);
    
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FoodModel f WHERE f.name = :name AND f.groupId IS NULL")
    boolean existsSystemFoodByName(@Param("name") String name);
}
