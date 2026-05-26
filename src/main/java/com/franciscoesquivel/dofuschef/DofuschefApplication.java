package com.franciscoesquivel.dofuschef;

import com.dofusdude.client.ApiClient;
import com.dofusdude.client.ApiException;
import com.dofusdude.client.Configuration;
import com.dofusdude.client.api.EquipmentApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DofuschefApplication {

	public static void main(String[] args) {
		SpringApplication.run(DofuschefApplication.class, args);
		ApiClient client = Configuration.getDefaultApiClient();
		client.setBasePath("https://api.dofusdu.de");

		EquipmentApi api = new EquipmentApi(client);
		String language, gameName;
		language = "es";
		gameName = "dofus3";
		try{
			System.out.println(api.getItemsEquipmentSearch(language, gameName, "guerra", 0, 200, 8, null));
		}catch(ApiException e){
			System.out.println("Error " + e);
		}

	}

}
