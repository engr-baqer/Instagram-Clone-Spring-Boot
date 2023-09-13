package devsinc.Instagram.clone.service.Interface;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface PostService {
    String createPost(List<MultipartFile> posts, String description, String token);

    String deletePostByPostId(Long postId);

    ResponseEntity<?> getMyAllPosts(String token);

    ResponseEntity<?> viewAllPublicPosts(String token);

    ResponseEntity<?> likeThePost(Long postId, String token);
}
