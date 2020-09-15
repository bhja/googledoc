package doc.google.api.service;

import java.io.File;
import java.io.FileWriter;

public abstract class AbstractGoogleDocService implements IGoogleDocService {
	static String outputPath = "tmp" + File.separator + "poc";
	static String DOC_URL =  "https://docs.googleapis.com/v1/documents/%s";
	public void dumpJsonToFile(String output, String documentId) {
		System.out.println(
				"*******************START OF JSON FOR DOCUMENT ID - " + documentId + "*******************");
		System.out.println(output);
		System.out
		.println("*******************END OF JSON FOR DOCUMENT ID - " + documentId + "*******************");
		try (FileWriter fw = new FileWriter(outputPath + java.io.File.separator + documentId + ".json")) {
			fw.write(output);
		} catch (Exception e) {
			System.out.println("Error writing to file");
		}
	}
}
