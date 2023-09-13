package devsinc.Instagram.clone.service.Interface;

import devsinc.Instagram.clone.dto.FollowRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface FollowService {
    ResponseEntity<?> followUserByFollowRequest(FollowRequest request);

    ResponseEntity<?> getListOfMyFollowers(String token);

    ResponseEntity<?> getListOfMyFollowings(String token);

    ResponseEntity<?> getListOfMyFollowRequests(String token);

    ResponseEntity<?> getListOfMyFollowingRequests(String token);

    ResponseEntity<?> acceptFollowRequest(String token, String username);
}
