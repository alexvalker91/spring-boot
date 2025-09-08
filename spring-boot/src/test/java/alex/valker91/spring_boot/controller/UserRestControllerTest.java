package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.User;
import alex.valker91.spring_boot.model.impl.UserImpl;
import alex.valker91.spring_boot.config.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserRestController.class)
@Import(AppConfig.class)
@ActiveProfiles("test")
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingFacade bookingFacade;

    @Test
    @DisplayName("GET /users/{id} requires view scope and returns user")
    void getUserById_authorized() throws Exception {
        User user = new UserImpl(1L, "Alex", "a@b.com");
        when(bookingFacade.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/1").with(jwt().jwt(jwt -> jwt.claim("scope", "view_catalogue")).authorities(() -> "SCOPE_view_catalogue")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Alex")))
                .andExpect(jsonPath("$.email", is("a@b.com")));
    }

    @Test
    @DisplayName("GET /users/{id} without scope is forbidden")
    void getUserById_forbidden() throws Exception {
        when(bookingFacade.getUserById(1L)).thenReturn(new UserImpl(1L, "Alex", "a@b.com"));

        mockMvc.perform(get("/api/v1/users/1").with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /users/create_user requires edit scope")
    void createUser_requiresEditScope() throws Exception {
        User user = new UserImpl(2L, "Jane", "j@b.com");
        when(bookingFacade.createUser(user)).thenReturn(user);

        String body = "{\"id\":2,\"name\":\"Jane\",\"email\":\"j@b.com\"}";

        mockMvc.perform(post("/api/v1/users/create_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "edit_catalogue")).authorities(() -> "SCOPE_edit_catalogue")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Jane")));

        mockMvc.perform(post("/api/v1/users/create_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(jwt()))
                .andExpect(status().isForbidden());
    }
}

