package org.booking.facade;


import org.booking.model.*;
import org.booking.storage.dao.UserDao;
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

}