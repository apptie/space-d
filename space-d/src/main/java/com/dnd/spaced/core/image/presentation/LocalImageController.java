package com.dnd.spaced.core.image.presentation;

import com.dnd.spaced.core.image.application.LocalImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class LocalImageController {

    private final LocalImageService localImageService;

    @GetMapping(value = "/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> readImage(@PathVariable String imageName) {
        Resource resource = localImageService.readImage(imageName);

        return ResponseEntity.ok()
                             .body(resource);
    }
}
