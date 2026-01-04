package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitListResponse {
    private List<UnitResponse> units;
    private int total;
}
