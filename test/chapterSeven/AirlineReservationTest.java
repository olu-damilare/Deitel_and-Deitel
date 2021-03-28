package chapterSeven;

import chapterSeven.airlineReservation.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class AirlineReservationTest {
    Aeroplane aeroplane;
    NewAirline airline;
    FlightReservation reservation;
    FlightBooking flightBooking;

    @BeforeEach
    void setUp(){
        aeroplane = new Aeroplane("Airforce1");
        airline = new NewAirline("Dana Airline", aeroplane);
        reservation = new FlightReservation();
        flightBooking = new FlightBooking();
    }

    @AfterEach
    void tearDown(){
        aeroplane=null;
        airline=null;
        reservation = null;
        flightBooking = null;
        FlightReservation.emptyReservedPassengerList();
    }

    @Test
    void testThatAirlineCanBeCreated() {
        assertEquals("""
                NewAirline{
                Airline Name='Dana Airline'
                Number of aeroplanes='1'}""", airline.toString());
    }

    @Test
    void testThatAirlineCanHaveAeroplanes(){

        assertEquals(1, airline.getTotalNumberOfAeroplanes());
        Aeroplane secondAeroplane = new Aeroplane("Eagles");
        airline.acquireAeroplane(secondAeroplane);
        assertEquals(2, airline.getTotalNumberOfAeroplanes());
    }

   @Test
    void testThatAeroplaneHasFiftySeats(){
        assertEquals(50, aeroplane.getNumberOfSeatsInPlane());
   }
   @Test
    void testThatAirlineHasFlightDetails(){
        FlightDetails flightDetails = new FlightDetails();
        assertEquals("FlightDetails{\n" +
                "Number Of Passengers = 0\n" +
                "Host Details = null\n" +
                "Pilot Details = null\n" +
                "Passenger Info = null\n" +
                "Flight  Number = 0\n" +
                "}", flightDetails.toString());
   }
   @Test
    void testThatPassengerCanReserveFirstClassSeat(){
        Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        reservation.reserveFlight(passenger, SeatClass.FIRSTCLASS);
        assertTrue(FlightReservation.hasReserved(passenger));
        assertEquals(1, reservation.getReservationID(passenger));
        assertEquals(SeatClass.FIRSTCLASS, reservation.getPassengerReservedSeatType(passenger));
   }
    @Test
    void testThatPassengerCanReserveBusinessClassSeat(){
       Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        reservation.reserveFlight(passenger, SeatClass.BUSINESS);

        assertEquals(1, reservation.getReservationID(passenger));
        assertTrue(FlightReservation.hasReserved(passenger));
        assertEquals(SeatClass.BUSINESS, reservation.getPassengerReservedSeatType(passenger));
    }

    @Test
    void testThatPassengerCanReserveEconomyClassSeat(){
        Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
         reservation.reserveFlight(passenger, SeatClass.ECONOMY);

        assertEquals(1, reservation.getReservationID(passenger));
        assertTrue(FlightReservation.hasReserved(passenger));
        assertEquals(SeatClass.ECONOMY, reservation.getPassengerReservedSeatType(passenger));
    }

@Test
    void testThatMultiplePassengersCanReserveFirstClassSeats(){
    Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
    Passenger passenger1 = new Passenger("Olu Jola", "0000", "bina@jolo.com");
    reservation.reserveFlight(passenger, SeatClass.FIRSTCLASS);
    reservation.reserveFlight(passenger1, SeatClass.FIRSTCLASS);

    assertTrue(FlightReservation.hasReserved(passenger));
    assertTrue(FlightReservation.hasReserved(passenger1));
    assertEquals(1, reservation.getReservationID(passenger));
    assertEquals(2, reservation.getReservationID(passenger1));
    assertEquals(SeatClass.FIRSTCLASS, reservation.getPassengerReservedSeatType(passenger));
    assertEquals(SeatClass.FIRSTCLASS, reservation.getPassengerReservedSeatType(passenger1));

}
    @Test
    void testThatMultiplePassengersCanReserveBusinessClassSeats(){
        Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        Passenger passenger1 = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        reservation.reserveFlight(passenger, SeatClass.BUSINESS);
        reservation.reserveFlight(passenger1, SeatClass.BUSINESS);

        assertTrue(reservation.hasReserved(passenger));
        assertTrue(reservation.hasReserved(passenger1));
        assertEquals(SeatClass.BUSINESS, reservation.getPassengerReservedSeatType(passenger));
        assertEquals(SeatClass.BUSINESS, reservation.getPassengerReservedSeatType(passenger1));

    }
    @Test
    void testThatMultiplePassengersCanReserveEconomyClassSeats(){
        Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        Passenger passenger1 = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        reservation.reserveFlight(passenger, SeatClass.ECONOMY);
        assertTrue(FlightReservation.hasReserved(passenger));
        assertEquals(1, reservation.getReservationID(passenger));
        assertEquals(SeatClass.ECONOMY, reservation.getPassengerReservedSeatType(passenger));

        reservation.reserveFlight(passenger1, SeatClass.ECONOMY);
        assertTrue(FlightReservation.hasReserved(passenger1));
        assertEquals(2, reservation.getReservationID(passenger1));
        assertEquals(SeatClass.ECONOMY,reservation.getPassengerReservedSeatType(passenger1));

    }


    @Test
    void testThatPassengerCanCancelReservation(){
        Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        reservation.reserveFlight(passenger, SeatClass.BUSINESS);
        assertTrue(FlightReservation.hasReserved(passenger));
        assertEquals(1, reservation.getReservationID(passenger));
        assertEquals(SeatClass.BUSINESS, reservation.getPassengerReservedSeatType(passenger));

        reservation.cancelReservation(passenger);
        assertFalse(FlightReservation.hasReserved(passenger));
        assertEquals(0, reservation.getReservationID(passenger));
        assertNull(reservation.getPassengerReservedSeatType(passenger));
    }

    @Test
    void testThatPassengerCanBookFirstClassSeatWithReservationID(){
        Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        reservation.reserveFlight(passenger, SeatClass.FIRSTCLASS);
        assertTrue(FlightReservation.hasReserved(passenger));
        assertEquals(1, reservation.getReservationID(passenger));

        FlightBooking.bookFlight(reservation.getReservationID(passenger));
        assertEquals(1, flightBooking.getTotalNumberOfFirstClassSeatsBooked());
        assertEquals(SeatClass.FIRSTCLASS, FlightBooking.getPassengerBookedSeatType(passenger));

        assertTrue(FlightBooking.hasBooked(reservation.getReservationID(passenger)));

    }

    @Test
    void testThatPassengerCanBookBusinessClassSeatWithReservationID(){
        Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        reservation.reserveFlight(passenger, SeatClass.BUSINESS);
        assertTrue(FlightReservation.hasReserved(passenger));
        assertEquals(1, reservation.getReservationID(passenger));

        FlightBooking.bookFlight(reservation.getReservationID(passenger));
        assertEquals(1, flightBooking.getTotalNumberOfBusinessClassSeatsBooked());
        assertEquals(SeatClass.BUSINESS, FlightBooking.getPassengerBookedSeatType(passenger));
        assertTrue(FlightBooking.hasBooked(passenger.getSeatNumber()));

    }

    @Test
    void testThatPassengerCanBookEconomyClassSeatWithReservationID(){
        Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        reservation.reserveFlight(passenger, SeatClass.ECONOMY);
        assertTrue(FlightReservation.hasReserved(passenger));
        assertEquals(1, reservation.getReservationID(passenger));

        FlightBooking.bookFlight(reservation.getReservationID(passenger));
        assertEquals(1, flightBooking.getTotalNumberOfEconomyClassSeatsBooked());
        assertEquals(SeatClass.ECONOMY, FlightBooking.getPassengerBookedSeatType(passenger));
        assertTrue(FlightBooking.hasBooked(passenger.getSeatNumber()));

    }
    @Test
    void testThatSameReservationIDCannotBeUsedToBookAFlightTwice(){
        Passenger passenger = new Passenger("Olu Jola", "0000", "bina@jolo.com");
        reservation.reserveFlight(passenger, SeatClass.ECONOMY);
        assertTrue(FlightReservation.hasReserved(passenger));
        assertEquals(1, reservation.getReservationID(passenger));

        FlightBooking.bookFlight(reservation.getReservationID(passenger));
        assertEquals(1, flightBooking.getTotalNumberOfEconomyClassSeatsBooked());
        assertEquals(SeatClass.ECONOMY, FlightBooking.getPassengerBookedSeatType(passenger));
        assertTrue(FlightBooking.hasBooked(passenger.getSeatNumber()));

        FlightBooking.bookFlight(reservation.getReservationID(passenger));
        assertEquals(1, flightBooking.getTotalNumberOfEconomyClassSeatsBooked());

    }




}
