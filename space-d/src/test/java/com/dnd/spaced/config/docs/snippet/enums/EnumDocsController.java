package com.dnd.spaced.config.docs.snippet.enums;

import com.dnd.spaced.config.docs.snippet.dto.response.CommonDocsResponse;
import com.dnd.spaced.core.account.domain.enums.Company;
import com.dnd.spaced.core.account.domain.enums.Experience;
import com.dnd.spaced.core.account.domain.enums.JobGroup;
import com.dnd.spaced.core.account.domain.enums.ProfileImageName;
import com.dnd.spaced.core.quiz.application.dto.response.TodayQuizResponse.TodayQuizStatus;
import com.dnd.spaced.core.quiz.domain.enums.QuizCategory;
import com.dnd.spaced.core.report.domain.enums.ReportReason;
import com.dnd.spaced.core.report.domain.enums.ReportStatus;
import com.dnd.spaced.core.word.domain.enums.Category;
import com.dnd.spaced.core.word.domain.enums.PronunciationType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class EnumDocsController {

    @GetMapping("/enums")
    public ResponseEntity<CommonDocsResponse<EnumDocs>> findEnums() {
        EnumDocs enumDocs =
                EnumDocs.builder()
                        .jobGroup(EnumDocsConverter.convert(JobGroup.values(), JobGroup::getName))
                        .company(EnumDocsConverter.convert(Company.values(), Company::getName))
                        .experience(EnumDocsConverter.convert(Experience.values(), Experience::getName))
                        .profileImageName(EnumDocsConverter.convert(ProfileImageName.values(), ProfileImageName::getKorean))
                        .category(EnumDocsConverter.convert(Category.values(), Category::getName))
                        .pronunciationType(EnumDocsConverter.convert(PronunciationType.values(), PronunciationType::getName))
                        .quizCategory(EnumDocsConverter.convert(QuizCategory.values(), QuizCategory::getName))
                        .reportReason(EnumDocsConverter.convert(ReportReason.values(), ReportReason::getCause))
                        .reportStatus(EnumDocsConverter.convert(ReportStatus.values(), ReportStatus::getName))
                        .todayQuizStatus(EnumDocsConverter.convert(TodayQuizStatus.values(), TodayQuizStatus::getName))
                        .build();

        return ResponseEntity.ok(new CommonDocsResponse<>(enumDocs));
    }
}
