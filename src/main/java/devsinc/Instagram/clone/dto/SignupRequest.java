package devsinc.Instagram.clone.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SignupRequest {

    @NotNull(message = "Please enter the phone number")
    @NotEmpty(message = "Phone number mustn't be empty")
//    @Pattern(regexp = "^(0)?(3)([0-9]{9})$", message = "Phone number is not valid")
    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", message = "Invalid email address!")
    private String email;


    @NotNull(message = "Please enter your full name")
    @NotEmpty(message = "Name mustn't be empty")
    @Size(min = 3, max = 20,message = "Length of full name must between 3 to 20 characters")
    private String fullName;


    @NotNull(message = "Please enter your username")
    @NotEmpty(message = "Username mustn't be empty")
    @Size(min = 5, max = 15,message = "Length must username between 5 to 15 characters")
    private String username;

    @NotNull(message = "Please enter your password")
    @NotEmpty(message = "Password mustn't be empty")
    @Size(min = 6, max = 12,message = "Length of password must between 6 to 12 characters")
    private String password;

}
