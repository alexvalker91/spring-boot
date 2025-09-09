package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.config.AppConfig;
import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Ticket;
import alex.valker91.spring_boot.model.impl.TicketImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TicketRestController.class)
@Import(AppConfig.class)
@ActiveProfiles("test")
class TicketRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingFacade bookingFacade;

    @Test
    @DisplayName("POST /tickets/book_ticket requires edit scope and returns ticket")
    void bookTicketAuthorized() throws Exception {
        Ticket t = new TicketImpl(10L, 5L, 1L, 1, Ticket.Category.STANDARD);
        when(bookingFacade.bookTicket(5L, 1L, 1, Ticket.Category.STANDARD)).thenReturn(t);

        mockMvc.perform(post("/api/v1/tickets/book_ticket")
                        .param("userId", "5")
                        .param("eventId", "1")
                        .param("place", "1")
                        .param("category", "STANDARD")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "edit_catalogue")).authorities(() -> "SCOPE_edit_catalogue")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)));
    }
}
