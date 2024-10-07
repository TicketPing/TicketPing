package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import response.CommonResponse;

@ControllerAdvice
@RestController
public class ApplicationExceptionHandler {

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

}
