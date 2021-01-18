package com.de.hiking.repository;

import com.de.hiking.models.Booking;
import com.de.hiking.models.Hiker;
import com.de.hiking.models.Trail;
import com.de.hiking.utils.LoggerUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository class that contains the methods managing the <code>Booking</code> entity
 */
@Repository
public class BookingRepository {

    Logger logger = LoggerFactory.getLogger(BookingRepository.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EntityManager entityManager;

    /**
     * Queries the DB and retrieves a <code>List</code> of <code>Booking</code> associated to a <code>Hiker</code>
     *
     * @param hikerId
     * @return the <code>List</code> of <code>Booking</code>
     */
    public List<Booking> getBookings(UUID hikerId) {
        LoggerUtil.logEnterMethod(logger, hikerId);
        Session session = sessionFactory.openSession();

        if (entityManager.find(Hiker.class, hikerId) == null) {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot retrieve bookings of a non existent hiker");
            LoggerUtil.logException(logger, re);
            throw re;
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Booking> criteria = builder.createQuery(Booking.class);
        Root<Booking> myObjectRoot = criteria.from(Booking.class);

        Predicate dateRestriction = builder.and(
                builder.equal(myObjectRoot.get("reservedByHikerId"), hikerId)
        );

        criteria.select(myObjectRoot).where(dateRestriction);

        TypedQuery<Booking> query = session.createQuery(criteria);

        List<Booking> results = query.getResultList();

        sessionFactory.getCurrentSession().close();

        LoggerUtil.logExitMethod(logger, results);

        return results;
    }


    /**
     * Queries the DB and deletes <code>Booking</code>
     *
     * @param bookingId
     */
    public void deleteBooking(UUID bookingId) {

        LoggerUtil.logEnterMethod(logger, bookingId);
        Session session = sessionFactory.openSession();

        if (entityManager.find(Booking.class, bookingId) == null) {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete a non existing booking");
            LoggerUtil.logException(logger, re);
            throw re;
        }

        Booking booking = new Booking();
        booking.setBookingId(bookingId);

        session.beginTransaction();
        session.delete(booking);
        session.getTransaction().commit();
        session.close();

        LoggerUtil.logExitMethod(logger, "");
    }

    /**
     * Queries the DB and retrieves a <code>List</code> of <code>Booking</code> belonging to a
     * given <code>LocalDate</code>
     *
     * @param date
     * @return the <code>List</code> of <code>Booking</code>
     */
    public List<Booking> getBookings(LocalDate date) {

        LoggerUtil.logEnterMethod(logger, date);

        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Booking> criteria = builder.createQuery(Booking.class);
        Root<Booking> myObjectRoot = criteria.from(Booking.class);

        Predicate dateRestriction = builder.and(
                builder.equal(myObjectRoot.get("bookingDate"), date)
        );

        criteria.select(myObjectRoot).where(dateRestriction);

        TypedQuery<Booking> query = session.createQuery(criteria);

        List<Booking> results = query.getResultList();

        sessionFactory.getCurrentSession().close();

        LoggerUtil.logExitMethod(logger, results);

        return results;
    }

    /**
     * Queries the DB and creates a <code>Booking</code> made by a <code>Hiker</code> to a given <code>Trail</code>
     *
     * @param booking
     * @param hikerId
     * @param trailId
     * @return the created <code>Booking</code> instance
     */
    public Booking createBooking(Booking booking, UUID hikerId, UUID trailId) {

        LoggerUtil.logEnterMethod(logger, booking, hikerId, trailId);

        Session session = sessionFactory.openSession();

        booking.setReservedByHikerId(hikerId);

        Trail trail = entityManager.find(Trail.class, trailId);

        booking.setTrail(trail);
        session.beginTransaction();
        session.save(booking);
        session.getTransaction().commit();

        session.close();

        LoggerUtil.logExitMethod(logger, booking);

        return booking;
    }


}
