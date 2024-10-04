package response;

import cases.ErrorCase;
import cases.SuccessCase;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    public static <T> CommonResponse<T> success(SuccessCase successCase, T data) {
        return CommonResponse.<T>builder()
            .status(successCase.getHttpStatus())
            .message(successCase.getMessage())
            .data(data)
            .build();
    }

    public static <T> CommonResponse<T> success(SuccessCase successCase) {
        return CommonResponse.<T>builder()
            .status(successCase.getHttpStatus())
            .message(successCase.getMessage())
            .build();
    }

    public static <T> CommonResponse<T> error(ErrorCase errorCase) {
        return CommonResponse.<T>builder()
            .status(errorCase.getHttpStatus())
            .message(errorCase.getMessage())
            .build();
    }
}
