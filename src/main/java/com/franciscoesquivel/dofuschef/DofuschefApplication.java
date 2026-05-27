package com.franciscoesquivel.dofuschef;

import com.dofusdude.client.ApiClient;
import com.dofusdude.client.ApiException;
import com.dofusdude.client.Configuration;
import com.dofusdude.client.api.EquipmentApi;
import com.dofusdude.client.api.ResourcesApi;
import com.dofusdude.client.model.ListItem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class DofuschefApplication {

	public static void main(String[] args) {
		SpringApplication.run(DofuschefApplication.class, args);
		ApiClient client = Configuration.getDefaultApiClient();
		client.setBasePath("https://api.dofusdu.de");

		ResourcesApi api = new ResourcesApi(client);
		String language, gameName;
		language = "es";
		gameName = "dofus3";
		try{
			List<ListItem> items = api.getItemsResourceSearch(language, gameName, "abráknido", 0, 200, 8, null);
			items.stream().map(Resource::mapListItem).forEach(System.out::println);
		}catch(ApiException e){
			System.out.println("Error " + e);
		}

	}

}
