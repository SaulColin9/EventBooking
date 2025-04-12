package org.booking.service.event;

import org.booking.model.Event;
import org.booking.storage.dao.EventDao;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventServiceImpl implements EventService{
    private EventDao eventDao;

    @Override
    public Event getEventById(long id) {
        return eventDao.get(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventDao.getAll().stream()
                .filter(event -> event.getTitle().equals(title))
                .skip((long) (pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventDao.getAll().stream()
                .filter(event -> event.getDate().equals(day))
                .skip((long) (pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public Event createEvent(Event event) {
        return eventDao.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventDao.update(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        Event eventToDelete = eventDao.get(eventId).orElseThrow(IllegalArgumentException::new);
        return eventDao.delete(eventToDelete).isPresent();
    }
}
