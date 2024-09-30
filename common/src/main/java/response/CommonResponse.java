package response;

import com.fasterxml.jackson.annotation.JsonInclude;
import exception.ErrorCase;
import success.SuccessCase;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(SuccessCase code, T data) {
        return CommonResponse.<T>builder()
            .status(code.getHttpStatus())
            .message(code.getMessage())
            .data(data)
            .build();
    }

    public static <T> CommonResponse<T> success(SuccessCase code) {
        return CommonResponse.<T>builder()
            .status(code.getHttpStatus())
            .message(code.getMessage())
            .build();
    }

    public static <T> CommonResponse<T> error(ErrorCase errorResponse) {
        return CommonResponse.<T>builder()
            .status(errorResponse.getHttpStatus())
            .message(errorResponse.getMessage())
            .build();
    }
}
