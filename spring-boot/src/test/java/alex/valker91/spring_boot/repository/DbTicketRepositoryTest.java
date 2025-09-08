package alex.valker91.spring_boot.repository;

import alex.valker91.spring_boot.entity.TicketDb;
import alex.valker91.spring_boot.facade.BookingFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DataJpaTest
@ActiveProfiles("test")
class DbTicketRepositoryTest {

    @TestConfiguration
    static class StubConfig {
        @Bean
        BookingFacade bookingFacade() {
            return mock(BookingFacade.class);
        }
    }

    @Autowired
    private DbTicketRepository dbTicketRepository;

    @Test
    @DisplayName("existsByEventIdAndPlace detects duplicates")
    void existsByEventIdAndPlace() {
        TicketDb t = new TicketDb();
        t.setEventId(10L);
        t.setUserId(20L);
        t.setCategory(TicketDb.Category.STANDARD);
        t.setPlace(1);
        TicketDb saved = dbTicketRepository.save(t);

        boolean exists = dbTicketRepository.existsByEventIdAndPlace(10L, 1);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("findAllByUserId returns user tickets")
    void findAllByUserId() {
        TicketDb t1 = new TicketDb();
        t1.setEventId(10L);
        t1.setUserId(99L);
        t1.setCategory(TicketDb.Category.STANDARD);
        t1.setPlace(1);
        dbTicketRepository.save(t1);

        TicketDb t2 = new TicketDb();
        t2.setEventId(11L);
        t2.setUserId(99L);
        t2.setCategory(TicketDb.Category.PREMIUM);
        t2.setPlace(2);
        dbTicketRepository.save(t2);

        TicketDb t3 = new TicketDb();
        t3.setEventId(11L);
        t3.setUserId(100L);
        t3.setCategory(TicketDb.Category.PREMIUM);
        t3.setPlace(3);
        dbTicketRepository.save(t3);

        Page<TicketDb> page = dbTicketRepository.findAllByUserId(99L, PageRequest.of(0, 10));
        assertThat(page.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("findAllByEventId returns event tickets")
    void findAllByEventId() {
        TicketDb e1 = new TicketDb();
        e1.setEventId(10L);
        e1.setUserId(99L);
        e1.setCategory(TicketDb.Category.STANDARD);
        e1.setPlace(1);
        dbTicketRepository.save(e1);

        TicketDb e2 = new TicketDb();
        e2.setEventId(10L);
        e2.setUserId(100L);
        e2.setCategory(TicketDb.Category.BAR);
        e2.setPlace(2);
        dbTicketRepository.save(e2);

        TicketDb e3 = new TicketDb();
        e3.setEventId(11L);
        e3.setUserId(100L);
        e3.setCategory(TicketDb.Category.PREMIUM);
        e3.setPlace(3);
        dbTicketRepository.save(e3);

        Page<TicketDb> page = dbTicketRepository.findAllByEventId(10L, PageRequest.of(0, 10));
        assertThat(page.getContent()).hasSize(2);
    }
}

