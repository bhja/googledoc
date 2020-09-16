package doc.google.api;

import java.util.Scanner;

import doc.google.api.service.GoogleDocService;
import doc.google.api.service.GoogleDocServiceWithToken;
import doc.google.api.service.IGoogleDocService;

public class Main {

	public static void main(String... strings) {
		try {
			//Pass a property called API to go via the client credentials
			IGoogleDocService service = (strings.length == 0  ? new GoogleDocServiceWithToken() : new GoogleDocService());
			try (Scanner input = new Scanner(System.in)) {
				do {
					System.out.println("Enter the document Id or -1 to exit :: ");
					String docId = input.next();
					if (String.valueOf(docId.trim()).equals("-1")) {
						System.out.println("Done processing the files");
						return;
					}
					System.out.println("Processing the request");
					 service.convertToJson(docId);
				} while (true);
			}

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
