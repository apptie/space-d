package com.dnd.spaced.config.stub;

import com.dnd.spaced.global.config.properties.NicknameProperties;
import java.util.Collections;

public class StubNicknameProperties extends NicknameProperties {

    public StubNicknameProperties() {
        super(Collections.emptyList(), Collections.emptyList(), "%s%03d");
    }

    @Override
    public String generate() {
        return "재빠른지구";
    }
}
