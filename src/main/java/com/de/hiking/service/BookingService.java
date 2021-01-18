package com.de.hiking.service;


import com.de.hiking.models.Booking;
import com.de.hiking.models.Hiker;
import com.de.hiking.models.Trail;
import com.de.hiking.repository.BookingRepository;
import com.de.hiking.repository.HikerRepository;
import com.de.hiking.repository.TrailRepository;
import com.de.hiking.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * Service layer managing the <code>Booking</code> entity and performing logical validation on the booking input
 */
@Service
public class BookingService {

    Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    TrailRepository trailRepository;

    @Autowired
    HikerRepository hikerRepository;

    /**
     * Creates a <code>Booking</code> entity
     *
     * @param booking
     * @param hikerId
     * @param trailId
     * @return the created entity
     */
    public Booking createBooking(Booking booking, UUID hikerId, UUID trailId) {
        LoggerUtil.logEnterMethod(logger, hikerId, trailId);
        if (verifyBooking(booking, trailId, hikerId)) {
            Booking result = bookingRepository.createBooking(booking, hikerId, trailId);
            LoggerUtil.logExitMethod(logger, result);
            return result;
        } else {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot book: one or more hiker do not respect the trail age constraints");
            LoggerUtil.logException(logger, re);
            throw re;
        }

    }

    /**
     * Deletes a <code>Booking</code> entity
     *
     * @param bookingId
     */
    public void deleteBooking(UUID bookingId) {
        LoggerUtil.logEnterMethod(logger, bookingId);
        bookingRepository.deleteBooking(bookingId);
        LoggerUtil.logExitMethod(logger, "");
    }

    /**
     * Retrieves all the bookings made by a <code>Hiker</code>
     *
     * @param hikerId
     * @return the list of <code>Booking</code> made by a <code>Hiker</code> or for a certain <code>Date</code>
     */
    public List<Booking> getBookings(LocalDate date, UUID hikerId) {
        LoggerUtil.logEnterMethod(logger, date, hikerId);

        if (date == null && hikerId == null) {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.BAD_REQUEST, "No selection criteria for the booking: insert either a hikerId or a date");
            LoggerUtil.logException(logger, re);
            throw re;
        }
        if (date != null && hikerId != null) {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Select bookings either for a biker or for a date");
            LoggerUtil.logException(logger, re);
            throw re;
        }

        List<Booking> result;

        if (date != null) {
            result = bookingRepository.getBookings(date);
        } else {
            result = bookingRepository.getBookings(hikerId);
        }
        LoggerUtil.logExitMethod(logger, result);
        return result;
    }

    /**
     * Verifies the correctness of a <code>Booking</code> in particular
     * it validates the age constraints and the existence of the <code>Hiker</code>
     * contained in the <code>Booking</code> instance
     *
     * @param booking
     * @param trailId
     * @param hikerId
     * @return a boolean indicating the booking correctness
     */
    private boolean verifyBooking(Booking booking, UUID trailId, UUID hikerId) {
        LoggerUtil.logEnterMethod(logger, trailId, hikerId);
        boolean accept;

        Trail trail = trailRepository.getTrail(trailId);
        int ageLowerLimit = trail.getMinAge();
        int ageUpperLimit = trail.getMaxAge();

        booking.setTrail(trail);

        Set<Hiker> hikers = booking.getBookMembers();

        if (hikers.isEmpty()) {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.BAD_REQUEST, "No empty bookings allowed");
            LoggerUtil.logException(logger, re);
            throw re;
        }

        Hiker booker = hikerRepository.getHiker(hikerId);
        if (booker == null) {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.NOT_FOUND, "Hiker making the booking not found");
            LoggerUtil.logException(logger, re);
            throw re;
        }

        Set<Hiker> membersToValidate = new HashSet<>();

        for (Hiker hiker : booking.getBookMembers()) {
            hiker = hikerRepository.getHiker(hiker.getHikerId());
            if (hiker == null) {
                ResponseStatusException re = new ResponseStatusException(HttpStatus.NOT_FOUND, "Hiker inside the booking not found");
                LoggerUtil.logException(logger, re);
                throw re;
            }
            membersToValidate.add(hiker);
        }

        booking.setBookMembers(membersToValidate);

        accept = hikers.stream().allMatch(a -> a.getAge() >= ageLowerLimit && a.getAge() <= ageUpperLimit);

        LoggerUtil.logExitMethod(logger, accept);
        return accept;

    }

}
