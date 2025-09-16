package org.unibl.etf.analyticsservice.model.projections;

public interface LocationCountProjection {
    String getCity();
    String getCountry();
    long getCount();
}
