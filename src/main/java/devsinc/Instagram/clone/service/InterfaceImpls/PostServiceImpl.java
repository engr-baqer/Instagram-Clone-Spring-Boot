package devsinc.Instagram.clone.service.InterfaceImpls;

import com.cloudinary.Cloudinary;
import devsinc.Instagram.clone.dto.ViewAllPostImagesResponse;
import devsinc.Instagram.clone.dto.ViewAllPostResponse;
import devsinc.Instagram.clone.enums.AccountType;
import devsinc.Instagram.clone.model.*;
import devsinc.Instagram.clone.repository.LikeRepository;
import devsinc.Instagram.clone.repository.PostImageRepository;
import devsinc.Instagram.clone.repository.PostRepository;
import devsinc.Instagram.clone.repository.UserRepository;
import devsinc.Instagram.clone.security.JwtService;
import devsinc.Instagram.clone.service.Interface.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final Cloudinary cloudinary;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final LikeRepository likeRepository;

    @Transactional
    @Override
    public String createPost(List<MultipartFile> posts, String description, String token) {
        if(posts.size()>=1 && posts.size()<= 10) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM, yyyy");
            Post userPost = Post.builder()
                    .user(userRepository.findByUsername(jwtService.extractUsername(token)).get())
                    .postDescription(description)
                    .creationDate(dateFormat.format(new Date()))
                    .build();
            
            Set<PostImage> imagesSet = posts.stream().map(post -> mapToPostImage(post,userPost)).collect(Collectors.toSet());
            userPost.setPostImages(imagesSet);
            postRepository.save(userPost);
            return "Post has been uploaded successfully!";
        }
        return "A single post contains 1 to 10 images. Maximum post size will be 50MB!";
    }


    @Override
    public String deletePostByPostId(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if(postOptional.isPresent()){
            Post post = postOptional.get();
            Set<PostImage> imagesOfPost = postImageRepository.findByPost(post);
            postRepository.delete(post);
            imagesOfPost.forEach(image-> {
                try {
                    cloudinary.uploader().destroy(image.getPublicId(),
                            Map.of());
                } catch (IOException e) {
                    throw new RuntimeException("Image is not existing on cloud!");
                }
            });
            return "Post has been deleted successfully!";
        }
        return "There is no post available with this id!";
    }

    @Transactional
    @Override
    public ResponseEntity<?> getMyAllPosts(String token) {
        Optional<User> userOptional = userRepository.findByUsername(jwtService.extractUsername(token));
        if(userOptional.isPresent())
        {
            User user = userOptional.get();
            Set<Post> posts = postRepository.findByUser(user);
            Set<ViewAllPostResponse> responses = posts.stream().map(this::mapToViewAllPostResponse).collect(Collectors.toSet());
            if (responses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("You have not created any posts yet!");
            }
            return ResponseEntity.ok().body(responses);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user creds! (Token is not valid)");
    }

    @Override
    public ResponseEntity<?> viewAllPublicPosts(String token) {
        Optional<User> userOptional = userRepository.findByUsername(jwtService.extractUsername(token));
        if(userOptional.isPresent())
        {
            Set<User> followings = userOptional.get().getFollowings().stream()
                    .map(Follow::getFollower).collect(Collectors.toSet());
            Set<User> userSet = userRepository.findByAccountType(AccountType.PUBLIC);
            userSet.addAll(followings);
            Set<ViewAllPostResponse> postSet = userSet.stream()
                    .flatMap(user -> postRepository.findByUser(user)
                            .stream()
                            .map(this::mapToViewAllPostResponse)
                    )
                    .collect(Collectors.toSet());
            if(postSet.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("There is no public posts exists yet!");
            }
            return ResponseEntity.status(HttpStatus.OK).body(postSet);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user creds! (Token is not valid)");
    }

    @Override
    public ResponseEntity<?> likeThePost(Long postId, String token) {
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        Optional<Post> post = postRepository.findById(postId);

        if(user.isPresent() && post.isPresent()) {
            if(!likeRepository.existsByUserAndPost(user.get(),post.get())) {
                Like like =  Like
                        .builder()
                        .creationTime(LocalDateTime.now())
                        .post(post.get())
                        .user(user.get())
                        .build();
                likeRepository.save(like);
                return ResponseEntity.status(HttpStatus.OK).body("You have like the post "+postId+"!");
            }
            return ResponseEntity.status(HttpStatus.IM_USED).body("Already liked!");
        } else if(post.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid post id!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
    }


    /**************** Helper functions ***************/

    private PostImage mapToPostImage(MultipartFile post, Post userPost) {
        Map<String, Object> data;
        PostImage postImage = null;
        try {
            data = cloudinary.uploader().upload(post.getBytes(), Map.of("folder", "Post Images"));
            System.out.println(data);
            postImage = PostImage.builder()
                    .imageUrl(data.get("url").toString())
                    .publicId(data.get("public_id").toString())
                    .post(userPost)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return postImage;
    }

    private ViewAllPostResponse mapToViewAllPostResponse(Post post) {
        return ViewAllPostResponse.builder()
                .postId(post.getPostId())
                .postDescription(post.getPostDescription())
                .creationDate(post.getCreationDate())
                .userId(post.getUser().getUserId())
                .postImages(post.getPostImages().stream().map(this::mapToViewAllPostImagesResponse).collect(Collectors.toSet()))
                .build();
    }

    private ViewAllPostImagesResponse mapToViewAllPostImagesResponse(PostImage image) {
        return ViewAllPostImagesResponse.builder()
                .imageId(image.getImageId())
                .postId(image.getPost().getPostId())
                .imageUrl(image.getImageUrl())
                .publicId(image.getPublicId())
                .build();
    }
}
