package jku.se.drilldown.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class BenchmarkFileReader {
	public static String readFile(String fileName) throws IOException{
		File xmlFile = new File(fileName);
		StringBuilder contents = new StringBuilder();
		BufferedReader input =  new BufferedReader(new FileReader(xmlFile));
		try {
			String line = null;
			while (( line = input.readLine()) != null){
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		}
		finally {
			input.close();
		}
		return contents.toString();
	}
}
