package org.booking.service.ticket;

import org.booking.model.*;
import org.booking.storage.dao.TicketDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    private TicketServiceImpl ticketService;
    private TicketDao ticketDao;

    @BeforeEach
    void setUp() {
        ticketDao = mock(TicketDao.class);
        ticketService = new TicketServiceImpl();
        ticketService.setTicketDao(ticketDao);
    }

    @Test
    void testBookTicket() {
        Ticket ticket = new TicketImpl();
        ticket.setUserId(1L);
        ticket.setEventId(1L);
        ticket.setCategory(Ticket.Category.PREMIUM);
        ticket.setPlace(42);
        Ticket savedTicket = new TicketImpl();
        savedTicket.setId(1L);
        when(ticketDao.save(any())).thenReturn(savedTicket);

        Ticket result = ticketService.bookTicket(1L, 1L, 42, Ticket.Category.PREMIUM);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(ticketDao, times(1)).save(any());
    }

    @Test
    void testGetBookedTicketsByUser() {
        User user = new UserImpl();
        user.setId(1L);
        Ticket ticket1 = new TicketImpl();
        ticket1.setUserId(1L);
        Ticket ticket2 = new TicketImpl();
        ticket2.setUserId(1L);
        Ticket ticket3 = new TicketImpl();
        ticket3.setUserId(2L);
        when(ticketDao.getAll()).thenReturn(Arrays.asList(ticket1, ticket2, ticket3));

        List<Ticket> result = ticketService.getBookedTickets(user, 10, 1);

        assertThat(result).hasSize(2);
        assertThat(result).contains(ticket1, ticket2);

        verify(ticketDao, times(1)).getAll();
    }

    @Test
    void testGetBookedTicketsByEvent() {
        Event event = new EventImpl();
        event.setId(1L);
        Ticket ticket1 = new TicketImpl();
        ticket1.setEventId(1L);
        Ticket ticket2 = new TicketImpl();
        ticket2.setEventId(1L);
        Ticket ticket3 = new TicketImpl();
        ticket3.setEventId(2L);
        when(ticketDao.getAll()).thenReturn(Arrays.asList(ticket1, ticket2, ticket3));

        List<Ticket> result = ticketService.getBookedTickets(event, 10, 1);

        assertThat(result).hasSize(2);
        assertThat(result).contains(ticket1, ticket2);

        verify(ticketDao, times(1)).getAll();
    }

    @Test
    void testCancelTicket() {
        Ticket ticketToCancel = new TicketImpl();
        ticketToCancel.setId(1L);
        when(ticketDao.get(1L)).thenReturn(Optional.of(ticketToCancel));
        when(ticketDao.delete(ticketToCancel)).thenReturn(Optional.of(ticketToCancel));

        boolean result = ticketService.cancelTicket(1L);

        assertThat(result).isTrue();

        verify(ticketDao, times(1)).get(1L);
        verify(ticketDao, times(1)).delete(ticketToCancel);
    }

    @Test
    void testCancelTicketThrowsExceptionWhenNotFound() {
        when(ticketDao.get(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.cancelTicket(2L))
                .isInstanceOf(IllegalArgumentException.class);

        verify(ticketDao, times(1)).get(2L);
        verify(ticketDao, times(0)).delete(any());
    }
}