package org.booking.service.ticket;

import org.booking.model.Event;
import org.booking.model.Ticket;
import org.booking.model.User;
import org.booking.storage.dao.TicketDao;

import java.util.List;

public interface TicketService {
    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);
    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);
    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);
    boolean cancelTicket(long ticketId);
}
