package devsinc.Instagram.clone.service.Interface;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface StoryService {

    String addStory(MultipartFile story, String token);

    String deleteBasedOnDeleteRequest(Long storyId);

    ResponseEntity<?> viewStoryByUsername(String username, String token);
}
