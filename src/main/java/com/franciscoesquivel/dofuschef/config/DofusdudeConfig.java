package com.franciscoesquivel.dofuschef.config;

import com.dofusdude.client.ApiClient;
import com.dofusdude.client.api.EquipmentApi;
import com.dofusdude.client.api.ResourcesApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DofusdudeConfig {

    @Value("${dofusdude.base-path}")
    private String basePath;

    @Bean
    public ApiClient apiClient() {
        ApiClient client = com.dofusdude.client.Configuration.getDefaultApiClient();
        client.setBasePath(basePath);
        return client;
    }

    @Bean
    public ApiContainer apiContainer(ApiClient client) {
        ApiContainer apiContainer = new ApiContainer();
        apiContainer.register(ResourcesApi.class, new ResourcesApi(client));
        apiContainer.register(EquipmentApi.class, new EquipmentApi(client));

        return apiContainer;
    }
}
