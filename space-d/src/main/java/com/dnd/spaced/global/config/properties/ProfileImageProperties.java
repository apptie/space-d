package com.dnd.spaced.global.config.properties;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.profile.image")
@RequiredArgsConstructor
public class ProfileImageProperties {

    private final List<String> name;

    public String find() {
        int profileImageIndex = generateRandomIndex(name.size());

        return name.get(profileImageIndex);
    }

    private int generateRandomIndex(int range) {
        return (int) (Math.random() * range);
    }
}
