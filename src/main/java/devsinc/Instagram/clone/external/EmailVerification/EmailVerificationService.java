package devsinc.Instagram.clone.external.EmailVerification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final JavaMailSender sender;

    @Async
    public void sendEmailCode(MimeMessage message) {
        sender.send(message);
    }

    public int generateVerificationCode() {
        int otpLength = 4; // Length of the OTP
        int min = (int) Math.pow(10, otpLength - 1);
        int max = (int) Math.pow(10, otpLength) - 1;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public boolean isCodeCorrect(VerificationCode code1, VerificationCode code2)
    {
        return code1.getDigit1().equals(code2.getDigit1()) &&
                code1.getDigit2().equals(code2.getDigit2()) &&
                code1.getDigit3().equals(code2.getDigit3()) &&
                code1.getDigit4().equals(code2.getDigit4());
    }
    public MimeMessage createMessage(String username,String email, int code) throws MessagingException {
        String htmlMessage = "<html><body style='font-family: Arial, sans-serif; font-size:16px;'>"
                + "<p>Hello <strong>" + username + "</strong>,</p>"
                + "<p>Welcome to <strong>Instagram</strong>!<br>Here is your verification code: <strong>" + code + "</strong></p>"
                + "<p>Thank you for using our service.</p>"
                + "</body></html>";

        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(email);
        helper.setSubject("Instagram-Email verification code");
        helper.setFrom("BlogVista");
        helper.setText(htmlMessage, true);
        return mimeMessage;
    }
}
