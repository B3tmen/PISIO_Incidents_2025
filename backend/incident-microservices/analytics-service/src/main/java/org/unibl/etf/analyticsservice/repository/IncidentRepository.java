package org.unibl.etf.analyticsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.unibl.etf.analyticsservice.model.entity.IncidentEntity;
import org.unibl.etf.analyticsservice.model.projections.DailyCountProjection;
import org.unibl.etf.analyticsservice.model.projections.LocationCountProjection;
import org.unibl.etf.analyticsservice.model.projections.TypeCountProjection;

import java.util.List;

public interface IncidentRepository extends JpaRepository<IncidentEntity, Integer> {

    @Query("SELECT i.type AS type, COUNT(i) AS count " +
            "FROM IncidentEntity i " +
            "GROUP BY i.type")
    List<TypeCountProjection> countByType();

    @Query("SELECT i.reportedAt AS reportedAt, COUNT(i) AS count " +
            "FROM IncidentEntity i " +
            "GROUP BY i.reportedAt")
    List<DailyCountProjection> countByDay();

    @Query("SELECT l.city AS city, l.country AS country, COUNT(i) AS count " +
            "FROM IncidentEntity i " +
            "JOIN i.location l " +
            "GROUP BY l.city, l.country")
    List<LocationCountProjection> countByLocation();
}
