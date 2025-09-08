package alex.valker91.spring_boot.facade;

import alex.valker91.spring_boot.facade.impl.BookingFacadeImpl;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.UserAccount;
import alex.valker91.spring_boot.model.impl.EventImpl;
import alex.valker91.spring_boot.model.impl.TicketImpl;
import alex.valker91.spring_boot.model.impl.UserAccountImpl;
import alex.valker91.spring_boot.service.EventService;
import alex.valker91.spring_boot.service.TicketService;
import alex.valker91.spring_boot.service.UserAccountService;
import alex.valker91.spring_boot.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingFacadeImplTest {

    private final EventService eventService = mock(EventService.class);
    private final TicketService ticketService = mock(TicketService.class);
    private final UserService userService = mock(UserService.class);
    private final UserAccountService userAccountService = mock(UserAccountService.class);

    private final BookingFacadeImpl facade = new BookingFacadeImpl(eventService, ticketService, userService, userAccountService);

    @Test
    @DisplayName("bookTicket deducts amount and returns ticket when funds sufficient")
    void bookTicket_success() {
        Event event = new EventImpl(1L, "E", new java.util.Date(), 100);
        UserAccount account = new UserAccountImpl(10L, 5L, 150);
        Ticket ticket = new TicketImpl(99L, 5L, 1L, 1, Ticket.Category.STANDARD);

        when(eventService.getEventById(1L)).thenReturn(event);
        when(userAccountService.getUserAccountByUserId(5L)).thenReturn(account);
        when(ticketService.bookTicket(5L, 1L, 1, Ticket.Category.STANDARD)).thenReturn(ticket);

        Ticket result = facade.bookTicket(5L, 1L, 1, Ticket.Category.STANDARD);

        assertThat(result).isNotNull();
        verify(userAccountService).updateUserAccount(argThat(a -> a.getUserAmount() == 50));
        verify(ticketService).bookTicket(5L, 1L, 1, Ticket.Category.STANDARD);
    }

    @Test
    @DisplayName("bookTicket returns null when insufficient funds")
    void bookTicket_insufficientFunds() {
        Event event = new EventImpl(1L, "E", new java.util.Date(), 200);
        UserAccount account = new UserAccountImpl(10L, 5L, 150);

        when(eventService.getEventById(1L)).thenReturn(event);
        when(userAccountService.getUserAccountByUserId(5L)).thenReturn(account);

        Ticket result = facade.bookTicket(5L, 1L, 1, Ticket.Category.STANDARD);

        assertThat(result).isNull();
        verify(ticketService, never()).bookTicket(anyLong(), anyLong(), anyInt(), any());
    }

    @Test
    @DisplayName("cancelTicket refunds amount and cancels ticket")
    void cancelTicket_refundAndCancel() {
        Ticket ticket = new TicketImpl(99L, 5L, 1L, 1, Ticket.Category.PREMIUM);
        Event event = new EventImpl(1L, "E", new java.util.Date(), 120);

        when(ticketService.getById(99L)).thenReturn(ticket);
        when(eventService.getEventById(1L)).thenReturn(event);
        when(ticketService.cancelTicket(99L)).thenReturn(true);

        boolean result = facade.cancelTicket(99L);

        assertThat(result).isTrue();
        verify(userAccountService).refillUserAccount(5L, 120);
        verify(ticketService).cancelTicket(99L);
    }
}

