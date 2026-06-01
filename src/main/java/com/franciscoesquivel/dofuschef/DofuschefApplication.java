package com.franciscoesquivel.dofuschef;

import com.dofusdude.client.ApiClient;
import com.dofusdude.client.ApiException;
import com.dofusdude.client.Configuration;
import com.dofusdude.client.api.ResourcesApi;
import com.dofusdude.client.model.ListItem;
import com.franciscoesquivel.dofuschef.Resources.Resource;
import com.franciscoesquivel.dofuschef.Resources.ResourceMapper;
import com.franciscoesquivel.dofuschef.dofusdude.ListItemMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.HashSet;
import java.util.List;

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
			List<ListItem> items = (List<ListItem>) api.getAllItemsResourcesListAsync(language, gameName, "desc", 1, 200, "", new HashSet<String>(), null);
			ListItemMapper<Resource> mapper = new ResourceMapper();
			items.stream().map(mapper::map).forEach(System.out::println);
		}catch(ApiException e){
			System.out.println("Error " + e);
		}

	}

}
