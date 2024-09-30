package exception;

import org.springframework.http.ResponseEntity;
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

}
