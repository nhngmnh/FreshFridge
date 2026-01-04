package hust.project.freshfridge.application.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserListResponse {
    private List<AdminUserResponse> users;
    private int total;
    private int activeCount;
    private int inactiveCount;
    private int verifiedCount;
    private int unverifiedCount;
}
