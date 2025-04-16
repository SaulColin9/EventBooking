package org.booking.storage.dao;

import org.booking.model.Ticket;
import org.booking.model.TicketImpl;
import org.booking.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TicketDaoTest {

    private TicketDao ticketDao;
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);

        ticketDao = new TicketDao();
        ticketDao.setStorage(storage);
    }

    @Test
    void testGetById() {
        Ticket mockTicket = new TicketImpl();
        mockTicket.setId(1L);
        mockTicket.setPlace(42);
        when(storage.get(1L, Ticket.class)).thenReturn(mockTicket);

        Optional<Ticket> result = ticketDao.get(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getPlace()).isEqualTo(42);

        verify(storage, times(1)).get(1L, Ticket.class);
    }

    @Test
    void testGetByIdWhenNotFound() {
        when(storage.get(5L, Ticket.class)).thenReturn(null);

        Optional<Ticket> result = ticketDao.get(5L);

        assertThat(result).isNotPresent();

        verify(storage, times(1)).get(5L, Ticket.class);
    }

    @Test
    void testGetAll() {
        Ticket ticket1 = new TicketImpl();
        ticket1.setId(1L);
        Ticket ticket2 = new TicketImpl();
        ticket2.setId(2L);
        List<Ticket> mockTickets = Arrays.asList(ticket1, ticket2);
        when(storage.getEntities(Ticket.class)).thenReturn(mockTickets);

        List<Ticket> result = ticketDao.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(ticket1, ticket2);

        verify(storage, times(1)).getEntities(Ticket.class);
    }

    @Test
    void testSave() {
        Ticket ticketToSave = new TicketImpl();
        ticketToSave.setPlace(45);
        ticketToSave.setCategory(Ticket.Category.PREMIUM);
        Ticket storedTicket = new TicketImpl();
        storedTicket.setId(1L);
        storedTicket.setPlace(ticketToSave.getPlace());
        storedTicket.setCategory(ticketToSave.getCategory());
        when(storage.add(ticketToSave)).thenReturn(storedTicket);

        Ticket result = ticketDao.save(ticketToSave);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPlace()).isEqualTo(45);
        assertThat(result.getCategory()).isEqualTo(Ticket.Category.PREMIUM);

        verify(storage, times(1)).add(ticketToSave);
    }

    @Test
    void testUpdate() {
        Ticket existingTicket = new TicketImpl();
        existingTicket.setId(1L);
        existingTicket.setPlace(20);

        Ticket updatedTicket = new TicketImpl();
        updatedTicket.setId(1L);
        updatedTicket.setPlace(25);

        when(storage.update(existingTicket)).thenReturn(updatedTicket);

        Ticket result = ticketDao.update(existingTicket);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPlace()).isEqualTo(25);

        verify(storage, times(1)).update(existingTicket);
    }

    @Test
    void testDelete() {
        Ticket mockTicket = new TicketImpl();
        mockTicket.setId(1L);
        when(storage.remove(mockTicket)).thenReturn(mockTicket);

        Optional<Ticket> result = ticketDao.delete(mockTicket);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);

        verify(storage, times(1)).remove(mockTicket);
    }

    @Test
    void testDeleteWhenNotFound() {
        Ticket mockTicket = new TicketImpl();
        mockTicket.setId(1L);
        when(storage.remove(mockTicket)).thenReturn(null);

        Optional<Ticket> result = ticketDao.delete(mockTicket);

        assertThat(result).isNotPresent();

        verify(storage, times(1)).remove(mockTicket);
    }
}