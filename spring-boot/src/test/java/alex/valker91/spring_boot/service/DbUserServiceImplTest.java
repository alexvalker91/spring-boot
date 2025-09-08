package alex.valker91.spring_boot.service;

import alex.valker91.spring_boot.entity.UserDb;
import alex.valker91.spring_boot.model.User;
import alex.valker91.spring_boot.model.impl.UserImpl;
import alex.valker91.spring_boot.repository.DbUserRepository;
import alex.valker91.spring_boot.service.impl.DbUserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbUserServiceImplTest {

    @Mock
    private DbUserRepository dbUserRepository;

    @InjectMocks
    private DbUserServiceImpl userService;

    @Test
    @DisplayName("getUserByEmail maps entity to model when present")
    void getUserByEmail_found() {
        when(dbUserRepository.findByEmail("a@b.com")).thenReturn(Optional.of(new UserDb(1L, "Alex", "a@b.com")));

        User user = userService.getUserByEmail("a@b.com");

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Alex");
        assertThat(user.getEmail()).isEqualTo("a@b.com");
    }

    @Test
    @DisplayName("getUserById returns null when missing")
    void getUserById_missing() {
        when(dbUserRepository.findById(99L)).thenReturn(Optional.empty());

        User user = userService.getUserById(99L);

        assertThat(user).isNull();
    }

    @Test
    @DisplayName("createUser saves entity and returns model")
    void createUser_saves() {
        User input = new UserImpl("Alex", "a@b.com");
        when(dbUserRepository.save(any(UserDb.class))).thenAnswer(inv -> inv.getArgument(0));

        User created = userService.createUser(input);

        assertThat(created).isNotNull();
        verify(dbUserRepository).save(any(UserDb.class));
    }

    @Test
    @DisplayName("updateUser saves entity and returns model")
    void updateUser_saves() {
        User input = new UserImpl(5L, "Alex", "a@b.com");

        User updated = userService.updateUser(input);

        assertThat(updated).isNotNull();
        verify(dbUserRepository).save(any(UserDb.class));
    }

    @Test
    @DisplayName("deleteUser returns true when user exists")
    void deleteUser_true() {
        when(dbUserRepository.findById(7L)).thenReturn(Optional.of(new UserDb(7L, "N", "e")));

        boolean result = userService.deleteUser(7L);

        assertThat(result).isTrue();
        verify(dbUserRepository).deleteById(eq(7L));
    }

    @Test
    @DisplayName("deleteUser returns false when user missing")
    void deleteUser_false() {
        when(dbUserRepository.findById(7L)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(7L);

        assertThat(result).isFalse();
        verify(dbUserRepository, never()).deleteById(anyLong());
    }
}

