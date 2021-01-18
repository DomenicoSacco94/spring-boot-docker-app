package com.de.hiking.models;


import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Models the <code>Booking</code> entity and its properties
 */
@ApiModel("Booking")
@Entity
@Table(name = "booking", schema = "hiking")
public class Booking {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, name = "booking_id")
    private UUID bookingId;

    @JoinColumn(name = "hiker_id")
    @Column(name = "reserved_by_hiker_id")
    private UUID reservedByHikerId;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @ManyToOne
    @JoinColumn(name = "trail_id")
    private Trail trail;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "booking_hiker",
            schema = "hiking",
            joinColumns = {@JoinColumn(name = "booking_id")},
            inverseJoinColumns = {@JoinColumn(name = "hiker_id")}
    )
    private Set<Hiker> bookMembers = new HashSet<>();

    public Booking() {
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Trail getTrail() {
        return trail;
    }

    public void setTrail(Trail trail) {
        this.trail = trail;
    }

    public Set<Hiker> getBookMembers() {
        return bookMembers;
    }

    public void setBookMembers(Set<Hiker> bookMembers) {
        this.bookMembers = bookMembers;
    }

    public UUID getReservedByHikerId() {
        return reservedByHikerId;
    }

    public void setReservedByHikerId(UUID reservedByHikerId) {
        this.reservedByHikerId = reservedByHikerId;
    }

}
