package com.de.hiking.controller;

import com.de.hiking.models.Booking;
import com.de.hiking.service.BookingService;
import com.de.hiking.utils.LoggerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Contains the controller methods managing the <code>Booking</code> entity
 */
@Api(value = "/api/hiking/v1", tags = {"Hiking API v1"})
@RequestMapping("/api/hiking/v1")
@Controller
public class BookingController {

    Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    BookingService bookingService;

    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    @ApiOperation(value = "Booking creation", notes = "Make a booking")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Booking.class),
            @ApiResponse(code = 404, message = "A hiker inside the booking does not exist"),
            @ApiResponse(code = 400, message = "Bad request")})
    @ResponseBody
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking, @RequestParam UUID hikerId, @RequestParam UUID trailId) {
        LoggerUtil.logEnterMethod(logger, booking, hikerId, trailId);
        Booking result = bookingService.createBooking(booking, hikerId, trailId);
        LoggerUtil.logExitMethod(logger, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/booking/{bookingId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Booking deletion", notes = "Delete a booking")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Booking not found")})
    @ResponseBody
    public ResponseEntity deleteBooking(@PathVariable UUID bookingId) {
        LoggerUtil.logEnterMethod(logger, bookingId);
        bookingService.deleteBooking(bookingId);
        ResponseEntity result = new ResponseEntity(HttpStatus.OK);
        LoggerUtil.logExitMethod(logger, result);
        return result;
    }

    @RequestMapping(value = "/booking", method = RequestMethod.GET)
    @ApiOperation(value = "Booking retrieval from a Hiker or for a specific date", notes = "Get all the bookings from a Hiker or for a specific date")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Booking.class, responseContainer = "list")})
    @ResponseBody
    public ResponseEntity<List<Booking>> getBookings(@RequestParam(name = "date", required = false)
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                     @RequestParam(name = "hikerId", required = false) UUID hikerId) {
        LoggerUtil.logEnterMethod(logger, date);
        List<Booking> result = bookingService.getBookings(date, hikerId);
        LoggerUtil.logExitMethod(logger, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
