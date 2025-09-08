package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.config.AppConfig;
import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Event;
import alex.valker91.spring_boot.model.impl.EventImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventRestController.class)
@Import(AppConfig.class)
@ActiveProfiles("test")
class EventRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingFacade bookingFacade;

    @Test
    @DisplayName("GET /events/{id} requires view scope and returns event")
    void getEventById_authorized() throws Exception {
        Event event = new EventImpl(1L, "E1", new Date(), 100);
        when(bookingFacade.getEventById(1L)).thenReturn(event);

        mockMvc.perform(get("/api/v1/events/1").with(jwt().jwt(jwt -> jwt.claim("scope", "view_catalogue")).authorities(() -> "SCOPE_view_catalogue")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("E1")));
    }
}

