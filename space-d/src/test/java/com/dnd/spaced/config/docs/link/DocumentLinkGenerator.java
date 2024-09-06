package com.dnd.spaced.config.docs.link;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DocumentLinkGenerator {

    public static String generateLinkCode(DocsUrl docsUrl) {
        return String.format("link:common/%s.html[%s,role=\"popup\"]", docsUrl.pageId, docsUrl.text);
    }

    public enum DocsUrl {
        JOB_GROUP("job-group", "직군"),
        COMPANY("company","회사"),
        EXPERIENCE("experience", "경력"),
        PROFILE_IMAGE_NAME("profile-image-name", "프로필 이미지 이름"),
        CATEGORY("category", "카테고리"),
        PRONUNCIATION_TYPE("pronunciationType", "용어 발음 타입");

        DocsUrl(String pageId, String text) {
            this.pageId = pageId;
            this.text = text;
        }

        private final String pageId;

        @Getter
        private final String text;
    }
}
