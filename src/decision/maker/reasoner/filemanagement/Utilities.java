package decision.maker.reasoner.filemanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Utilities {
	private final int bufferSize = 4 * 1024;
	
	public void copyFiles(File input_file, File output_file) {
		try {
			FileInputStream reader = new FileInputStream(input_file);
			FileOutputStream writer = new FileOutputStream(output_file);
			byte[] b = new byte[bufferSize]; 
			int bytesRead;
			while ((bytesRead = reader.read(b)) >= 0) {
				writer.write(b, 0, bytesRead);
			}
			writer.flush();
			reader.close();
			writer.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred while processing temporary files");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("I/O error");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void appendString(File temp_file_file, String toAppend) {
		try {
			FileWriter filewriter = new FileWriter(temp_file_file, true);
			BufferedWriter writer = new BufferedWriter(filewriter);
			writer.write(toAppend);
			writer.flush();
			filewriter.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public String getStringFromFile(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String res = "";
			String temp;
			int i = 0;
			while ((temp = reader.readLine()) != null) {
				res += temp+"\n";
				System.out.println(i++);
			}
			return res;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}
	
}
