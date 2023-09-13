package devsinc.Instagram.clone.service.Interface;

import devsinc.Instagram.clone.dto.CommentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    ResponseEntity<?> createNewComment(String token, CommentRequest commentRequest);

    ResponseEntity<?> deleteCommentById(Long commentId, String token);
}
