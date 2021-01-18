package com.de.hiking.repository;

import com.de.hiking.models.Hiker;
import com.de.hiking.utils.LoggerUtil;
import org.hibernate.Session;
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
 * Repository class that contains the methods managing the <code>Hiker</code> entity
 */
@Repository
public class HikerRepository {

    Logger logger = LoggerFactory.getLogger(HikerRepository.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EntityManager entityManager;

    /**
     * Queries the DB and registers a <code>Hiker</code>
     *
     * @param hiker
     * @return the created <code>Hiker</code>
     */
    public Hiker createHiker(Hiker hiker) {

        LoggerUtil.logEnterMethod(logger, hiker);
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        //Save the hiker in database
        session.save(hiker);
        //Commit the transaction
        session.getTransaction().commit();
        session.close();

        sessionFactory.getCurrentSession().close();

        LoggerUtil.logExitMethod(logger, hiker);

        return hiker;
    }

    /**
     * Queries the DB and deletes a <code>Hiker</code>
     *
     * @param hikerId
     */
    public void deleteHiker(UUID hikerId) {

        LoggerUtil.logEnterMethod(logger, hikerId);
        Session session = sessionFactory.openSession();

        //check if hiker exists
        boolean isPresent = getHikers().stream().map(a -> a.getHikerId()).anyMatch(a -> a.equals(hikerId));
        if (!isPresent) {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete a non existing hiker");
            LoggerUtil.logException(logger, re);
            throw re;
        }

        Hiker hiker = new Hiker();
        hiker.setHikerId(hikerId);

        session.beginTransaction();
        session.delete(hiker);
        session.getTransaction().commit();
        session.close();

        LoggerUtil.logExitMethod(logger, hiker);
    }

    /**
     * Queries the DB and retrieves a <code>List</code> of all the registered <code>Hiker</code>
     *
     * @return
     */
    public List<Hiker> getHikers() {

        LoggerUtil.logEnterMethod(logger);

        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Hiker> cr = cb.createQuery(Hiker.class);
        Root<Hiker> root = cr.from(Hiker.class);
        cr.select(root);

        Query<Hiker> query = sessionFactory.openSession().createQuery(cr);
        List<Hiker> results = query.getResultList();

        sessionFactory.getCurrentSession().close();

        LoggerUtil.logExitMethod(logger, results);

        return results;
    }


    /**
     * Queries the DB and retrieves a<code>Hiker</code>
     *
     * @param hikerId
     * @return the <code>Hiker</code>
     */
    public Hiker getHiker(UUID hikerId) {

        LoggerUtil.logEnterMethod(logger, hikerId);
        Hiker hiker = entityManager.find(Hiker.class, hikerId);
        LoggerUtil.logExitMethod(logger, hiker);

        return hiker;
    }

}
