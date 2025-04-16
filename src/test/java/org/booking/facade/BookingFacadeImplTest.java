package org.booking.facade;


import org.booking.model.*;
import org.booking.storage.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("/test-beans.xml")
class BookingFacadeImplTest {
    @Autowired
    private BookingFacade bookingFacade;

    @BeforeEach
    void setUp() {
        assertThat(bookingFacade).isNotNull();
    }

    @Test
    void testCreateAndGetEvent() {
        Event event = new EventImpl();
        event.setId(5);
        event.setTitle("Spring Integration Test Workshop");
        event.setDate(new Date());

        Event createdEvent = bookingFacade.createEvent(event);

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
        bookingFacade.createEvent(event);

        event = new EventImpl();
        event.setTitle("Integration Event Exact Match");
        event.setDate(new Date());
        bookingFacade.createEvent(event);

        List<Event> events = bookingFacade.getEventsByTitle("Integration Event Exact Match", 10, 1);

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

}