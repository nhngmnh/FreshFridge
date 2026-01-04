package hust.project.freshfridge.infrastructure.persistence.specification;

import hust.project.freshfridge.infrastructure.persistence.model.ShoppingListModel;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification builder for ShoppingListModel queries
 */
public class ShoppingListSpecification {

    private ShoppingListSpecification() {
        // Utility class
    }

    /**
     * Filter by group ID
     */
    public static Specification<ShoppingListModel> hasGroupId(Long groupId) {
        return (root, query, cb) -> cb.equal(root.get("groupId"), groupId);
    }

    /**
     * Filter by status
     */
    public static Specification<ShoppingListModel> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    /**
     * Filter by name (case-insensitive, contains)
     */
    public static Specification<ShoppingListModel> hasNameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    /**
     * Order by created date descending
     */
    public static Specification<ShoppingListModel> orderByCreatedAtDesc() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("createdAt")));
            return cb.conjunction();
        };
    }

    /**
     * Build specification with all filters
     */
    public static Specification<ShoppingListModel> buildSpec(Long groupId, String status, String name) {
        Specification<ShoppingListModel> spec = hasGroupId(groupId);

        if (status != null && !status.isBlank()) {
            spec = spec.and(hasStatus(status));
        }

        if (name != null && !name.isBlank()) {
            spec = spec.and(hasNameLike(name));
        }

        return spec.and(orderByCreatedAtDesc());
    }
}
