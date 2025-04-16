package org.booking.service.ticket;

import org.booking.model.Event;
import org.booking.model.Ticket;
import org.booking.model.TicketImpl;
import org.booking.model.User;
import org.booking.storage.dao.TicketDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService{
    private TicketDao ticketDao;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        Ticket ticket = new TicketImpl();
        ticket.setUserId(userId);
        ticket.setCategory(category);
        ticket.setEventId(eventId);
        ticket.setPlace(place);
        return ticketDao.save(ticket);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketDao.getAll().stream()
                .filter(ticket -> ticket.getUserId() == user.getId())
                .skip((long) (pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return ticketDao.getAll().stream()
                .filter(ticket -> ticket.getUserId() == event.getId())
                .skip((long) (pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());    }

    @Override
    public boolean cancelTicket(long ticketId) {
        Ticket ticketToDelete = ticketDao.get(ticketId).orElseThrow(IllegalArgumentException::new);
        return ticketDao.delete(ticketToDelete).isPresent();
    }

    @Autowired
    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }
}
