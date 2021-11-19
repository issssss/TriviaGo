package hr.fer.ruazosa.networkquiz;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class NetworkQuizApplication {

	public static void main(String[] args) throws IOException {

		//ovo tu po potrebi/vlastitom ip-u izmijeniti izmijeniti
		String base_URL = "192.168.5.10"; //Izabela
		//String base_URL = "192.168.1.5"; //Monika
		//FileInputStream refreshToken = new FileInputStream("C:\\Users\\Filip\\Downloads\\triviagoproject-firebase-adminsdk-fj1lj-004686daa9.json");
		//FileInputStream refreshToken = new FileInputStream("C:\\Users\\Monika\\Downloads\\triviagoproject-firebase-adminsdk-fj1lj-0a2ce93ec5.json");

		FirebaseOptions options = FirebaseOptions.builder()
		//		.setCredentials(GoogleCredentials.fromStream(refreshToken))
				.setCredentials(GoogleCredentials.getApplicationDefault())
				.setDatabaseUrl("http://"+ base_URL+":8080/")
				.build();

		FirebaseApp.initializeApp(options);
		SpringApplication.run(NetworkQuizApplication.class, args);
	}

}
