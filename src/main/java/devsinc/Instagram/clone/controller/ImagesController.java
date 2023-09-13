package devsinc.Instagram.clone.controller;

import devsinc.Instagram.clone.external.CloudinaryConfigs.CloudinaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Images API")
public class ImagesController {
    private final CloudinaryService cloudinaryService;
    @PostMapping(value = "/uploadProfileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(@RequestParam MultipartFile file,
                                                      HttpServletRequest request)
    {
        return ResponseEntity.ok(cloudinaryService.uploadProfileImage(file,request.getHeader("Authorization")));
    }
}
