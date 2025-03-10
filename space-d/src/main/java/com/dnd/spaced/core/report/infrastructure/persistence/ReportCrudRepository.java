package com.dnd.spaced.core.report.infrastructure.persistence;

import com.dnd.spaced.core.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

interface ReportCrudRepository extends JpaRepository<Report, Long> {
}
