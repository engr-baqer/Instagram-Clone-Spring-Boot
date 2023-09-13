package devsinc.Instagram.clone.external.CloudinaryConfigs;

import com.cloudinary.Cloudinary;
import devsinc.Instagram.clone.service.InterfaceImpls.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final UserServiceImpl userService;


    public String uploadProfileImage(MultipartFile file, String token) {
        Map<String, Object> data;
        try {
            data = cloudinary.uploader().upload(file.getBytes(), Map.of("folder", "Profile Images"));
            userService.updateUserProfileImageUrl(data.get("url").toString(),data.get("public_id").toString(),token.substring(7));
            return data.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image!", e);
        }
    }
}

