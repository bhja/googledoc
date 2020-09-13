package doc.google.api;


import java.util.Scanner;

import doc.google.api.service.GoogleDocService;

public class Main {
  
	public static void main(String...strings) {
		try {
			GoogleDocService service = new GoogleDocService();
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
		
  }catch(Exception ex) {
	  throw new RuntimeException(ex);
	}
}
 
}
