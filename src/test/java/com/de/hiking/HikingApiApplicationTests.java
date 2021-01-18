package com.de.hiking;

import com.de.hiking.models.Booking;
import com.de.hiking.models.Hiker;
import com.de.hiking.models.Trail;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = HikingApiApplication.class)
class HikingApiApplicationTests {

    private String API_PATH = "/api/hiking/v1";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataSource datasource;

    private static boolean dataLoaded = false;


    //sets up some hikers on which do the testing
    @BeforeAll
    public void setup() throws SQLException {
        if (!dataLoaded) {
            try (Connection con = datasource.getConnection()) {
                ScriptUtils.executeSqlScript(con, new ClassPathResource("hiking_test.sql"));
                dataLoaded = true;
            }
        }
    }

    //deletes the hiker samples used in testing
    @AfterAll
    public void destroy() throws SQLException {
        if (dataLoaded) {
            try (Connection con = datasource.getConnection()) {
                ScriptUtils.executeSqlScript(con, new ClassPathResource("hiking_post_test.sql"));
                dataLoaded = false;
            }
        }
    }

    @DisplayName("Checks that the booking deletion works")
    @Test
    void checkDeleteBooking() {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());

        Set members = new HashSet<Hiker>();

        Hiker h1 = new Hiker();
        h1.setHikerId(UUID.fromString("3c8097ef-ecef-43ea-ae43-3cb88cd7ab7e"));
        h1.setAge(15);
        members.add(h1);

        booking.setBookMembers(members);

        ResponseEntity<Booking> result = this.restTemplate.postForEntity(API_PATH + "/booking?hikerId=9899a076-9d93-471e-b06e-3447bcaa5200&trailId=ac43d61a-9a62-4151-9448-f4b09dba6b54", booking, Booking.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        ResponseEntity<Object> resultDelete = this.restTemplate.exchange(
                API_PATH + "/booking/" + result.getBody().getBookingId(),
                HttpMethod.DELETE,
                new HttpEntity<>(""),
                Object.class);

        assertEquals(HttpStatus.OK, resultDelete.getStatusCode());
    }

    @DisplayName("Checks that the deletion of a non existing book does not work")
    @Test
    void checkDeleteNonExistingBooking() {

        ResponseEntity<Object> resultDelete = this.restTemplate.exchange(
                API_PATH + "/booking/" + UUID.randomUUID(),
                HttpMethod.DELETE,
                new HttpEntity<>(""),
                Object.class);

        assertEquals(HttpStatus.NOT_FOUND, resultDelete.getStatusCode());
    }

    @DisplayName("Checks that the registration of a Hiker works")
    @Test
    void checkCreateHiker() {

        Hiker hiker = new Hiker();
        hiker.setAge(22);
        hiker.setMail("randommail@random.com");
        hiker.setName("John");
        hiker.setSurname("Doe");

        ResponseEntity<Hiker> result = this.restTemplate.postForEntity(API_PATH + "/hiker", hiker, Hiker.class);

        assert (result.getBody().getHikerId() != null);

        Hiker hikertToDelete = result.getBody();

        this.restTemplate.exchange(
                API_PATH + "/hiker/" + hikertToDelete.getHikerId(),
                HttpMethod.DELETE,
                new HttpEntity<>(""),
                Object.class);

    }

    @DisplayName("Checks that the deletion a Hiker works")
    @Test
    void checkDeleteHiker() {

        Hiker hiker = new Hiker();
        hiker.setAge(22);
        hiker.setMail("randommail@random.com");
        hiker.setName("John");
        hiker.setSurname("Doe");
        ResponseEntity<Hiker> result = this.restTemplate.postForEntity(API_PATH + "/hiker", hiker, Hiker.class);
        assert (result.getBody().getHikerId() != null);

        Hiker hikertToDelete = result.getBody();

        ResponseEntity<Object> resultDelete = this.restTemplate.exchange(
                API_PATH + "/hiker/" + hikertToDelete.getHikerId(),
                HttpMethod.DELETE,
                new HttpEntity<>(""),
                Object.class);

        assertEquals(HttpStatus.OK, resultDelete.getStatusCode());
    }

    @DisplayName("Checks that the deletion of a non existing hiker does not work")
    @Test
    void checkDeleteNonExistingHiker() {

        ResponseEntity<Object> result = this.restTemplate.exchange(
                API_PATH + "/hiker/" + UUID.randomUUID(),
                HttpMethod.DELETE,
                new HttpEntity<>(""),
                Object.class);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    }

    @DisplayName("Checks that retrieval of all hikers works")
    @Test
    void checkGetHikers() {

        ResponseEntity<Hiker[]> result = this.restTemplate.getForEntity(API_PATH + "/hikers", Hiker[].class);
        List<Hiker> hikers = Arrays.asList(result.getBody());
        assert (hikers.size() > 0);
    }

    @DisplayName("Checks that retrieval of all trails works")
    @Test
    void checkGetTrails() {

        ResponseEntity<Trail[]> result = this.restTemplate.getForEntity(API_PATH + "/trails", Trail[].class);
        List<Trail> trails = Arrays.asList(result.getBody());
        assert (trails.size() > 0);
    }

    @DisplayName("Checks that the booking creation works")
    @Test
    void checkPostBooking() {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());

        //add members to booking
        Set members = new HashSet<Hiker>();

        Hiker h1 = new Hiker();
        h1.setHikerId(UUID.fromString("3c8097ef-ecef-43ea-ae43-3cb88cd7ab7e"));
        h1.setAge(15);
        members.add(h1);

        Hiker h2 = new Hiker();
        h2.setHikerId(UUID.fromString("a3e69f82-0645-4ae4-9f44-a46de25dbea6"));
        h2.setAge(15);
        members.add(h2);

        Hiker h3 = new Hiker();
        h3.setHikerId(UUID.fromString("d7be7688-01c4-4713-b273-3749ccd2a1ab"));
        h3.setAge(15);
        members.add(h3);

        booking.setBookMembers(members);

        ResponseEntity<Booking> result = this.restTemplate.postForEntity(API_PATH + "/booking?hikerId=d7be7688-01c4-4713-b273-3749ccd2a1ab&trailId=c820b2b3-f10a-4ab9-86c0-e32362e2cc1d", booking, Booking.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        //the booking has its members
        assert (result.getBody().getBookMembers().size() == 3);

        ResponseEntity<Object> resultDelete = this.restTemplate.exchange(
                API_PATH + "/booking/" + result.getBody().getBookingId(),
                HttpMethod.DELETE,
                new HttpEntity<>(""),
                Object.class);

    }

    @DisplayName("Check that a book including a non existing hiker faults")
    @Test
    void checkPostBookingNonExistingMember() {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());

        //add members to booking
        Set members = new HashSet<Hiker>();

        Hiker h1 = new Hiker();
        h1.setHikerId(UUID.randomUUID());
        h1.setAge(15);
        members.add(h1);

        Hiker h2 = new Hiker();
        h2.setHikerId(UUID.fromString("a3e69f82-0645-4ae4-9f44-a46de25dbea6"));
        h2.setAge(15);
        members.add(h2);

        Hiker h3 = new Hiker();
        h3.setHikerId(UUID.fromString("d7be7688-01c4-4713-b273-3749ccd2a1ab"));
        h3.setAge(15);
        members.add(h3);

        booking.setBookMembers(members);

        ResponseEntity<Booking> result = this.restTemplate.postForEntity(API_PATH + "/booking?hikerId=d7be7688-01c4-4713-b273-3749ccd2a1ab&trailId=c820b2b3-f10a-4ab9-86c0-e32362e2cc1d", booking, Booking.class);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    }

    @DisplayName("Check that a book including a hiker not respecting the age constraints fails")
    @Test
    void checkPostBookingAgeWrongMember() {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());

        //add members to booking
        Set members = new HashSet<Hiker>();

        Hiker h2 = new Hiker();
        h2.setHikerId(UUID.fromString("a3e69f82-0645-4ae4-9f44-a46de25dbea6"));
        h2.setAge(15);
        members.add(h2);

        Hiker h3 = new Hiker();
        h3.setHikerId(UUID.fromString("d7be7688-01c4-4713-b273-3749ccd2a1ab"));
        h3.setAge(5);
        members.add(h3);

        booking.setBookMembers(members);

        ResponseEntity<Booking> result = this.restTemplate.postForEntity(API_PATH + "/booking?hikerId=d7be7688-01c4-4713-b273-3749ccd2a1ab&trailId=c820b2b3-f10a-4ab9-86c0-e32362e2cc1d", booking, Booking.class);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

    }

    @DisplayName("Check that a book referring to a non existing trail fails")
    @Test
    void checkPostBookingAgeWrongTrail() {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());

        //add members to booking
        Set members = new HashSet<Hiker>();

        Hiker h2 = new Hiker();
        h2.setHikerId(UUID.fromString("a3e69f82-0645-4ae4-9f44-a46de25dbea6"));
        h2.setAge(15);
        members.add(h2);

        Hiker h3 = new Hiker();
        h3.setHikerId(UUID.fromString("d7be7688-01c4-4713-b273-3749ccd2a1ab"));
        h3.setAge(5);
        members.add(h3);

        booking.setBookMembers(members);

        ResponseEntity<Booking> result = this.restTemplate.postForEntity(API_PATH + "/booking?hikerId=d7be7688-01c4-4713-b273-3749ccd2a1ab&trailId=" + UUID.randomUUID(), booking, Booking.class);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    }

    @DisplayName("Check that a book made by a non existing hiker fails")
    @Test
    void checkPostBookingAgeWrongHiker() {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());

        //add members to booking
        Set members = new HashSet<Hiker>();

        Hiker h2 = new Hiker();
        h2.setHikerId(UUID.fromString("a3e69f82-0645-4ae4-9f44-a46de25dbea6"));
        h2.setAge(25);
        members.add(h2);

        Hiker h3 = new Hiker();
        h3.setHikerId(UUID.fromString("d7be7688-01c4-4713-b273-3749ccd2a1ab"));
        h3.setAge(25);
        members.add(h3);

        booking.setBookMembers(members);

        ResponseEntity<Booking> result = this.restTemplate.postForEntity(API_PATH + "/booking?hikerId=" + UUID.randomUUID() + "&trailId=c820b2b3-f10a-4ab9-86c0-e32362e2cc1d", booking, Booking.class);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

    }

    @DisplayName("Check that a book including no hikers fails")
    @Test
    void checkPostBookingEmptyMember() {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());

        //add members to booking
        Set members = new HashSet<Hiker>();

        booking.setBookMembers(members);

        ResponseEntity<Booking> result = this.restTemplate.postForEntity(API_PATH + "/booking?hikerId=d7be7688-01c4-4713-b273-3749ccd2a1ab&trailId=c820b2b3-f10a-4ab9-86c0-e32362e2cc1d", booking, Booking.class);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

    }


    @DisplayName("Checks that the booking retrieval per hiker works")
    @Test
    void checkGetBookingHiker() {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());

        //add members to booking
        Set members = new HashSet<Hiker>();

        Hiker h1 = new Hiker();
        h1.setHikerId(UUID.fromString("3c8097ef-ecef-43ea-ae43-3cb88cd7ab7e"));
        h1.setAge(15);
        members.add(h1);

        Hiker h2 = new Hiker();
        h2.setHikerId(UUID.fromString("a3e69f82-0645-4ae4-9f44-a46de25dbea6"));
        h2.setAge(15);
        members.add(h2);

        Hiker h3 = new Hiker();
        h3.setHikerId(UUID.fromString("d7be7688-01c4-4713-b273-3749ccd2a1ab"));
        h3.setAge(15);
        members.add(h3);

        booking.setBookMembers(members);

        ResponseEntity<Booking> result = this.restTemplate.postForEntity(API_PATH + "/booking?hikerId=d7be7688-01c4-4713-b273-3749ccd2a1ab&trailId=c820b2b3-f10a-4ab9-86c0-e32362e2cc1d", booking, Booking.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        //the booking has its members
        assert (result.getBody().getBookMembers().size() == 3);

        ResponseEntity<Booking[]> resultGet = this.restTemplate.getForEntity(API_PATH + "/booking?hikerId=d7be7688-01c4-4713-b273-3749ccd2a1ab", Booking[].class);

        //only bookings for the selected hiker
        Arrays.asList(resultGet.getBody()).stream().map(a -> a.getBookingId()).allMatch(a -> a.equals(UUID.fromString("d7be7688-01c4-4713-b273-3749ccd2a1ab")));

        ResponseEntity<Object> resultDelete = this.restTemplate.exchange(
                API_PATH + "/booking/" + result.getBody().getBookingId(),
                HttpMethod.DELETE,
                new HttpEntity<>(""),
                Object.class);

    }

    @DisplayName("Checks that the booking retrieval does not work with no params")
    @Test
    void checkGetBookingNoParam() {
        ResponseEntity<Object> resultGet = this.restTemplate.getForEntity(API_PATH + "/booking", Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, resultGet.getStatusCode());
    }

    @DisplayName("Checks that the booking retrieval does not work with both params")
    @Test
    void checkGetBookingBothParam() {
        ResponseEntity<Object> resultGet = this.restTemplate.getForEntity(API_PATH + "/booking?hikerId=" + UUID.randomUUID() + "&date=" + LocalDate.now(), Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, resultGet.getStatusCode());
    }

    @DisplayName("Checks that retrieving a non existing hiker returns an error message")
    @Test
    void checkGetNonExistingHiker() {
        ResponseEntity<Object> resultGet = this.restTemplate.getForEntity(API_PATH + "/hiker/" + UUID.randomUUID(), Object.class);
        assertEquals(HttpStatus.NOT_FOUND, resultGet.getStatusCode());
    }

    @DisplayName("Checks that retrieving an existing hiker works")
    @Test
    void checkGetHiker() {
        ResponseEntity<Hiker> resultGet = this.restTemplate.getForEntity(API_PATH + "/hiker/3c8097ef-ecef-43ea-ae43-3cb88cd7ab7e", Hiker.class);
        assertEquals(HttpStatus.OK, resultGet.getStatusCode());
    }

    @DisplayName("Checks that the retrieval of books belonging to a non existing hiker returns an error")
    @Test
    void checkGetBookingNonExistingHiker() {
        ResponseEntity<Object> resultGet = this.restTemplate.getForEntity(API_PATH + "/booking?hikerId=" + UUID.randomUUID(), Object.class);
        assertEquals(HttpStatus.NOT_FOUND, resultGet.getStatusCode());
    }

    @DisplayName("Checks that the retrieval of the bookings for a specific date works")
    @Test
    void checkGetBookingDate() {
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now());

        //add members to booking
        Set members = new HashSet<Hiker>();

        Hiker h1 = new Hiker();
        h1.setHikerId(UUID.fromString("3c8097ef-ecef-43ea-ae43-3cb88cd7ab7e"));
        h1.setAge(15);
        members.add(h1);

        Hiker h2 = new Hiker();
        h2.setHikerId(UUID.fromString("a3e69f82-0645-4ae4-9f44-a46de25dbea6"));
        h2.setAge(15);
        members.add(h2);

        Hiker h3 = new Hiker();
        h3.setHikerId(UUID.fromString("d7be7688-01c4-4713-b273-3749ccd2a1ab"));
        h3.setAge(15);
        members.add(h3);

        booking.setBookMembers(members);

        ResponseEntity<Booking> result = this.restTemplate.postForEntity(API_PATH + "/booking?hikerId=d7be7688-01c4-4713-b273-3749ccd2a1ab&trailId=c820b2b3-f10a-4ab9-86c0-e32362e2cc1d", booking, Booking.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        //the booking has its members
        assert (result.getBody().getBookMembers().size() == 3);

        ResponseEntity<Booking[]> resultGet = this.restTemplate.getForEntity(API_PATH + "/booking?date=" + LocalDate.now(), Booking[].class);

        //only bookings for the selected hiker
        Arrays.asList(resultGet.getBody()).stream().map(a -> a.getBookingId()).allMatch(a -> a.equals(UUID.fromString("d7be7688-01c4-4713-b273-3749ccd2a1ab")));

        ResponseEntity<Object> resultDelete = this.restTemplate.exchange(
                API_PATH + "/booking/" + result.getBody().getBookingId(),
                HttpMethod.DELETE,
                new HttpEntity<>(""),
                Object.class);

    }

}
