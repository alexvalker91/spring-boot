package alex.valker91.spring_boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Create user then fetch by email via REST with JWT scopes")
    void createThenFetchUser() throws Exception {
        String body = "{\"id\":3,\"name\":\"Bob\",\"email\":\"bob@example.com\"}";

        mockMvc.perform(post("/api/v1/users/create_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "edit_catalogue")).authorities(() -> "SCOPE_edit_catalogue")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)));

        mockMvc.perform(get("/api/v1/users/email").param("email", "bob@example.com")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "view_catalogue")).authorities(() -> "SCOPE_view_catalogue")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bob")))
                .andExpect(jsonPath("$.email", is("bob@example.com")));
    }
}

