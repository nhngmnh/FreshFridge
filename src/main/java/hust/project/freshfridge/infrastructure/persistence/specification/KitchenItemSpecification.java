package hust.project.freshfridge.infrastructure.persistence.specification;

import hust.project.freshfridge.infrastructure.persistence.model.KitchenItemModel;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class KitchenItemSpecification {

    private KitchenItemSpecification() {
    }

    public static Specification<KitchenItemModel> hasGroupId(Long groupId) {
        return (root, query, cb) -> cb.equal(root.get("groupId"), groupId);
    }

    public static Specification<KitchenItemModel> hasFoodId(Long foodId) {
        return (root, query, cb) -> {
            if (foodId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("foodId"), foodId);
        };
    }

    public static Specification<KitchenItemModel> expiryDateBefore(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("expiryDate"), date);
        };
    }

    public static Specification<KitchenItemModel> orderByExpiryDate() {
        return (root, query, cb) -> {
            query.orderBy(cb.asc(root.get("expiryDate")));
            return cb.conjunction();
        };
    }

    public static Specification<KitchenItemModel> buildSpec(Long groupId, Long foodId, LocalDate expiryBefore) {
        Specification<KitchenItemModel> spec = hasGroupId(groupId);

        if (foodId != null) {
            spec = spec.and(hasFoodId(foodId));
        }

        if (expiryBefore != null) {
            spec = spec.and(expiryDateBefore(expiryBefore));
        }

        return spec.and(orderByExpiryDate());
    }
}
