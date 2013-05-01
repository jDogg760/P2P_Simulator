/**
 * 
 */
package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Joshua Pantarotto
 *
 */
public class FileGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int numFiles = 20;
		ArrayList<File> files = new ArrayList<File>();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter("file_list.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		for (int i = 0; i < numFiles; i++) {
			UUID newID = UUID.randomUUID();
			int fileSize = (int) Math.floor(Math.random() * 1000 + 1);	// Max file size for transfer = 1GB, min = 1MB
//			File newFile = new File(newID, fileSize);
			out.println(fileSize);
			//			System.out.println("\t"+newFile.id);
		}
		out.close();
	}

}
