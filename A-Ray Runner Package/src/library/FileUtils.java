package library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {

	public static String readAll(File file) {
		Output<String> out = new StringOutput();
		read(out, file);
		return out.toObject();
	}
	
	public static List<String> readAllLines(File file) {
		Output<List<String>> out = new ListOutput();
		read(out, file);
		return out.toObject();
	}
	
	private static <T> void read(Output<T> output, File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			for (String current = reader.readLine(); current != null; current = reader
					.readLine()) {
				output.append(current);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

interface Output<T> {
	
	public void append(String line);
	
	public T toObject();
	
}

class ListOutput implements Output<List<String>> {
	
	private final List<String> result = new LinkedList<>();

	@Override
	public void append(String line) {
		result.add(line);
	}

	@Override
	public List<String> toObject() {
		return result;
	}
	
}

class StringOutput implements Output<String> {
	
	private final StringBuilder result = new StringBuilder();

	@Override
	public void append(String line) {
		result.append(line).append('\n');
	}

	@Override
	public String toObject() {
		return result.toString();
	}
	
}
