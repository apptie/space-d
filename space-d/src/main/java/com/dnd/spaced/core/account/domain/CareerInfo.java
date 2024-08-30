package com.dnd.spaced.core.account.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareerInfo {

    @Enumerated(EnumType.STRING)
    private JobGroup jobGroup;

    @Enumerated(EnumType.STRING)
    private Company company;

    @Enumerated(EnumType.STRING)
    private Experience experience;

    @Builder
    private CareerInfo(String jobGroupName, String companyName, String experienceName) {
        this.jobGroup = JobGroup.findBy(jobGroupName);
        this.company = Company.findBy(companyName);
        this.experience = Experience.findBy(experienceName);
    }
}
