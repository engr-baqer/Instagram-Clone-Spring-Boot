package devsinc.Instagram.clone.service.Interface;

import devsinc.Instagram.clone.dto.LoginRequest;
import devsinc.Instagram.clone.dto.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

    ResponseEntity<?> userRegistration(SignupRequest request);

    ResponseEntity<?> userLogin(LoginRequest request);

    void updateUserProfileImageUrl(String url, String publicId, String token);

    String getEmailVerificationCode(String email);

    String verifyVerificationCode(String code, String token);

    ResponseEntity<?> changeAccountType(String token);

    ResponseEntity<?> getSearchedUsers(String keyword);
}
