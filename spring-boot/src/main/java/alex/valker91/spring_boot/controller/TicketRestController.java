package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketRestController {

    private final BookingFacade bookingFacade;

    /*
    Example: POST
    localhost:8081/api/v1/tickets/book_ticket?userId=2&eventId=1&place=2&category=STANDARD
     */
    @PostMapping("/book_ticket")
    public Ticket bookTicket(
            @RequestParam(name = "userId", required = true) long userId,
            @RequestParam(name = "eventId", required = true) long eventId,
            @RequestParam(name = "place", required = true) int place,
            @RequestParam(name = "category", required = true) Ticket.Category category
    ) {
        return bookingFacade.bookTicket(userId, eventId, place, category);
    }


    /*
    Example: GET
    localhost:8081/api/v1/tickets/booked_tickets_by_user
{
    "id": 2,
    "name": "alex",
    "email": "alex@gmail.com"
}
     */
    @GetMapping("/booked_tickets_by_user")
    public List<Ticket> getBookedTicketsByUser(
            @RequestBody User user,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNum) {
        return bookingFacade.getBookedTickets(user, pageSize, pageNum);
    }

    /*
    Example: GET
    localhost:8081/api/v1/tickets/booked_tickets_by_event
{
    "id": 1,
    "title": "New Title Test",
    "date": "2023-10-05 18:00:00.000",
    "ticketPrice": 100
}
     */
    @GetMapping("/booked_tickets_by_event")
    public List<Ticket> getBookedTicketsByEvent(
            @RequestBody Event event,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNum) {
        return bookingFacade.getBookedTickets(event, pageSize, pageNum);
    }

    //    boolean cancelTicket(long ticketId);
    /*
    Example: DELETE
    localhost:8081/api/v1/users/delete_user/1
     */
    @DeleteMapping("cancel_ticket/{ticketId}")
    public void cancelTicket(@PathVariable("ticketId") long ticketId) {
        bookingFacade.cancelTicket(ticketId);
    }
}
