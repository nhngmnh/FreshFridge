package hust.project.freshfridge.application.usecase;

import hust.project.freshfridge.application.dto.response.*;
import hust.project.freshfridge.domain.entity.Unit;
import hust.project.freshfridge.domain.repository.IUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnitUseCase {

    private final IUnitRepository unitRepository;

    public UnitListResponse getAllUnits() {
        List<Unit> units = unitRepository.findAll();
        
        List<UnitResponse> responses = units.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return UnitListResponse.builder()
                .units(responses)
                .total(responses.size())
                .build();
    }

    private UnitResponse toResponse(Unit unit) {
        return UnitResponse.builder()
                .id(unit.getId())
                .name(unit.getName())
                .abbreviation(unit.getAbbreviation())
                .build();
    }
}
