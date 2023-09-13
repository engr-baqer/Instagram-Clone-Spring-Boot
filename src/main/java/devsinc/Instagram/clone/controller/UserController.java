package devsinc.Instagram.clone.controller;

import devsinc.Instagram.clone.dto.LoginRequest;
import devsinc.Instagram.clone.dto.SignupRequest;
import devsinc.Instagram.clone.service.Interface.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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

/**
 * Controller class for managing user-related endpoints, such as user login and signup.
 * <p>
 * This Controller class contains the endpoints related to user login, signup, and other user-related operations.
 * It handles incoming HTTP requests and delegates the processing to the UserService.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User API")
public class UserController {
    private final UserService userService;
    @SecurityRequirements
    @PostMapping("/signup")
    public ResponseEntity<?> userSignupRequest(
            @Valid @RequestBody SignupRequest request, BindingResult bindingResult
    )
    {
        if (bindingResult.hasErrors()) {
            List<String> validationErrors = bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body("Validation errors: \n" + String.join(",\n", validationErrors));
        }
        return userService.userRegistration(request);
    }

    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<?> userLoginRequest(@Valid @RequestBody LoginRequest request, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            List<String> validationErrors = bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body("Validation errors: \n" + String.join(",\n", validationErrors));
        }
        return userService.userLogin(request);
    }


    @GetMapping("/getEmailVerificationCode")
    public ResponseEntity<?> getEmailVerificationCode(@RequestParam String email) {
        return ResponseEntity.ok(userService.getEmailVerificationCode(email));
    }

    @GetMapping("/verifyEmailVerificationCode")
    public ResponseEntity<?> verifyVerificationCode(@RequestParam String code, HttpServletRequest request) {
        return ResponseEntity.ok(userService.verifyVerificationCode(code,
                request.getHeader("Authorization").substring(7)));
    }

    @GetMapping("/changeAccountType")
    public ResponseEntity<?> changeAccountType(HttpServletRequest request) {
        return userService.changeAccountType(request.getHeader("Authorization").substring(7));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> getListOfUsers(@PathVariable String keyword) {
        return userService.getSearchedUsers(keyword);
    }

}
