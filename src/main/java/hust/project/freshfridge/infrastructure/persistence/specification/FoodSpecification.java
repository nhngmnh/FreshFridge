package hust.project.freshfridge.infrastructure.persistence.specification;

import hust.project.freshfridge.infrastructure.persistence.model.FoodModel;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification builder for FoodModel queries
 */
public class FoodSpecification {

    private FoodSpecification() {
        // Utility class
    }

    /**
     * Filter by group ID or system foods (groupId is null)
     */
    public static Specification<FoodModel> belongsToGroupOrSystem(Long groupId) {
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("groupId"), groupId),
                cb.isNull(root.get("groupId"))
        );
    }

    /**
     * Filter by category ID
     */
    public static Specification<FoodModel> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("categoryId"), categoryId);
        };
    }

    /**
     * Search by name (case-insensitive, partial match)
     */
    public static Specification<FoodModel> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%");
        };
    }

    /**
     * Filter by exact group ID
     */
    public static Specification<FoodModel> hasGroupId(Long groupId) {
        return (root, query, cb) -> {
            if (groupId == null) {
                return cb.isNull(root.get("groupId"));
            }
            return cb.equal(root.get("groupId"), groupId);
        };
    }

    /**
     * Order by name ascending
     */
    public static Specification<FoodModel> orderByName() {
        return (root, query, cb) -> {
            query.orderBy(cb.asc(root.get("name")));
            return cb.conjunction();
        };
    }

    /**
     * Filter system foods only (groupId is null)
     */
    public static Specification<FoodModel> isSystemFood() {
        return (root, query, cb) -> cb.isNull(root.get("groupId"));
    }

    /**
     * Build specification with all filters
     */
    public static Specification<FoodModel> buildSpec(Long groupId, Long categoryId, String name) {
        Specification<FoodModel> spec = belongsToGroupOrSystem(groupId);
        
        if (categoryId != null) {
            spec = spec.and(hasCategoryId(categoryId));
        }
        
        if (name != null && !name.isBlank()) {
            spec = spec.and(nameContains(name));
        }
        
        return spec.and(orderByName());
    }

    /**
     * Build specification for system foods only
     */
    public static Specification<FoodModel> buildSystemFoodSpec(Long categoryId, String name) {
        Specification<FoodModel> spec = isSystemFood();
        
        if (categoryId != null) {
            spec = spec.and(hasCategoryId(categoryId));
        }
        
        if (name != null && !name.isBlank()) {
            spec = spec.and(nameContains(name));
        }
        
        return spec.and(orderByName());
    }
}
