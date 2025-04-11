package org.booking;

import org.booking.model.Event;
import org.booking.model.EventImpl;
import org.booking.model.User;
import org.booking.model.UserImpl;
import org.booking.storage.InMemoryStorage;
import org.booking.storage.Storage;

import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        User user = new UserImpl();
        user.setName("Saul");
        user.setId(1);
        user.setEmail("saul@gmail.com");
        Storage storage = new InMemoryStorage();
        storage.add(user);
        Event event = new EventImpl();
        event.setDate(new Date());
        event.setTitle("My event");
        event.setId(1);
        storage.add(event);
        System.out.println(storage.getEntities(User.class));
        System.out.println(storage.getEntities(Event.class));
        user.setName("Naomi");
        storage.update(user);
        System.out.println(storage.getEntities(User.class));
        storage.remove(user);
        System.out.println(storage.getEntities(User.class));

    }
}
