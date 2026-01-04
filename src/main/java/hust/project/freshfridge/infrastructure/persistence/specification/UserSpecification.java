package hust.project.freshfridge.infrastructure.persistence.specification;

import hust.project.freshfridge.infrastructure.persistence.model.UserModel;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<UserModel> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<UserModel> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<UserModel> hasRole(String role) {
        return (root, query, cb) -> {
            if (role == null || role.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("role"), role);
        };
    }

    public static Specification<UserModel> hasIsActive(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<UserModel> buildSpec(String email, String name, String role, Boolean isActive) {
        Specification<UserModel> spec = UserSpecification.hasEmail(email)
                .and(hasName(name))
                .and(hasRole(role))
                .and(hasIsActive(isActive));
        
        return spec.and((root, query, cb) -> {
            query.orderBy(cb.desc(root.get("createdAt")));
            return cb.conjunction();
        });
    }
}
