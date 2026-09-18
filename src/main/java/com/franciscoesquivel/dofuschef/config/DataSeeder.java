package com.franciscoesquivel.dofuschef.config;

import com.franciscoesquivel.dofuschef.model.User;
import com.franciscoesquivel.dofuschef.service.AuthService;
import com.franciscoesquivel.dofuschef.service.EquipmentService;
import com.franciscoesquivel.dofuschef.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "dofusdude.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements ApplicationRunner {

    private final ResourceService resourceService;
    private final EquipmentService equipmentService;
    private final AuthService authService;
    private final User ADMIN = User.builder()
            .username("admin")
            .password("admin123")
            .email("francisco.esquivel032@gmail.com")
            .build();

    @Override
    public void run(ApplicationArguments args) {
        log.info("Seeding database from Dofusdude API...");
        resourceService.load();
        equipmentService.load();

        log.info("Seeding database with admin user...");
        authService.register(ADMIN);
    }
}
