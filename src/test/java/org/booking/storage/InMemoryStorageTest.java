package org.booking.storage;

import org.booking.model.Event;
import org.booking.model.EventImpl;
import org.booking.storage.id.IdGenerator;
import org.booking.storage.id.IncrementalIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryStorageTest {

    private InMemoryStorage inMemoryStorage;
    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        idGenerator = new IncrementalIdGenerator();
        inMemoryStorage = new InMemoryStorage();
        inMemoryStorage.setIdGenerator(idGenerator);
    }

    @Test
    void testGetEntities() {
        Event event1 = new EventImpl();
        Event event2 = new EventImpl();
        inMemoryStorage.add(event1);
        inMemoryStorage.add(event2);

        List<Event> retrievedEntities = inMemoryStorage.getEntities(Event.class);

        assertThat(retrievedEntities).hasSize(2).containsExactlyInAnyOrder(event1, event2);
    }

    @Test
    void testGetEntityById() {
        Event event = new EventImpl();
        inMemoryStorage.add(event);

        Event retrievedEvent = inMemoryStorage.get(1L, Event.class);

        assertThat(retrievedEvent).isNotNull().isEqualTo(event);
    }

    @Test
    void testGetEntityByIdWhenNotFound() {
        Event retrievedEvent = inMemoryStorage.get(1L, Event.class);

        assertThat(retrievedEvent).isNull();
    }

    @Test
    void testAddEntity() {
        Event event = new EventImpl();

        Event addedEvent = (Event) inMemoryStorage.add(event);

        assertThat(addedEvent.getId()).isEqualTo(1L);
    }

    @Test
    void testUpdateEntity() {
        Event event = new EventImpl();
        inMemoryStorage.add(event);

        Event updatedEvent = new EventImpl();
        updatedEvent.setId(1L);
        updatedEvent.setTitle("Updated Title");

        Event result = (Event) inMemoryStorage.update(updatedEvent);

        assertThat(result).isEqualTo(updatedEvent);
        Event retrievedEvent = inMemoryStorage.get(1L, Event.class);
        assertThat(retrievedEvent.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void testRemoveEntity() {
        Event event = new EventImpl();
        event.setId(1L);
        inMemoryStorage.add(event);

        Event removedEvent = (Event) inMemoryStorage.remove(event);

        assertThat(removedEvent).isEqualTo(event);
        Event retrievedEvent = inMemoryStorage.get(1L, Event.class);
        assertThat(retrievedEvent).isNull();
    }

}