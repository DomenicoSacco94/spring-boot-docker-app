package com.de.hiking.repository;

import com.de.hiking.models.Trail;
import com.de.hiking.utils.LoggerUtil;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

/**
 * Repository class that contains the methods managing the <code>Trail</code> entity
 */
@Repository
public class TrailRepository {

    Logger logger = LoggerFactory.getLogger(TrailRepository.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EntityManager entityManager;

    /**
     * Queries the DB and retrieves a <code>List</code> of all <code>Trail</code>
     *
     * @return the <code>List</code> of <code>Trail</code>
     */
    public List<Trail> getTrails() {

        LoggerUtil.logEnterMethod(logger);
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Trail> cr = cb.createQuery(Trail.class);
        Root<Trail> root = cr.from(Trail.class);
        cr.select(root);

        Query<Trail> query = sessionFactory.openSession().createQuery(cr);
        List<Trail> results = query.getResultList();

        LoggerUtil.logExitMethod(logger, results);

        return results;
    }

    /**
     * Queries the DB and retrieves <code>Trail</code>
     *
     * @return the retrieved <code>Trail</code>
     */
    public Trail getTrail(UUID trailId) {

        LoggerUtil.logEnterMethod(logger, trailId);

        Trail trail = entityManager.find(Trail.class, trailId);
        if (trail == null) {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.NOT_FOUND, "Trail not found");
            LoggerUtil.logException(logger, re);
            throw re;
        }
        LoggerUtil.logExitMethod(logger, trail);

        return trail;
    }
}
