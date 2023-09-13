package devsinc.Instagram.clone.controller;

import devsinc.Instagram.clone.service.Interface.StoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Story API")
public class StoryController {
    private final StoryService storyService;
    @PostMapping("/story/addStory")
    public ResponseEntity<?> addStory(@RequestParam("story") MultipartFile story,
                                      HttpServletRequest request){
        return ResponseEntity.ok().body(storyService.addStory(story,
                request.getHeader("Authorization").substring(7)));
    }

    @DeleteMapping("/story/delete/{storyId}")
    public ResponseEntity<?> deleteStory(@PathVariable Long storyId) {
        return ResponseEntity.ok().body(storyService.deleteBasedOnDeleteRequest(storyId));
    }

    @GetMapping("/story/view")
    public ResponseEntity<?> viewStoryOfAUser(@RequestParam String username, HttpServletRequest request) {
        return storyService.viewStoryByUsername(username, request.getHeader("Authorization").substring(7));
    }
}
