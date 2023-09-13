package devsinc.Instagram.clone.external.CloudinaryConfigs;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> configuration = new HashMap<>();
        configuration.put("cloud_name", "dmdptfqhx");
        configuration.put("api_key", "612666723699221");
        configuration.put("api_secret", "E4l-b0_6fjJMr3OZawPiLsVpY0g");
        configuration.put("secure", true);

        return new Cloudinary(configuration);
    }
}
