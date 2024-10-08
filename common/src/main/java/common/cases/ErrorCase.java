package common.cases;

import org.springframework.http.HttpStatus;

public interface ErrorCase {
    HttpStatus getHttpStatus();
    String getMessage();
}

