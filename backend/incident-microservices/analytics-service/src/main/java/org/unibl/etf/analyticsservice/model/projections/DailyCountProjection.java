package org.unibl.etf.analyticsservice.model.projections;

import java.time.LocalDateTime;

public interface DailyCountProjection {
    LocalDateTime getReportedAt();
    long getCount();
}
