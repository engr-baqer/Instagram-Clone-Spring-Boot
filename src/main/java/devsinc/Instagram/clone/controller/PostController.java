package devsinc.Instagram.clone.controller;

import devsinc.Instagram.clone.service.Interface.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Post API")
public class PostController {
    private final PostService postService;


    @GetMapping("/posts/myPosts")
    public ResponseEntity<?> getMyAllPosts(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            ResponseEntity<?> response = postService.getMyAllPosts(token);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok().body(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Authorization header");
        }
    }

    /* WORK IN PROGRESS */

    @GetMapping("/posts/publicPosts")
    public ResponseEntity<?> viewAllPublicPosts(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            ResponseEntity<?> response = postService.viewAllPublicPosts(token);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok().body(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Authorization header");
        }
    }


    @PostMapping(value = "/posts/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestParam("posts") List<MultipartFile> posts,
                                        @RequestParam("description") String description,
                                        HttpServletRequest request){
        return ResponseEntity.ok(postService.createPost(posts, description,request.getHeader("Authorization").substring(7)));
    }
    @PostMapping("/posts/edit/{postId}")
    public void editPost(@PathVariable Long postId){
        // This endpoint will edit this posts
    }
    @DeleteMapping("/posts/delete/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return ResponseEntity.accepted().body(postService.deletePostByPostId(postId));
    }

    @GetMapping("/posts/like/{postId}")
    public ResponseEntity<?> likeThePost(@PathVariable Long postId, HttpServletRequest request) {
        return postService.likeThePost(postId,request.getHeader("Authorization").substring(7));
    }
}
