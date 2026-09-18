package com.franciscoesquivel.dofuschef.security;

import com.franciscoesquivel.dofuschef.exception.ResourceNotFoundException;
import com.franciscoesquivel.dofuschef.model.User;
import com.franciscoesquivel.dofuschef.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final IUserRepository userRepository;

    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getCurrentUser() {
        String username = getCurrentUsername();
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("Authenticated user not found: " + username);
        }
        return user;
    }
}
