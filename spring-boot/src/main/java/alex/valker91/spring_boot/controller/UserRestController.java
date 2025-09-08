package alex.valker91.spring_boot.controller;

import alex.valker91.spring_boot.facade.BookingFacade;
import alex.valker91.spring_boot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final BookingFacade bookingFacade;

    /*
    Example: GET
    localhost:8081/api/v1/users/1
     */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        return bookingFacade.getUserById(userId);
    }


    /*
    Example: GET
    localhost:8081/api/v1/users/email?email=alex@gmail.com
     */
    @GetMapping("/email")
    private User getUserByEmail(
            @RequestParam(name = "email", required = true) String email
    ) {
        return bookingFacade.getUserByEmail(email);
    }

    /*
    Example: GET
    localhost:8081/api/v1/users/name?name=alex
     */
    @GetMapping("/name")
    private List<User> getUsersByName(
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "pageNum", defaultValue = "0", required = false) int pageNum
    ) {
        return bookingFacade.getUsersByName(name, pageSize, pageNum);
    }

    /*
    Example: POST
    localhost:8081/api/v1/users/create_user
{
    "id": 2,
    "name": "alex",
    "email": "alex@gmail.com"
}
     */
    @PostMapping("/create_user")
    public User createUser(@RequestBody User user) {
        return bookingFacade.createUser(user);
    }

    /*
    Example: PUT
    localhost:8081/api/v1/users/update_user
{
    "id": 1,
    "name": "alex new",
    "email": "alex_new@gmail.com"
}
     */
    @PutMapping("/update_user")
    public User updateUser(@RequestBody User user) {
        return bookingFacade.updateUser(user);
    }

    /*
    Example: DELETE
    localhost:8081/api/v1/users/delete_user/1
     */
    @DeleteMapping("delete_user/{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        bookingFacade.deleteUser(userId);
    }
}
