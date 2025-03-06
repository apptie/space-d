package com.dnd.spaced.core.image.application;

import com.dnd.spaced.core.image.application.exception.ImageFileNotFoundException;
import com.dnd.spaced.global.config.properties.ImageStorePathProperties;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalImageService {

    private static final String FILE_PROTOCOL_PREFIX = "file:";

    private final ImageStorePathProperties imageStorePathProperties;

    public Resource readImage(String imageName) {
        try {
            return new UrlResource(FILE_PROTOCOL_PREFIX + imageStorePathProperties.path() + imageName);
        } catch (MalformedURLException e) {
            throw new ImageFileNotFoundException("지정한 이미지 파일을 찾지 못했습니다.");
        }
    }
}
