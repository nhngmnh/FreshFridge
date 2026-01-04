package hust.project.freshfridge.domain.repository;

import hust.project.freshfridge.domain.entity.Unit;
import java.util.List;
import java.util.Optional;

public interface IUnitRepository {
    Unit save(Unit unit);
    Optional<Unit> findById(Long id);
    Optional<Unit> findByName(String name);
    List<Unit> findAll();
    void deleteById(Long id);
}
