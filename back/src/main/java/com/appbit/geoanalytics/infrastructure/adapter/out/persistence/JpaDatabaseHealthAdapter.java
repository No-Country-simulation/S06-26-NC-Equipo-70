package com.appbit.geoanalytics.infrastructure.adapter.out.persistence;

import com.appbit.geoanalytics.application.port.out.DatabaseHealthPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JpaDatabaseHealthAdapter implements DatabaseHealthPort {

    private static final String HEALTH_QUERY = "SELECT 1";
    private static final int QUERY_TIMEOUT_MS = 3000;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true, timeout = 3)
    public boolean isDatabaseAvailable() {
        try {
            Query query = entityManager.createNativeQuery(HEALTH_QUERY);
            query.setHint("jakarta.persistence.query.timeout", QUERY_TIMEOUT_MS);

            Object result = query.getSingleResult();

            if (result instanceof Number number) {
                return number.intValue() == 1;
            }

            return "1".equals(String.valueOf(result));

        } catch (RuntimeException _) {
            return false;
        }
    }
}