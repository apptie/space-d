package com.dnd.spaced.core.account.domain.embed;

import com.dnd.spaced.core.account.domain.enums.RegistrationId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Social {

    @Enumerated(EnumType.STRING)
    private RegistrationId registrationId;

    private String socialIdentifier;

    public Social(RegistrationId registrationId, String socialIdentifier) {
        this.registrationId = registrationId;
        this.socialIdentifier = socialIdentifier;
    }
}
