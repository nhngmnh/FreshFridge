package hust.project.freshfridge.infrastructure.persistence.repository.impl;

import hust.project.freshfridge.domain.entity.KitchenItem;
import hust.project.freshfridge.domain.repository.IKitchenItemRepository;
import hust.project.freshfridge.infrastructure.persistence.mapper.KitchenItemMapper;
import hust.project.freshfridge.infrastructure.persistence.model.KitchenItemModel;
import hust.project.freshfridge.infrastructure.persistence.repository.jpa.KitchenItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class KitchenItemRepositoryImpl implements IKitchenItemRepository {

    private final KitchenItemJpaRepository kitchenItemJpaRepository;
    private final KitchenItemMapper kitchenItemMapper;

    @Override
    public KitchenItem save(KitchenItem item) {
        KitchenItemModel model = kitchenItemMapper.toModel(item);
        KitchenItemModel savedModel = kitchenItemJpaRepository.save(model);
        return kitchenItemMapper.toDomain(savedModel);
    }

    @Override
    public Optional<KitchenItem> findById(Long id) {
        return kitchenItemJpaRepository.findById(id)
                .map(kitchenItemMapper::toDomain);
    }

    @Override
    public Optional<KitchenItem> findByFoodIdAndGroupId(Long foodId, Long groupId) {
        return kitchenItemJpaRepository.findByFoodIdAndGroupId(foodId, groupId)
                .map(kitchenItemMapper::toDomain);
    }

    @Override
    public List<KitchenItem> findByGroupId(Long groupId) {
        return kitchenItemJpaRepository.findByGroupId(groupId).stream()
                .map(kitchenItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<KitchenItem> findAll(Long groupId, Long foodId, String foodName, LocalDate expiryBefore,
                                     Long categoryId, String sortBy, String sortOrder) {
        List<KitchenItemModel> results;
        
        // Default sort order
        if (sortOrder == null) {
            sortOrder = "asc";
        }
        
        // Handle different sort combinations
        if ("name".equalsIgnoreCase(sortBy)) {
            if ("desc".equalsIgnoreCase(sortOrder)) {
                results = kitchenItemJpaRepository.findAllWithFiltersSortByNameDesc(
                        groupId, foodId, foodName, expiryBefore, categoryId);
            } else {
                results = kitchenItemJpaRepository.findAllWithFiltersSortByNameAsc(
                        groupId, foodId, foodName, expiryBefore, categoryId);
            }
        } else if ("createdAt".equalsIgnoreCase(sortBy)) {
            if ("desc".equalsIgnoreCase(sortOrder)) {
                results = kitchenItemJpaRepository.findAllWithFiltersSortByCreatedAtDesc(
                        groupId, foodId, foodName, expiryBefore, categoryId);
            } else {
                results = kitchenItemJpaRepository.findAllWithFiltersSortByCreatedAtAsc(
                        groupId, foodId, foodName, expiryBefore, categoryId);
            }
        } else if ("expiryDate".equalsIgnoreCase(sortBy) && "desc".equalsIgnoreCase(sortOrder)) {
            results = kitchenItemJpaRepository.findAllWithFiltersSortByExpiryDesc(
                    groupId, foodId, foodName, expiryBefore, categoryId);
        } else {
            // Default: sort by expiry date ASC
            results = kitchenItemJpaRepository.findAllWithFilters(
                    groupId, foodId, foodName, expiryBefore, categoryId);
        }
        
        return results.stream()
                .map(kitchenItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<KitchenItem> findExpiringItems(Long groupId, LocalDate thresholdDate) {
        return kitchenItemJpaRepository.findExpiringItems(groupId, thresholdDate).stream()
                .map(kitchenItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByFoodIdAndGroupId(Long foodId, Long groupId) {
        return kitchenItemJpaRepository.existsByFoodIdAndGroupId(foodId, groupId);
    }

    @Override
    public void deleteById(Long id) {
        kitchenItemJpaRepository.deleteById(id);
    }
}
