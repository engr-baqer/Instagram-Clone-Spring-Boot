package devsinc.Instagram.clone.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {

    @NotNull(message = "Please enter your username")
    @NotEmpty(message = "Username mustn't be empty")
    @Size(min = 5, max = 15,message = "Length must username between 5 to 15 characters")
    private String username;


    @NotNull(message = "Please enter your password")
    @NotEmpty(message = "Password mustn't be empty")
    @Size(min = 6, max = 12,message = "Length of password must between 6 to 12 characters")
    private String password;
}
