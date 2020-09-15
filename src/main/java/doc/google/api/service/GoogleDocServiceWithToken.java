package doc.google.api.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;

public class GoogleDocServiceWithToken extends AbstractGoogleDocService {

	private UserCredentials credentials = null;
	private long _tokenValidity;
	private final long _tokenValidThreshold = 3600;
	private String _currentToken;
	private long _lastTokenTime = 0;

	protected String getCurrentToken() {
		return _currentToken;
	}

	protected long getLastTokenTime() {
		return _lastTokenTime;
	}

	protected long getTokenValidThreshold() {
		return _tokenValidThreshold;
	}

	protected long getTokenValidity() {
		return _tokenValidity;
	}

	public GoogleDocServiceWithToken() {
		try (InputStream in = GoogleDocServiceWithToken.class.getClassLoader().getResourceAsStream("tokens.json")) {
			credentials = UserCredentials.fromStream(in);
		} catch (IOException ex) {
			System.out.print("Error processing the property file " + ex.getMessage());
			System.exit(1);
		}
	}

	public void convertToJson(String documentId) {
		try {
			getToken();
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(String.format(DOC_URL, documentId));

			// add request headers
			request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getCurrentToken());
			org.apache.http.HttpResponse response = client.execute(request);

			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("Issue processing the request");
				System.out.println(responseString);
				return;
			}
			dumpJsonToFile(responseString, documentId);
			

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

	protected String getToken() {
		long tokenGrantTime = getLastTokenTime();
		long tokenTTL = getTokenValidity();
		long now = System.currentTimeMillis();
		long tokenValidThreshold = getTokenValidThreshold();
		if ((now - tokenGrantTime) > (tokenTTL - tokenValidThreshold)) {
			refreshAccessToken();
		}
		return getCurrentToken();

	}

	protected void refreshAccessToken() {
		try {
			AccessToken token = credentials.refreshAccessToken();
			_currentToken = token.getTokenValue();
			_lastTokenTime = System.currentTimeMillis();
			_tokenValidity = token.getExpirationTime().getTime();
		} catch (IOException e) {
			System.out.print("Error requesting token" + e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
