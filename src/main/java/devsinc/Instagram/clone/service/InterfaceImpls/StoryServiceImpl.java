package devsinc.Instagram.clone.service.InterfaceImpls;

import com.cloudinary.Cloudinary;
import devsinc.Instagram.clone.dto.ViewStoryResponse;
import devsinc.Instagram.clone.model.Follow;
import devsinc.Instagram.clone.model.Stories;
import devsinc.Instagram.clone.model.User;
import devsinc.Instagram.clone.repository.FollowRepository;
import devsinc.Instagram.clone.repository.StoriesRepository;
import devsinc.Instagram.clone.repository.UserRepository;
import devsinc.Instagram.clone.security.JwtService;
import devsinc.Instagram.clone.service.Interface.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final StoriesRepository storyRepository;
    private final FollowRepository followRepository;


    @Override
    public String addStory(MultipartFile story, String token) {
        Map<String, Object> data;
        try {
            Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
            if (user.isPresent()) {
                data = cloudinary.uploader().upload(story.getBytes(), Map.of("folder", "Stories"));
                Stories createStory = Stories.builder()
                        .imageUrl(data.get("url").toString())
                        .publicId(data.get("public_id").toString())
                        .user(user.get())
                        .creationTime(LocalDateTime.now())
                        .build();
                storyRepository.save(createStory);
                return user.get().getFullName() + "!\nYour story has been added successfully!";
            } else {
                return "Invalid user credentials!";
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image!", e);
        }
    }

    @Transactional
    public String deleteBasedOnDeleteRequest(Long storyId) {
        Optional<Stories> story = storyRepository.findById(storyId);
        if (story.isPresent()) {
            try {
                cloudinary.uploader().destroy(story.get().getPublicId(),
                        Map.of());
                storyRepository.delete(story.get());
                return "Story has been deleted successfully!";
            } catch (IOException e) {
                throw new RuntimeException("Image is not existing on cloud!");
            }
        }
        return "Invalid story id!";
    }

    @Override
    public ResponseEntity<?> viewStoryByUsername(String username, String token) {
        Optional<User> targetUser = userRepository.findByUsername(username);
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if(targetUser.isPresent() && user.isPresent()) {
            if(targetUser.get() == user.get()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can request to watch your stories by yourself!");
            }
            Follow follow = followRepository.findByFollowerAndFollowing(targetUser.get(),user.get());
            if(follow != null) {
                List<ViewStoryResponse> storiesList = storyRepository.findByUser(targetUser.get())
                        .stream()
                        .map(this::mapToStoryViewResponse).toList();
                if(!storiesList.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.OK).body(storiesList);
                }
                return ResponseEntity.status(HttpStatus.OK).body(username+" doesn't have any stories!");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Follow "+username+", to watch his stories!");
        } else  if(targetUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found against this \""+username+"\" username!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
    }

    private ViewStoryResponse mapToStoryViewResponse(Stories story) {
        return ViewStoryResponse
                .builder()
                .storyId(story.getStoryId())
                .storyUrl(story.getImageUrl())
                .creationTime(story.getCreationTime())
                .build();
    }
}
