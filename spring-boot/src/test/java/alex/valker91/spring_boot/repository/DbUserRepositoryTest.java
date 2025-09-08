package alex.valker91.spring_boot.repository;

import alex.valker91.spring_boot.entity.UserDb;
import alex.valker91.spring_boot.facade.BookingFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DataJpaTest
@ActiveProfiles("test")
class DbUserRepositoryTest {

    @TestConfiguration
    static class StubConfig {
        @Bean
        BookingFacade bookingFacade() {
            return mock(BookingFacade.class);
        }
    }

    @Autowired
    private DbUserRepository dbUserRepository;

    @Test
    @DisplayName("findByEmail returns user when present")
    void findByEmail_found() {
        UserDb saved = dbUserRepository.save(new UserDb(null, "Alex", "alex@example.com"));

        Optional<UserDb> found = dbUserRepository.findByEmail("alex@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull();
        assertThat(found.get().getName()).isEqualTo("Alex");
    }

    @Test
    @DisplayName("findByEmail returns empty when not present")
    void findByEmail_notFound() {
        Optional<UserDb> found = dbUserRepository.findByEmail("missing@example.com");
        assertThat(found).isNotPresent();
    }
}

