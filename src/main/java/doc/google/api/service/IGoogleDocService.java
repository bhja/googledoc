package doc.google.api.service;

import java.io.IOException;

public interface IGoogleDocService {
	 void dumpJsonToFile(String output, String documentId) throws IOException;
	 void convertToJson(String documentId) ;

}
