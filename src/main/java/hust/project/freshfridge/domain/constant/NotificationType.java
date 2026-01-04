package hust.project.freshfridge.domain.constant;

public enum NotificationType {
    EXPIRY_WARNING,         // Cảnh báo thực phẩm sắp hết hạn
    EXPIRY_ALERT,           // Thực phẩm đã hết hạn
    SHOPPING_REMINDER,      // Nhắc mua sắm
    GROUP_INVITE,           // Mời vào nhóm
    GROUP_JOIN,             // Thành viên mới tham gia
    GROUP_LEAVE,            // Thành viên rời nhóm
    MEMBER_REMOVED,         // Bị xóa khỏi nhóm
    MEAL_REMINDER,          // Nhắc bữa ăn
    SYSTEM                  // Thông báo hệ thống
}
