package com.jd.eventhall.igdblocal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jd.eventhall.igdblocal.service.GameService;
import com.jd.eventhall.igdblocal.service.IGDBWebhookService;

@SpringBootApplication
public class IgdbLocalDataBaseApplication implements CommandLineRunner {

	@Autowired
	private GameService gameSvc;

	@Autowired
	private IGDBWebhookService webhookSvc;

	public static void main(String[] args) {
		SpringApplication.run(IgdbLocalDataBaseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// gameSvc.createGameDB();
		// gameSvc.createCoverDB();
		// gameSvc.createReleaseDateDB();
		// gameSvc.createPlatformDB();
		// can make db on run
		webhookSvc.createGameWebhooks();
		webhookSvc.createCoverWebhooks();
		webhookSvc.createReleaseDateWebhooks();
		webhookSvc.createPlatformWebhooks();
	}

}
