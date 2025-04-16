package org.booking.storage.dao;

import org.booking.model.Event;
import org.booking.model.EventImpl;
import org.booking.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EventDaoTest {

    private EventDao eventDao;
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = mock(Storage.class);

        eventDao = new EventDao();
        eventDao.setStorage(storage);
    }

    @Test
    void testGetById() {
        Event mockEvent = new EventImpl();
        mockEvent.setId(1L);
        when(storage.get(1L, Event.class)).thenReturn(mockEvent);

        Optional<Event> result = eventDao.get(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);

        verify(storage, times(1)).get(1L, Event.class);
    }

    @Test
    void testGetByIdWhenNotFound() {
        when(storage.get(5L, Event.class)).thenReturn(null);

        Optional<Event> result = eventDao.get(5L);

        assertThat(result).isNotPresent();

        verify(storage, times(1)).get(5L, Event.class);
    }

    @Test
    void testGetAll() {
        Event event1 = new EventImpl();
        event1.setId(1L);
        Event event2 = new EventImpl();
        event2.setId(2L);
        List<Event> mockEvents = Arrays.asList(event1, event2);
        when(storage.getEntities(Event.class)).thenReturn(mockEvents);

        List<Event> result = eventDao.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(event1, event2);

        verify(storage, times(1)).getEntities(Event.class);
    }

    @Test
    void testSave() {
        Event eventToSave = new EventImpl();
        eventToSave.setTitle("Spring Workshop");
        Event storedEvent = new EventImpl();
        storedEvent.setId(1L);
        storedEvent.setTitle(eventToSave.getTitle());
        when(storage.add(eventToSave)).thenReturn(storedEvent);

        Event result = eventDao.save(eventToSave);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Spring Workshop");

        verify(storage, times(1)).add(eventToSave);
    }

    @Test
    void testUpdate() {
        Event existingEvent = new EventImpl();
        existingEvent.setId(1L);
        existingEvent.setTitle("Old Title");

        Event updatedEvent = new EventImpl();
        updatedEvent.setId(1L);
        updatedEvent.setTitle("New Title");

        when(storage.update(existingEvent)).thenReturn(updatedEvent);

        Event result = eventDao.update(existingEvent);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New Title");

        verify(storage, times(1)).update(existingEvent);
    }

    @Test
    void testDelete() {
        Event mockEvent = new EventImpl();
        mockEvent.setId(1L);
        when(storage.remove(mockEvent)).thenReturn(mockEvent);

        Optional<Event> result = eventDao.delete(mockEvent);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);

        verify(storage, times(1)).remove(mockEvent);
    }

    @Test
    void testDeleteWhenNotFound() {
        Event mockEvent = new EventImpl();
        mockEvent.setId(1L);
        when(storage.remove(mockEvent)).thenReturn(null);

        Optional<Event> result = eventDao.delete(mockEvent);

        assertThat(result).isNotPresent();

        verify(storage, times(1)).remove(mockEvent);
    }
}