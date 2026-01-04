package hust.project.freshfridge.application.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitResponse {
    private Long id;
    private String name;
    private String abbreviation;
}
