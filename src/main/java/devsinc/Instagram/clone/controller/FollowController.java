package devsinc.Instagram.clone.controller;

import devsinc.Instagram.clone.dto.FollowRequest;
import devsinc.Instagram.clone.service.Interface.FollowService;
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
@Tag(name = "Follow API")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<?>  followUser(@Valid @RequestBody FollowRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> validationErrors = bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body("Validation errors: \n" + String.join(",\n", validationErrors));
        }
        return followService.followUserByFollowRequest(request);
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(HttpServletRequest request) {
        return followService.getListOfMyFollowers(request.getHeader("Authorization").substring(7));
    }

    @GetMapping("/followings")
    public ResponseEntity<?> getFollowings(HttpServletRequest request) {
        return followService.getListOfMyFollowings(request.getHeader("Authorization").substring(7));
    }

    @GetMapping("/followRequests")
    public ResponseEntity<?> getFollowRequests(HttpServletRequest request) {
        return followService.getListOfMyFollowRequests(request.getHeader("Authorization").substring(7));
    }
    @GetMapping("/followingRequests")
    public ResponseEntity<?> getFollowingRequests(HttpServletRequest request) {
        return followService.getListOfMyFollowingRequests(request.getHeader("Authorization").substring(7));
    }

    @GetMapping("/acceptFollowRequest")
    public ResponseEntity<?> acceptFollowRequest(HttpServletRequest request, @RequestParam String username) {
        return followService.acceptFollowRequest(request.getHeader("Authorization").substring(7),
                username);
    }
}
