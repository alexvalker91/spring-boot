package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventRestController {

    private final BookingFacade bookingFacade;

    /*
    Example: GET
    localhost:8081/api/v1/events/1
     */
    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable long eventId) {
        return bookingFacade.getEventById(eventId);
    }

    /*
    Example: GET
    localhost:8081/api/v1/events/title?title=New%20Title
     */
    @GetMapping("/title")
    private List<Event> getEventsByTitle(
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "pageNum", defaultValue = "0", required = false) int pageNum
    ) {
        return bookingFacade.getEventsByTitle(title, pageSize, pageNum);
    }

    /*
    Example: GET
    localhost:8081/api/v1/events/day?day=2023-10-05%2017:00:00
     */
    @GetMapping("/day")
    private List<Event> getEventsForDay(
            @RequestParam(name = "day", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date day,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "pageNum", defaultValue = "0", required = false) int pageNum
    ) {
        return bookingFacade.getEventsForDay(day, pageSize, pageNum);
    }

    /*
    Example: POST
    localhost:8081/api/v1/events/create_event
{
    "id": 2,
    "title": "Some title",
    "date": "2023-10-05 17:50:00",
    "ticketPrice": 200
}
     */
    @PostMapping("/create_event")
    public Event createEvent(@RequestBody Event event) {
        return bookingFacade.createEvent(event);
    }

    /*
    Example: PUT
    localhost:8081/api/v1/events/update_event
{
    "id": 2,
    "title": "Some title New",
    "date": "2023-10-05 17:50:00",
    "ticketPrice": 200
}
     */
    @PutMapping("/update_event")
    public Event updateEvent(@RequestBody Event event) {
        return bookingFacade.updateEvent(event);
    }

    /*
    Example: DELETE
    localhost:8081/api/v1/events/delete_event/2
     */
    @DeleteMapping("delete_event/{eventId}")
    public void deleteEvent(@PathVariable("eventId") long eventId) {
        bookingFacade.deleteEvent(eventId);
    }
}
