package devsinc.Instagram.clone.controller;

import devsinc.Instagram.clone.dto.CommentRequest;
import devsinc.Instagram.clone.service.Interface.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Comment API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(HttpServletRequest request,
                                           @Valid @RequestBody CommentRequest commentRequest,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> validationErrors = bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body("Validation errors: \n" + String.join(",\n", validationErrors));
        }
        return commentService.createNewComment(request.getHeader("Authorization").substring(7),
                commentRequest);
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public ResponseEntity<?> deleteAnyCommentOnHisOwnPost(@PathVariable Long commentId, HttpServletRequest request) {
        return commentService.deleteCommentById(commentId, request.getHeader("Authorization").substring(7));
    }
}
