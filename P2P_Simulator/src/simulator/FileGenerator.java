/**
 * 
 */
package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

public class FileGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int numFiles = 5;
		ArrayList<File> files = new ArrayList<File>();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter("file_list.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		for (int i = 0; i < numFiles; i++) {
			UUID newID = UUID.randomUUID();
			int fileSize = (int) Math.floor(Math.random() * 1000 + 1);	// Max file size for transfer = 1GB, min = 1MB
			out.println(fileSize);
		}
		out.close();
	}

}
