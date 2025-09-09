package alex.valker91.spring_boot.repository;

import alex.valker91.spring_boot.entity.EventDb;
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

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DataJpaTest
@ActiveProfiles("test")
class DbEventRepositoryTest {

    @TestConfiguration
    static class StubConfig {
        @Bean
        BookingFacade bookingFacade() {
            return mock(BookingFacade.class);
        }
    }

    @Autowired
    private DbEventRepository dbEventRepository;

    @Test
    @DisplayName("findByTitleContainingIgnoreCase returns matching events")
    void findByTitleContainingIgnoreCase() {
        EventDb e1 = new EventDb();
        e1.setTitle("SpringOne");
        e1.setDate(new Date());
        e1.setTicketPrice(100);
        dbEventRepository.save(e1);

        EventDb e2 = new EventDb();
        e2.setTitle("SpringTwo");
        e2.setDate(new Date());
        e2.setTicketPrice(200);
        dbEventRepository.save(e2);

        EventDb e3 = new EventDb();
        e3.setTitle("OtherConf");
        e3.setDate(new Date());
        e3.setTicketPrice(300);
        dbEventRepository.save(e3);

        Page<EventDb> page = dbEventRepository.findByTitleContainingIgnoreCase("spring", PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("findByDateBetween returns events within day bounds")
    void findByDateBetween() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date start = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date end = cal.getTime();

        EventDb e = new EventDb();
        e.setTitle("TodayEvent");
        e.setDate(new Date());
        e.setTicketPrice(100);
        dbEventRepository.save(e);

        Page<EventDb> page = dbEventRepository.findByDateBetween(start, end, PageRequest.of(0, 10));
        assertThat(page.getContent()).hasSize(1);
    }
}
