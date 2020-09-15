package doc.google.api.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GoogleDocService extends AbstractGoogleDocService {

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static final List<String> SCOPES = Collections.singletonList(DocsScopes.DOCUMENTS_READONLY);
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	
	public GoogleDocService() {
	}

	public void convertToJson(String documentId) {
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Credential credentials = getCredentials(HTTP_TRANSPORT);
			Docs service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName("POC")
					.build();
			Document document = service.documents().get(documentId).execute();
			dumpJsonToFile(gson.toJson(document).toString(), documentId);
	
		} catch (Exception ex) {
			System.out.println("Error processing the request");
			if (ex instanceof GoogleJsonResponseException) {
				System.out.println("*******START OF ERROR STATUS *******\n"
						+ ((GoogleJsonResponseException) ex).getMessage() + "\n*******END OF ERROR STATUS *******");
			} else {
				throw new RuntimeException(ex);
			}
		}
	}

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

		InputStream in = GoogleDocService.class.getClassLoader().getResourceAsStream("client_secrets.json");
		if (in == null) {
			throw new FileNotFoundException("Resource not found: client_secrets.json");
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(new java.io.File(outputPath)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	

}
