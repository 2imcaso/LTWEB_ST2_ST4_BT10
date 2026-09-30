package vn.iotstar.exceptions;

import com.nimbusds.jose.JOSEException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.text.ParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail detail;
        if (exception instanceof BadCredentialsException) detail = problem(401, "Thong tin dang nhap khong hop le", exception);
        else if (exception instanceof AccountStatusException) detail = problem(403, "Tai khoan bi khoa", exception);
        else if (exception instanceof AccessDeniedException) detail = problem(403, "Khong duoc phep truy cap tai nguyen", exception);
        else if (exception instanceof JOSEException || exception instanceof ParseException || exception instanceof IllegalArgumentException)
            detail = problem(401, "JWT khong hop le hoac da het han", exception);
        else detail = problem(500, "Loi he thong", exception);
        return detail;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validation(MethodArgumentNotValidException ex) { return problem(400, "Du lieu khong hop le", ex); }
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail responseStatus(ResponseStatusException ex) { return problem(ex.getStatusCode().value(), ex.getReason(), ex); }
    private ProblemDetail problem(int status, String description, Exception ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(status), ex.getMessage());
        detail.setTitle(HttpStatus.valueOf(status).getReasonPhrase()); detail.setType(URI.create("about:blank"));
        detail.setProperty("description", description); return detail;
    }
}
