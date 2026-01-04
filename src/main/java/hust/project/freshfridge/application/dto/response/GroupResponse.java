package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {
    private Long id;
    private String name;
    private Long adminId;
    private String adminName;
    private List<MemberResponse> members;
    private LocalDateTime createdAt;
}
