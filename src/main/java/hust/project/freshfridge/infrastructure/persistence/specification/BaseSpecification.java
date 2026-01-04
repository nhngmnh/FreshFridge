package hust.project.freshfridge.infrastructure.persistence.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 * Base specification class providing common operations for building JPA Criteria queries.
 * @param <T> The entity type
 */
public abstract class BaseSpecification<T> implements Specification<T> {

    @Override
    public abstract Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);

    /**
     * Combines this specification with another using AND
     */
    public Specification<T> and(Specification<T> other) {
        Specification<T> self = this;
        return (root, query, cb) -> cb.and(
                self.toPredicate(root, query, cb),
                other.toPredicate(root, query, cb)
        );
    }

    /**
     * Combines this specification with another using OR
     */
    public Specification<T> or(Specification<T> other) {
        Specification<T> self = this;
        return (root, query, cb) -> cb.or(
                self.toPredicate(root, query, cb),
                other.toPredicate(root, query, cb)
        );
    }

    /**
     * Creates an equals specification
     */
    public static <T> Specification<T> equals(String field, Object value) {
        return (root, query, cb) -> {
            if (value == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get(field), value);
        };
    }

    /**
     * Creates an is null specification
     */
    public static <T> Specification<T> isNull(String field) {
        return (root, query, cb) -> cb.isNull(root.get(field));
    }

    /**
     * Creates an is not null specification
     */
    public static <T> Specification<T> isNotNull(String field) {
        return (root, query, cb) -> cb.isNotNull(root.get(field));
    }

    /**
     * Creates a like specification (case-insensitive)
     */
    public static <T> Specification<T> like(String field, String value) {
        return (root, query, cb) -> {
            if (value == null || value.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
        };
    }

    /**
     * Creates an OR condition between two specifications
     */
    public static <T> Specification<T> or(Specification<T> spec1, Specification<T> spec2) {
        return (root, query, cb) -> cb.or(
                spec1.toPredicate(root, query, cb),
                spec2.toPredicate(root, query, cb)
        );
    }

    /**
     * Creates an AND condition between two specifications
     */
    public static <T> Specification<T> and(Specification<T> spec1, Specification<T> spec2) {
        return (root, query, cb) -> cb.and(
                spec1.toPredicate(root, query, cb),
                spec2.toPredicate(root, query, cb)
        );
    }
}
