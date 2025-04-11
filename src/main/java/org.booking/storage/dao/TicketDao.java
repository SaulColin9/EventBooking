package org.booking.storage.dao;

import org.booking.model.Ticket;
import org.booking.storage.Storage;

import java.util.List;
import java.util.Optional;

public class TicketDao implements Dao<Ticket> {
    private Storage storage;
    @Override
    public Optional<Ticket> get(long id) {
        return Optional.of(storage.get(id, Ticket.class));
    }

    @Override
    public List<Ticket> getAll() {
        return storage.getEntities(Ticket.class);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return (Ticket) storage.add(ticket);
    }

    @Override
    public Ticket update(Ticket ticket) {
        return (Ticket) storage.update(ticket);
    }

    @Override
    public Optional<Ticket> delete(Ticket ticket) {
        return Optional.of((Ticket) storage.remove(ticket));
    }

    public void setStorage(Storage storage){
        this.storage = storage;
    }
}
