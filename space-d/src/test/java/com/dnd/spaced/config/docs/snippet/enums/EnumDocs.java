package com.dnd.spaced.config.docs.snippet.enums;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnumDocs {

    private Map<String, String> jobGroup;
    private Map<String, String> company;
    private Map<String, String> experience;
    private Map<String, String> profileImageName;
}
