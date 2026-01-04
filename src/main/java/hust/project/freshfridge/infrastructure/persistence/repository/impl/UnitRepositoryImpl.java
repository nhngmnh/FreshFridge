package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.Unit;
import hust.project.freshfridge.domain.repository.IUnitRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.UnitMapper;
import hust.project.freshfridge.infrastructure.persistence.model.UnitModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.UnitJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UnitRepositoryImpl implements IUnitRepository {

    private final UnitJpaRepository unitJpaRepository;
    private final UnitMapper unitMapper;

    @Override
    public Unit save(Unit unit) {
        UnitModel model = unitMapper.toModel(unit);
        UnitModel savedModel = unitJpaRepository.save(model);
        return unitMapper.toDomain(savedModel);
    }

    @Override
    public Optional<Unit> findById(Long id) {
        return unitJpaRepository.findById(id)
                .map(unitMapper::toDomain);
    }

    @Override
    public Optional<Unit> findByName(String name) {
        return unitJpaRepository.findByName(name)
                .map(unitMapper::toDomain);
    }

    @Override
    public List<Unit> findAll() {
        return unitJpaRepository.findAll().stream()
                .map(unitMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        unitJpaRepository.deleteById(id);
    }
}
