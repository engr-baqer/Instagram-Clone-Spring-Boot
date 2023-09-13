package devsinc.Instagram.clone.service.InterfaceImpls;

import devsinc.Instagram.clone.dto.CommentRequest;
import devsinc.Instagram.clone.model.Comment;
import devsinc.Instagram.clone.model.Post;
import devsinc.Instagram.clone.model.User;
import devsinc.Instagram.clone.repository.CommentRepository;
import devsinc.Instagram.clone.repository.PostRepository;
import devsinc.Instagram.clone.repository.UserRepository;
import devsinc.Instagram.clone.security.JwtService;
import devsinc.Instagram.clone.service.Interface.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public ResponseEntity<?> createNewComment(String token, CommentRequest request) {
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        Optional<Post> post = postRepository.findById(request.getPostId());

        if(user.isPresent() && post.isPresent()) {
            Comment comment = Comment
                    .builder()
                    .commentText(request.getCommentText())
                    .creationTime(LocalDateTime.now())
                    .user(user.get())
                    .post(post.get())
                    .build();
            commentRepository.save(comment);
            return ResponseEntity.status(HttpStatus.OK).body("Comment done!");
        }
        else if(post.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post id is not valid");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
    }

    @Override
    public ResponseEntity<?> deleteCommentById(Long commentId, String token) {
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(user.isPresent() && comment.isPresent()) {
            if(comment.get().getUser() == user.get()) {
                commentRepository.delete(comment.get());
                return ResponseEntity.status(HttpStatus.OK).body("Comment has been deleted successfully!");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You're not allowed to delete this comment!");
        } else if(comment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid comment id!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
    }
}
