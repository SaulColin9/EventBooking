package org.booking.facade;

import org.booking.model.*;
import org.booking.storage.id.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-beans.xml")
class BookingFacadeImplTest {
    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        assertThat(bookingFacade).isNotNull();
        idGenerator.reset();
    }

    @Test
    void testCreateAndGetEvent() {
        Event event = new EventImpl();
        event.setTitle("Spring Integration Test Workshop");
        event.setDate(new Date());

        Event createdEvent = bookingFacade.createEvent(event);

        assertThat(createdEvent.getId()).isEqualTo(1);
        assertThat("Spring Integration Test Workshop").isEqualTo(createdEvent.getTitle());

        Event fetchedEvent = bookingFacade.getEventById(createdEvent.getId());
        assertThat(fetchedEvent).isNotNull();

        assertThat(fetchedEvent.getTitle()).isEqualTo("Spring Integration Test Workshop");
    }

    @Test
    void testGetEventsByTitle() {
        Event event = new EventImpl();
        event.setTitle("Integration Event 1");
        event.setDate(new Date());
        Event createdEvent1 = bookingFacade.createEvent(event);
        assertThat(createdEvent1.getId()).isEqualTo(1);


        event = new EventImpl();
        event.setTitle("Integration Event Exact Match");
        event.setDate(new Date());
        Event createdEvent2 = bookingFacade.createEvent(event);

        List<Event> events = bookingFacade.getEventsByTitle("Integration Event Exact Match", 10, 1);

        assertThat(createdEvent2.getId()).isEqualTo(2);
        assertThat(events.size()).isEqualTo(1);
        assertThat(events.get(0).getTitle()).isEqualTo("Integration Event Exact Match");
    }

    @Test
    void testBookTicketAndCancel() {
        User user = new UserImpl();
        user.setName("Bob Smith");
        user.setEmail("bobsmith@example.com");
        User createdUser = bookingFacade.createUser(user);

        Event event = new EventImpl();
        event.setTitle("Java Conference");
        event.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(event);

        Ticket ticket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 20, Ticket.Category.PREMIUM);

        assertThat(ticket.getCategory()).isEqualTo(Ticket.Category.PREMIUM);
        assertThat(ticket.getPlace()).isEqualTo(20);

        boolean isCancelled = bookingFacade.cancelTicket(ticket.getId());

        assertThat(isCancelled).isTrue();
    }

    @Test
    void testCreateAndGetUser() {
        User user = new UserImpl();
        user.setName("Alice Johnson");
        user.setEmail("alice@example.com");

        User createdUser = bookingFacade.createUser(user);

        assertThat(createdUser.getId()).isEqualTo(1);
        assertThat(createdUser.getName()).isEqualTo("Alice Johnson");
        assertThat(createdUser.getEmail()).isEqualTo("alice@example.com");

        User fetchedUser = bookingFacade.getUserById(createdUser.getId());
        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getName()).isEqualTo("Alice Johnson");
    }

    @Test
    void testDeleteEvent() {
        Event event = new EventImpl();
        event.setTitle("Test Event for Deletion");
        event.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(event);

        Event fetchedEvent = bookingFacade.getEventById(createdEvent.getId());
        assertThat(fetchedEvent).isNotNull();

        boolean isDeleted = bookingFacade.deleteEvent(createdEvent.getId());

        assertThat(isDeleted).isTrue();
        assertThatThrownBy(()->bookingFacade.getEventById(createdEvent.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testGetEventsForDay() {
        Date targetDate = new Date(2023, 11, 15); // Target date
        Event event1 = new EventImpl();
        event1.setTitle("Morning Concert");
        event1.setDate(targetDate);
        bookingFacade.createEvent(event1);

        Event event2 = new EventImpl();
        event2.setTitle("Evening Gala");
        event2.setDate(targetDate);
        bookingFacade.createEvent(event2);

        Event event3 = new EventImpl();
        event3.setTitle("Another Day Event");
        event3.setDate(new Date(2023, 11, 16));
        bookingFacade.createEvent(event3);

        List<Event> events = bookingFacade.getEventsForDay(targetDate, 10, 1);

        assertThat(events.size()).isEqualTo(2);
        assertThat(events).extracting(Event::getTitle)
                .containsExactlyInAnyOrder("Morning Concert", "Evening Gala");
    }

    @Test
    void testGetUserByEmail() {
        User user = new UserImpl();
        user.setName("Alice Johnson");
        user.setEmail("alice.johnson@example.com");
        bookingFacade.createUser(user);

        User fetchedUser = bookingFacade.getUserByEmail("alice.johnson@example.com");

        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getName()).isEqualTo("Alice Johnson");
        assertThat(fetchedUser.getEmail()).isEqualTo("alice.johnson@example.com");
    }

    @Test
    void testUpdateUser() {
        User user = new UserImpl();
        user.setName("Alice Johnson");
        user.setEmail("alice.johnson@example.com");
        User createdUser = bookingFacade.createUser(user);

        assertThat(createdUser.getEmail()).isEqualTo("alice.johnson@example.com");

        createdUser.setEmail("alice.new@example.com");
        User updatedUser = bookingFacade.updateUser(createdUser);

        assertThat(updatedUser.getEmail()).isEqualTo("alice.new@example.com");

        User fetchedUser = bookingFacade.getUserById(updatedUser.getId());
        assertThat(fetchedUser.getEmail()).isEqualTo("alice.new@example.com");
    }

    @Test
    void testGetUsersByName() {
        User user1 = new UserImpl();
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        bookingFacade.createUser(user1);

        User user2 = new UserImpl();
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        bookingFacade.createUser(user2);

        User user3 = new UserImpl();
        user3.setName("Alice");
        user3.setEmail("alice.another@example.com");
        bookingFacade.createUser(user3);

        List<User> users = bookingFacade.getUsersByName("Alice", 10, 1);

        assertThat(users.size()).isEqualTo(2);
        assertThat(users).extracting(User::getEmail)
                .containsExactlyInAnyOrder("alice@example.com", "alice.another@example.com");
    }

    @Test
    void testGetBookedTicketsByUser() {
        User user = new UserImpl();
        user.setName("Alice Johnson");
        user.setEmail("alice.johnson@example.com");
        User createdUser = bookingFacade.createUser(user);

        Event event1 = new EventImpl();
        event1.setTitle("Spring Boot Training");
        event1.setDate(new Date());
        Event createdEvent1 = bookingFacade.createEvent(event1);

        Event event2 = new EventImpl();
        event2.setTitle("JUnit Workshop");
        event2.setDate(new Date());
        Event createdEvent2 = bookingFacade.createEvent(event2);

        bookingFacade.bookTicket(createdUser.getId(), createdEvent1.getId(), 15, Ticket.Category.STANDARD);
        bookingFacade.bookTicket(createdUser.getId(), createdEvent2.getId(), 25, Ticket.Category.PREMIUM);

        List<Ticket> bookedTickets = bookingFacade.getBookedTickets(createdUser, 10, 1);

        assertThat(bookedTickets.size()).isEqualTo(2);
        assertThat(bookedTickets).extracting(Ticket::getCategory)
                .containsExactlyInAnyOrder(Ticket.Category.STANDARD, Ticket.Category.PREMIUM);
    }
}