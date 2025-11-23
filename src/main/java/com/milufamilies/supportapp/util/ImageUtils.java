package com.milufamilies.supportapp.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ImageUtils {

    private static final String UPLOAD_DIR = "uploads";

    public void saveProfileImage(MultipartFile file, Long userId) {
        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            BufferedImage resizedImage = Thumbnails.of(originalImage)
                    .size(300, 300)
                    .asBufferedImage();

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File outputFile = new File(uploadDir, "user_" + userId + ".png");
            ImageIO.write(resizedImage, "png", outputFile);

        } catch (IOException e) {
            throw new RuntimeException("שגיאה בשמירת תמונת פרופיל", e);
        }
    }
}
