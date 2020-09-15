package doc.google.api.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class AbstractGoogleDocService implements IGoogleDocService {
	static String outputPath = File.separator+"tmp" + File.separator + "poc";
	static String DOC_URL =  "https://docs.googleapis.com/v1/documents/%s";
	public void dumpJsonToFile(String output, String documentId) throws IOException{
		System.out.println(
				"*******************START OF JSON FOR DOCUMENT ID - " + documentId + "*******************");
		System.out.println(output);
		System.out
		.println("*******************END OF JSON FOR DOCUMENT ID - " + documentId + "*******************");
		File dataDirectory = new File(outputPath);
		if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
		      throw new IOException("unable to create directory: " + outputPath);
		    }
		try (FileWriter fw = new FileWriter(outputPath + java.io.File.separator + documentId + ".json")) {
			if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
			      throw new IOException("unable to create directory: " + outputPath);
			    }
				fw.write(output);
		} catch (Exception e) {
			System.out.println("Error writing to file"+e.getMessage());
		}
	}
}
