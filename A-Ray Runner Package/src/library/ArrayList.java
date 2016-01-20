package library;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ArrayList<E> implements List<E>, Comparable<ArrayList<E>> {

	public static final int DEFAULT_ORIGINAL_SIZE = 16;

	private static final int GROWING_SIZE = 32;

	private static final String SEPARATOR = ", ";

	private Object[] array;
	private int size = 0;

	public ArrayList() {
		this(DEFAULT_ORIGINAL_SIZE);
	}

	public ArrayList(int originalSize) {
		this.array = new Object[originalSize];
	}

	public ArrayList(E[] array) {
		this(array.length + DEFAULT_ORIGINAL_SIZE);
		for (E element : array) {
			add(element);
		}
	}

	public ArrayList(Collection<? extends E> collection) {
		this(collection, DEFAULT_ORIGINAL_SIZE);
	}

	public ArrayList(Collection<? extends E> collection, int originalSize) {
		this(originalSize);
		addAll(collection);
	}

	@Override
	public boolean add(E element) {
		if (array.length <= size) {
			grow();
		}
		array[size++] = element;
		return true;
	}

	@Override
	public void add(int index, E element) {
		if (array.length <= size) {
			grow();
		}
		for (int i = size - 1; i >= index; i--) {
			array[i + 1] = array[i];
		}
		array[index] = element;
		size++;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		for (E element : collection) {
			add(element);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		for (E element : collection) {
			add(index++, element);
		}
		return true;
	}

	@Override
	public void clear() {
		size = 0;
		array = new Object[DEFAULT_ORIGINAL_SIZE];
	}

	@Override
	public boolean contains(Object object) {
		return indexOf(object) != -1;
	}

	@Override
	public boolean containsAll(Collection<?> objects) {
		for (Object object : objects) {
			if (!contains(object)) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		return (E) array[index];
	}

	@Override
	public int indexOf(Object object) {
		for (int i = 0; i < size; i++) {
			if (array[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return listIterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		for (int i = size - 1; i >= 0; i--) {
			if (array[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		return new ArrayListIterator(this, 0);
	}

	@Override
	public ListIterator<E> listIterator(int startIndex) {
		return new ArrayListIterator(this, startIndex);
	}

	@Override
	public boolean remove(Object object) {
		int index = indexOf(object);
		if (index == -1) {
			return false;
		}
		remove(index);
		return true;
	}

	@Override
	public E remove(int index) {
		@SuppressWarnings("unchecked")
		E result = (E) array[index];
		for (int i = index; i < size; i++) {
			if (i + 2 > array.length) {
				array[i] = null;
			} else {
				array[i] = array[i + 1];
			}
		}
		size--;
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean result = true;
		for (Object object : collection) {
			result &= remove(object);
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E set(int index, E element) {
		if (index >= array.length) {
			ensureCapacity(index + GROWING_SIZE);
		}
		@SuppressWarnings("unchecked")
		E result = (E) array[index];
		array[index] = element;
		return result;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<E> subList(int start, int end) {
		ArrayList<E> result = new ArrayList<>();
		result.array = Arrays.copyOfRange(this.array, start, end);
		return result;
	}

	@Override
	public Object[] toArray() {
		Object[] result = array.length > size ? array : new Object[size];
		System.arraycopy(array, 0, result, 0, size);
		return result;
	}

	@Override
	public <T> T[] toArray(T[] array) {
		@SuppressWarnings("unchecked")
		T[] result = array.length > size ? array : (T[]) new Object[size];
		System.arraycopy(array, 0, result, 0, size);
		return result;
	}

	public void ensureCapacity(int capacity) {
		if (capacity > array.length) {
			Object[] newArray = new Object[capacity];
			System.arraycopy(array, 0, newArray, 0, array.length);
			array = newArray;
		}
	}

	public int getCapacity() {
		return this.array.length;
	}

	private void grow() {
		ensureCapacity(array.length + GROWING_SIZE);
	}

	private final class ArrayListIterator implements ListIterator<E> {

		private int currentIndex;
		private final ArrayList<E> list;

		public ArrayListIterator(ArrayList<E> list, int index) {
			currentIndex = index - 1;
			this.list = list;
		}

		@Override
		public void add(E element) {
			list.add(currentIndex++, element);
		}

		@Override
		public boolean hasNext() {
			return currentIndex < list.size - 1;
		}

		@Override
		public boolean hasPrevious() {
			return currentIndex >= 0;
		}

		@Override
		public E next() {
			return list.get(++currentIndex);
		}

		@Override
		public int nextIndex() {
			return currentIndex + 1;
		}

		@Override
		public E previous() {
			return list.get(--currentIndex);
		}

		@Override
		public int previousIndex() {
			return currentIndex - 1;
		}

		@Override
		public void remove() {
			list.remove(currentIndex);
		}

		@Override
		public void set(E element) {
			list.set(currentIndex, element);
		}

	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append('[');
		for (E element : this) {
			result.append(element).append(SEPARATOR);
		}
		return result.delete(result.length() - SEPARATOR.length(), result.length()).append(']').toString();
	}

	@Override
	public int compareTo(ArrayList<E> other) {
		if (!(other.get(0) instanceof Comparable<?>)) {
			return this.size - other.size;
		}
		for (int i = 0; i < Math.min(this.size, other.size); i++) {
			@SuppressWarnings("unchecked")
			int compareToResult = ((Comparable<E>) this.get(i)).compareTo(other.get(i));
			if (compareToResult != 0) {
				return compareToResult;
			}
		}
		return this.size - other.size;
	}

}