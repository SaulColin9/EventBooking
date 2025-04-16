package org.booking;

import org.booking.model.Event;
import org.booking.model.EventImpl;
import org.booking.model.User;
import org.booking.model.UserImpl;
import org.booking.service.event.EventService;
import org.booking.service.event.EventServiceImpl;
import org.booking.service.ticket.TicketService;
import org.booking.service.user.UserService;
import org.booking.service.user.UserServiceImpl;
import org.booking.storage.InMemoryStorage;
import org.booking.storage.Storage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class App
{
    public static void main( String[] args )
    {
//        User user = new UserImpl();
//        user.setName("Saul");
//        user.setId(1);
//        user.setEmail("saul@gmail.com");
//        Storage storage = new InMemoryStorage();
//        storage.add(user);
//        Event event = new EventImpl();
//        event.setDate(new Date());
//        event.setTitle("My event");
//        event.setId(1);
//        storage.add(event);
//        System.out.println(storage.getEntities(User.class));
//        System.out.println(storage.getEntities(Event.class));
//        user.setName("Naomi");
//        storage.update(user);
//        System.out.println(storage.getEntities(User.class));
//        storage.remove(user);
//        System.out.println(storage.getEntities(User.class));
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring/beans.xml");
        EventService eventService = (EventService) context.getBean("eventService");
        UserService userService = (UserService) context.getBean("userService");
        TicketService ticketService = (TicketService) context.getBean("ticketService");
        Event event = new EventImpl();
        event.setId(1);
        event.setDate(new Date());
        event.setTitle("Hello");
        eventService.createEvent(event);
        System.out.println(eventService.getEventsByTitle("Hello", 1,1));
    }
}
