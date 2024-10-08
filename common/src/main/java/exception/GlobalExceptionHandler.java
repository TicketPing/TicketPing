package exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import response.CommonResponse;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<CommonResponse> handleDnaApplicationException(ApplicationException e) {
        CommonResponse response = CommonResponse.error(e.getExceptionCase());
        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleValidException(BindingResult bindingResult) {
        String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        CommonResponse response = CommonResponse.error(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity handleFeignException(FeignException feignException) throws JsonProcessingException {
        String responseJson = feignException.contentUTF8();
        Map<String, String> responseMap = objectMapper.readValue(responseJson, Map.class);
        return ResponseEntity
                .status(feignException.status())
                .body(responseMap);
    }

}
