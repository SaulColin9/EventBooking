package org.booking.service.event;

import org.booking.model.Event;
import org.booking.storage.dao.EventDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService{
    private EventDao eventDao;
    private final static Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Override
    public Event getEventById(long id) {
        logger.info("Getting event with id of {}", id);
        return eventDao.get(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        logger.info("Getting events with title of {}", title);
        return eventDao.getAll().stream()
                .filter(event -> event.getTitle().equals(title))
                .skip((long) (pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        logger.info("Getting events for {} date", day);
        return eventDao.getAll().stream()
                .filter(event -> event.getDate().equals(day))
                .skip((long) (pageNum - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public Event createEvent(Event event) {
        logger.info("Creating event");
        return eventDao.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        logger.info("Updating event");
        return eventDao.update(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        logger.info("Deleting event");
        Event eventToDelete = eventDao.get(eventId).orElseThrow(IllegalArgumentException::new);
        return eventDao.delete(eventToDelete).isPresent();
    }

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
