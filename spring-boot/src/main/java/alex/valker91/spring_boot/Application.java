package alex.valker91.spring_boot;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private BookingFacade bookingFacade;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World");
        Event event = bookingFacade.getEventById(1);
        System.out.println("Title Kurami: " + event.getTitle());
        System.out.println("Hello World");
        List<Event> events = bookingFacade.getEventsByTitle("New Title Test", 10, 0);
        System.out.println("Title Kurami: " + events.size());
    }

//    // Docker
//    docker run --name my-keycloak -p 8082:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v ./config/keycloak/import:/opt/keycloak/data/import quay.io/keycloak/keycloak:23.0.4 start-dev --import-realm
//    docker --version
//
//    docker stop my-keycloak // остановить
//    docker rm my-keycloak // удаление контейнера
//    docker start my-keycloak // запуск
//
//    http://localhost:8082/
//
//    keycloak:
//    http://localhost:8082/admin/master/console/#/my-keycloak
//    user: admin
//    password: admin
}
