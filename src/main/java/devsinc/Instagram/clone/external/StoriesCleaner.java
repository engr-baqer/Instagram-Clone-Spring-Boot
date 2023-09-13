package devsinc.Instagram.clone.external;

import com.cloudinary.Cloudinary;
import devsinc.Instagram.clone.model.Stories;
import devsinc.Instagram.clone.repository.StoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StoriesCleaner {
    private final StoriesRepository storyRepository;
    private final Cloudinary cloudinary;

    @Scheduled(fixedDelay = 100000)
    @Transactional
    public void cleanupOldStories() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(20);

        List<Stories> storiesToDelete = storyRepository.findByCreationTimeBefore(oneMinuteAgo);

        for (Stories story : storiesToDelete) {
            try {
                cloudinary.uploader().destroy(story.getPublicId(),
                        Map.of());
                storyRepository.delete(story);
            } catch (IOException e) {
                throw new RuntimeException("Image is not existing on cloud!");
            }
        }
    }
}
