package devsinc.Instagram.clone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        String errorMessage = "Files size exceeds the maximum limit. Max limit is 50MB!";
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorMessage);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handUsernameNotFoundException(UsernameNotFoundException ex) {
        String errorMessage = "Invalid user!";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
