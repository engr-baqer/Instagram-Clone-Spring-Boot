package devsinc.Instagram.clone.service.InterfaceImpls;

import com.cloudinary.Cloudinary;
import devsinc.Instagram.clone.dto.AuthenticationResponse;
import devsinc.Instagram.clone.dto.LoginRequest;
import devsinc.Instagram.clone.dto.SignupRequest;
import devsinc.Instagram.clone.enums.AccountStatus;
import devsinc.Instagram.clone.enums.AccountType;
import devsinc.Instagram.clone.external.EmailVerification.EmailVerificationService;
import devsinc.Instagram.clone.external.EmailVerification.VerificationCode;
import devsinc.Instagram.clone.model.User;
import devsinc.Instagram.clone.repository.UserRepository;
import devsinc.Instagram.clone.security.JwtService;
import devsinc.Instagram.clone.service.Interface.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

//    dependency injection
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final Cloudinary cloudinary;



    /**
     * Checks if a user with the given username already exists in the user repository.
     *
     * @param username The username to check for existence.
     * @return True if a user with the provided username exists; otherwise, false.
     */
    private boolean isExistByUsername(String username)
    {
        System.out.println(userRepository.existsByUsername(username));
        return userRepository.existsByUsername(username.trim());
    }



    /**
     * Checks if a user with the given phone number already exists in the user repository.
     *
     * @param email The email address to check for existence.
     * @return True if a user with the provided phone number exists; otherwise, false.
     */
    private boolean isExistByEmail(String email)
    {
        return userRepository.existsByEmail(email.trim());
    }


    private Long authenticateUser(String token) {
        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent() && username.equals(user.get().getUsername())) {
            return user.get().getUserId();
        }
        return null;
    }



    /**
     * Registers a new user based on the provided signup request.
     * <p>
     * This method checks if the provided username or phone number already exists in the user repository.
     * If either exists, it returns an error message. Otherwise, it creates a new user with the provided
     * details, saves the user to the repository, generates an authentication token, and returns a success
     * message along with the generated token.
     *
     * @param request The SignupRequest containing user registration details.
     * @return A response message indicating the result of the registration process:
     *         - "This username is already exists!" if the username is already in use.
     *         - "This phone is already exists!" if the phone number is already associated with an account.
     *         - "User has been saved successfully!\nToken: Bearer [generated token]" on successful registration.
     */
    @Override
    public ResponseEntity<?> userRegistration(SignupRequest request) {
        if (isExistByUsername(request.getUsername())){
            return ResponseEntity.unprocessableEntity().body("This username is already exists!");
        }
        else if(isExistByEmail(request.getEmail())){
            return ResponseEntity.unprocessableEntity().body("This phone is already exists!");
        }
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM, yyyy");
            User user = User.builder()
                    .email(request.getEmail().trim())
                    .fullName(request.getFullName().trim())
                    .username(request.getUsername().trim())
                    .password(passwordEncoder.encode(request.getPassword()).trim())
                    .accountStatus(AccountStatus.PENDING)
                    .createdAt(dateFormat.format(new Date()))
                    .accountType(AccountType.PUBLIC)
                    .build();
            userRepository.save(user);
            return ResponseEntity.status(200).body(AuthenticationResponse.builder()
                    .token(jwtService.generateToken(user))
                    .build());
        }
    }

    @Override
    public ResponseEntity<?> userLogin(LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isPresent() && BCrypt.checkpw(request.getPassword(),user.get().getPassword()))
            return ResponseEntity.status(200).body(AuthenticationResponse.builder()
                    .token(jwtService.generateToken(user.get()))
                    .build());
        else {
            return ResponseEntity.unprocessableEntity().body("Invalid login credentials!");
        }
    }


    @Override
    public void updateUserProfileImageUrl(String url, String publicId, String token) {
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if(user.isPresent())
        {
            if (user.get().getImageUrl().equals("")) {
                user.get().setImageUrl(url);
                user.get().setPublicId(publicId);
                userRepository.save(user.get());
            } else {
                try {
                    cloudinary.uploader().destroy(user.get().getPublicId(),
                            Map.of());
                    user.get().setImageUrl(url);
                    user.get().setPublicId(publicId);
                    userRepository.save(user.get());
                } catch (IOException e) {
                    throw new RuntimeException("Image is not existing on cloud!");
                }
            }

        }
    }

    @Override
    public String getEmailVerificationCode(String email) {
        int generatedCode = 0;
        if(isExistByEmail(email)) {
            generatedCode = emailVerificationService.generateVerificationCode();
            Optional<User> user = userRepository.findByEmail(email);
            try{
                emailVerificationService.sendEmailCode(
                        emailVerificationService.createMessage(
                                user.get().getFullName(), email, generatedCode
                        ));
            } catch (MessagingException exception) {
                System.err.println("An error occurred while sending the email: " + exception.getMessage());
            }

            user.get().setVerificationCode(String.valueOf(generatedCode));
            userRepository.save(user.get());
        }
        if(!String.valueOf(generatedCode).equals("")) {
            return String.valueOf(generatedCode);
        } else {
            return null;
        }
    }

    @Override
    public String verifyVerificationCode(String code, String token) {
        String username = jwtService.extractUsername(token);
        if (!username.equals("")) {
            Optional<User> user = userRepository.findByUsername(username);

            if(!user.get().getAccountStatus().toString().equals("VERIFIED")) {
                int dbCode = Integer.parseInt(user.get().getVerificationCode());
                int paramCode = Integer.parseInt(code);
                if (emailVerificationService.isCodeCorrect(
                        VerificationCode.builder()
                                .digit1(String.valueOf((dbCode / 1000) % 10))
                                .digit2(String.valueOf((dbCode / 100) % 10))
                                .digit3(String.valueOf((dbCode / 10) % 10))
                                .digit4(String.valueOf(dbCode % 10))
                                .build()
                        ,
                        VerificationCode.builder()
                                .digit1(String.valueOf((paramCode / 1000) % 10))
                                .digit2(String.valueOf((paramCode / 100) % 10))
                                .digit3(String.valueOf((paramCode / 10) % 10))
                                .digit4(String.valueOf(paramCode % 10))
                                .build()
                )) {
                    user.get().setAccountStatus(AccountStatus.VERIFIED);
                    userRepository.save(user.get());
                    return "VERIFIED!";
                } else {
                    return "Invalid verification code!";
                }
            } else {
                return "You are already VERIFIED!";
            }
        }
        return "Invalid authentication creds!";
    }

    @Transactional
    @Override
    public ResponseEntity<?> changeAccountType(String token) {
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if(user.isPresent()) {
            if(user.get().getAccountType() == null) {
                user.get().setAccountType(AccountType.PUBLIC);
                userRepository.save(user.get());
                return ResponseEntity.status(HttpStatus.OK).body("Your account type is PUBLIC!");
            } else if(user.get().getAccountType() == AccountType.PUBLIC) {
                user.get().setAccountType(AccountType.PRIVATE);
                userRepository.save(user.get());
                return ResponseEntity.status(HttpStatus.OK).body("Account type has been changed from PUBLIC to PRIVATE!");
            } else if(user.get().getAccountType() == AccountType.PRIVATE){
                user.get().setAccountType(AccountType.PUBLIC);
                userRepository.save(user.get());
                return ResponseEntity.status(HttpStatus.OK).body("Account type has been changed from PRIVATE to PUBLIC!");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Database contains an unknown value!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not valid!");
    }

    @Override
    public ResponseEntity<?> getSearchedUsers(String keyword) {
        List<String> listOfUsers = userRepository.findByUsernameContaining(keyword).stream().map(User::getUsername).toList();
        return ResponseEntity.status(HttpStatus.OK).body(listOfUsers);
    }
}
