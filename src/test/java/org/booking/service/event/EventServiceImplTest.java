package org.booking.service.event;

import org.booking.model.Event;
import org.booking.model.EventImpl;
import org.booking.storage.dao.EventDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    private EventServiceImpl eventService;
    private EventDao eventDao;

    @BeforeEach
    void setUp() {
        eventDao = mock(EventDao.class);
        eventService = new EventServiceImpl();
        eventService.setEventDao(eventDao);
    }

    @Test
    void testGetEventById() {
        Event mockEvent = new EventImpl();
        mockEvent.setId(1L);
        when(eventDao.get(1L)).thenReturn(Optional.of(mockEvent));

        Event result = eventService.getEventById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(eventDao, times(1)).get(1L);
    }

    @Test
    void testGetEventByIdThrowsExceptionWhenNotFound() {
        when(eventDao.get(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEventById(2L))
                .isInstanceOf(IllegalArgumentException.class);

        verify(eventDao, times(1)).get(2L);
    }

    @Test
    void testGetEventsByTitle() {
        Event event1 = new EventImpl();
        event1.setId(1L);
        event1.setTitle("Concert");

        Event event2 = new EventImpl();
        event2.setId(2L);
        event2.setTitle("Concert");

        Event event3 = new EventImpl();
        event3.setId(3L);
        event3.setTitle("Workshop");

        when(eventDao.getAll()).thenReturn(Arrays.asList(event1, event2, event3));

        List<Event> result = eventService.getEventsByTitle("Concert", 10, 1);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Event::getId).containsExactly(1L, 2L);

        verify(eventDao, times(1)).getAll();
    }

    @Test
    void testGetEventsForDay() {
        Date targetDate = new Date();
        Event event1 = new EventImpl();
        event1.setId(1L);
        event1.setDate(targetDate);

        Event event2 = new EventImpl();
        event2.setId(2L);
        event2.setDate(targetDate);

        when(eventDao.getAll()).thenReturn(Arrays.asList(event1, event2));

        List<Event> result = eventService.getEventsForDay(targetDate, 10, 1);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Event::getId).containsExactly(1L, 2L);

        verify(eventDao, times(1)).getAll();
    }

    @Test
    void testCreateEvent() {
        Event event = new EventImpl();
        Event storedEvent = new EventImpl();
        storedEvent.setId(1L);
        when(eventDao.save(event)).thenReturn(storedEvent);

        Event result = eventService.createEvent(event);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(eventDao, times(1)).save(event);
    }

    @Test
    void testUpdateEvent() {
        Event existingEvent = new EventImpl();
        existingEvent.setId(1L);

        Event updatedEvent = new EventImpl();
        updatedEvent.setId(1L);
        updatedEvent.setTitle("Updated");

        when(eventDao.update(existingEvent)).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(existingEvent);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Updated");

        verify(eventDao, times(1)).update(existingEvent);
    }

    @Test
    void testDeleteEvent() {
        Event mockEvent = new EventImpl();
        mockEvent.setId(1L);
        when(eventDao.get(1L)).thenReturn(Optional.of(mockEvent));
        when(eventDao.delete(mockEvent)).thenReturn(Optional.of(mockEvent));

        boolean result = eventService.deleteEvent(1L);

        assertThat(result).isTrue();

        verify(eventDao, times(1)).get(1L);
        verify(eventDao, times(1)).delete(mockEvent);
    }

    @Test
    void testDeleteEventThrowsExceptionWhenNotFound() {
        when(eventDao.get(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.deleteEvent(2L))
                .isInstanceOf(IllegalArgumentException.class);

        verify(eventDao, times(1)).get(2L);
        verify(eventDao, times(0)).delete(any());
    }
}