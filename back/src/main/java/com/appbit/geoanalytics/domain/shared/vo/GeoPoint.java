package com.appbit.geoanalytics.domain.shared.vo;

import com.appbit.geoanalytics.domain.shared.exception.SharedDomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record GeoPoint(BigDecimal latitude,
                       BigDecimal longitude) {

    private static final int SCALE = 6;

    private static final BigDecimal ZERO = new BigDecimal("0.000000");
    private static final BigDecimal LAT_MIN = new BigDecimal("-90.000000");
    private static final BigDecimal LAT_MAX = new BigDecimal("90.000000");
    private static final BigDecimal LON_MIN = new BigDecimal("-180.000000");
    private static final BigDecimal LON_MAX = new BigDecimal("180.000000");

    public GeoPoint {
        if (latitude == null) throw new SharedDomainException("Latitude cannot be null");
        if (longitude == null) throw new SharedDomainException("Longitude cannot be null");

        latitude = normalize(latitude, "Latitude");
        longitude = normalize(longitude, "Longitude");

        if (latitude.compareTo(LAT_MIN) < 0 || latitude.compareTo(LAT_MAX) > 0)
            throw new SharedDomainException("Latitude must be between -90 and 90 degrees");

        if (longitude.compareTo(LON_MIN) < 0 || longitude.compareTo(LON_MAX) > 0)
            throw new SharedDomainException("Longitude must be between -180 and 180 degrees");

        if (longitude.compareTo(LON_MAX) == 0) longitude = LON_MIN;

        if (latitude.compareTo(LAT_MAX) == 0 || latitude.compareTo(LAT_MIN) == 0)
            longitude = ZERO;
    }

    private static BigDecimal normalize(BigDecimal value, String fieldName) {
        try {
            return value.setScale(SCALE, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException _) {
            throw new SharedDomainException(
                    fieldName + " must have at most " + SCALE + " decimal places"
            );
        }
    }
}
