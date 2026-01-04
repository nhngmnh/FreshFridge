package hust.project.freshfridge.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Success codes
    SUCCESS("00000", "Thành công", HttpStatus.OK),
    
    // Authentication errors (001xx)
    INVALID_CREDENTIALS("00100", "Email hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("00101", "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("00102", "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("00103", "Bạn chưa đăng nhập", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("00104", "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    EMAIL_NOT_VERIFIED("00105", "Email chưa được xác thực", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED("00106", "Tài khoản đã bị khóa", HttpStatus.FORBIDDEN),
    
    // User errors (002xx)
    USER_NOT_FOUND("00200", "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("00201", "Email đã được sử dụng", HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS("00202", "Số điện thoại đã được sử dụng", HttpStatus.CONFLICT),
    INVALID_OLD_PASSWORD("00203", "Mật khẩu cũ không đúng", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH("00204", "Mật khẩu xác nhận không khớp", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_FORMAT("00205", "Mật khẩu phải có ít nhất 6 ký tự", HttpStatus.BAD_REQUEST),
    
    // Email verification errors (003xx)
    VERIFICATION_CODE_EXPIRED("00300", "Mã xác thực đã hết hạn", HttpStatus.BAD_REQUEST),
    VERIFICATION_CODE_INVALID("00301", "Mã xác thực không đúng", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_VERIFIED("00302", "Email đã được xác thực", HttpStatus.BAD_REQUEST),
    VERIFICATION_EMAIL_FAILED("00303", "Gửi email xác thực thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    
    // Group errors (004xx)
    GROUP_NOT_FOUND("00400", "Không tìm thấy nhóm", HttpStatus.NOT_FOUND),
    ALREADY_IN_GROUP("00401", "Bạn đã ở trong một nhóm", HttpStatus.CONFLICT),
    NOT_IN_GROUP("00402", "Bạn chưa tham gia nhóm nào", HttpStatus.BAD_REQUEST),
    NOT_GROUP_OWNER("00403", "Chỉ chủ nhóm mới có quyền này", HttpStatus.FORBIDDEN),
    CANNOT_REMOVE_OWNER("00404", "Không thể xóa chủ nhóm", HttpStatus.BAD_REQUEST),
    USER_NOT_IN_GROUP("00405", "Người dùng không thuộc nhóm này", HttpStatus.BAD_REQUEST),
    INVITATION_NOT_FOUND("00406", "Không tìm thấy lời mời", HttpStatus.NOT_FOUND),
    INVITATION_EXPIRED("00407", "Lời mời đã hết hạn", HttpStatus.BAD_REQUEST),
    ALREADY_INVITED("00408", "Đã gửi lời mời cho người này", HttpStatus.CONFLICT),
    
    // Food errors (005xx)
    FOOD_NOT_FOUND("00500", "Không tìm thấy thực phẩm", HttpStatus.NOT_FOUND),
    FOOD_ALREADY_EXISTS("00501", "Thực phẩm đã tồn tại trong nhóm", HttpStatus.CONFLICT),
    
    // Kitchen errors (006xx)
    KITCHEN_ITEM_NOT_FOUND("00600", "Không tìm thấy món trong bếp", HttpStatus.NOT_FOUND),
    KITCHEN_ITEM_ALREADY_EXISTS("00601", "Thực phẩm này đã có trong bếp", HttpStatus.CONFLICT),
    INSUFFICIENT_QUANTITY("00602", "Số lượng không đủ", HttpStatus.BAD_REQUEST),
    INVALID_EXPIRY_DATE("00603", "Ngày hết hạn không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // Shopping errors (007xx)
    SHOPPING_LIST_NOT_FOUND("00700", "Không tìm thấy danh sách mua sắm", HttpStatus.NOT_FOUND),
    SHOPPING_ITEM_NOT_FOUND("00701", "Không tìm thấy mục trong danh sách", HttpStatus.NOT_FOUND),
    SHOPPING_ITEM_ALREADY_EXISTS("00702", "Thực phẩm đã có trong danh sách", HttpStatus.CONFLICT),
    ITEM_ALREADY_PURCHASED("00703", "Mục này đã được mua", HttpStatus.BAD_REQUEST),
    INVALID_SHOPPING_STATUS("00704", "Trạng thái danh sách không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // Meal errors (008xx)
    MEAL_PLAN_NOT_FOUND("00800", "Không tìm thấy kế hoạch bữa ăn", HttpStatus.NOT_FOUND),
    INVALID_MEAL_DATE("00801", "Ngày kế hoạch không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // Recipe errors (009xx)
    RECIPE_NOT_FOUND("00900", "Không tìm thấy công thức", HttpStatus.NOT_FOUND),
    RECIPE_ACCESS_DENIED("00901", "Bạn không có quyền xem công thức này", HttpStatus.FORBIDDEN),
    
    // Category & Unit errors (010xx)
    CATEGORY_NOT_FOUND("01000", "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    UNIT_NOT_FOUND("01001", "Không tìm thấy đơn vị", HttpStatus.NOT_FOUND),
    
    // Notification errors (011xx)
    NOTIFICATION_NOT_FOUND("01100", "Không tìm thấy thông báo", HttpStatus.NOT_FOUND),
    
    // Validation errors (012xx)
    VALIDATION_ERROR("01200", "Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD("01201", "Thiếu trường bắt buộc", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT("01202", "Định dạng không hợp lệ", HttpStatus.BAD_REQUEST),
    
    // System errors (099xx)
    INTERNAL_ERROR("09900", "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("09901", "Dịch vụ tạm thời không khả dụng", HttpStatus.SERVICE_UNAVAILABLE),
    FILE_UPLOAD_FAILED("09902", "Tải file thất bại", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
