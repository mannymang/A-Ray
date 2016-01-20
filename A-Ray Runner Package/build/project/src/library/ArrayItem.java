package library;

public class ArrayItem implements Comparable<ArrayItem> {

	private Type type;
	private Object value;

	public ArrayItem(Object value, Type type) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(ArrayItem other) {
		return ((Comparable<Object>) this.value).compareTo(other.value);
	}

}
