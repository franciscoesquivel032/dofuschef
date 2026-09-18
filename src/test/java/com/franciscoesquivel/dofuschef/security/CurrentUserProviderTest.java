package com.franciscoesquivel.dofuschef.security;

import com.franciscoesquivel.dofuschef.exception.ResourceNotFoundException;
import com.franciscoesquivel.dofuschef.model.User;
import com.franciscoesquivel.dofuschef.repository.IUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentUserProviderTest {

    @Mock
    IUserRepository userRepository;

    @InjectMocks
    CurrentUserProvider provider;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUsername_whenAuthenticated_thenReturnsPrincipalName() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("alice", null));

        String username = provider.getCurrentUsername();

        assertThat(username).isEqualTo("alice");
    }

    @Test
    void getCurrentUser_whenUserExists_thenReturnsUserEntity() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("alice", null));
        User user = new User(1L, "alice", "hash", "alice@test.com");
        when(userRepository.findUserByUsername("alice")).thenReturn(user);

        User result = provider.getCurrentUser();

        assertThat(result).isEqualTo(user);
    }

    @Test
    void getCurrentUser_whenUserNotFound_thenThrowsResourceNotFoundException() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("ghost", null));
        when(userRepository.findUserByUsername("ghost")).thenReturn(null);

        assertThatThrownBy(() -> provider.getCurrentUser())
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
