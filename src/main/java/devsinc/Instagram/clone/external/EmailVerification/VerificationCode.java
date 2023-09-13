package devsinc.Instagram.clone.external.EmailVerification;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class VerificationCode {
    private String digit1;
    private String digit2;
    private String digit3;
    private String digit4;
}