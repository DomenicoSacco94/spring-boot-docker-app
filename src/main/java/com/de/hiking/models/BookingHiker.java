package com.de.hiking.models;


import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Models the connections between <code>Booking</code> and <code>Hiker</code> entities
 */
@ApiModel("BookingHiker")
@Entity
@Table(name = "booking_hiker", schema = "hiking")
public class BookingHiker implements Serializable {


    @Id
    @JoinColumn(name = "hiker_id")
    @Column(name = "hiker_id")
    private UUID hikerId;

    @Id
    @JoinColumn(name = "booking_id")
    @Column(name = "booking_id")
    private UUID bookingId;

    public UUID getHikerId() {
        return hikerId;
    }

    public void setHikerId(UUID hikerId) {
        this.hikerId = hikerId;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }
}
