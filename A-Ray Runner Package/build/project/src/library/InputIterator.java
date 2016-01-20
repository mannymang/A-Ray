package library;

import java.io.File;
import java.util.Iterator;
import java.util.function.Predicate;

public class InputIterator implements Iterator<Character> {

	private final String input;
	private final int length;
	private int index = 0;

	public InputIterator(File input) {
		this(FileUtils.readAll(input));
	}

	public InputIterator(String input) {
		this.input = input;
		this.length = this.input.length();
	}

	@Override
	public boolean hasNext() {
		return length >= index;
	}

	@Override
	public Character next() {
		return index >= length ? '\0' : input.charAt(index++);
	}

	public String nextChars(int numOfChars) {
		if (index + numOfChars > length) {
			numOfChars = length - index;
		}
		String result = input.substring(index, index + numOfChars);
		index += numOfChars;
		return result;
	}

	public String nextCharsUntil(Predicate<Character> predicate) {
		int endIndex = index + 1;
		while (endIndex != length && !predicate.test(input.charAt(endIndex))) {
			endIndex++;
		}
		String result = input.substring(index, endIndex);
		index = endIndex;
		while (index != length && predicate.test(input.charAt(index))) {
			index++;
		}
		return result;
	}

	public int getNumRemaining() {
		return length - index;
	}
	
	public void reset() {
		index = 0;
	}

}
