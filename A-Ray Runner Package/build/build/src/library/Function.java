package library;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public final class Function<T> {

	private final Type[] parameterTypes;
	private final RunnableFunction<T> function;

	public Function(Type[] parameterTypes, RunnableFunction<T> function) {
		this.parameterTypes = parameterTypes;
		this.function = function;
	}

	public T run(List<ArrayItem> memory, InputIterator input,
			StringBuilder output, MutableObject temporaryVariable,
			Object[] args) {
		if (args.length != parameterTypes.length) {
			throw new IllegalArgumentException(); // TODO
		}
		for (int i = 0; i < args.length; i++) {
			switch (parameterTypes[i]) {
			case ARRAY:
				args[i] = toArray(args[i]);
				break;
			case BOOLEAN:
				args[i] = toBoolean(args[i]);
				break;
			case CHARACTER:
				args[i] = toChar(args[i]);
				break;
			case DECIMAL:
				args[i] = toDecimal(args[i]);
				break;
			case INTEGER:
				args[i] = toInteger(args[i]);
				break;
			case STRING:
				args[i] = toString(args[i]);
				break;
			case FUNCTION:
				args[i] = toFunction(args[i]);
			default:
				// object
				break;
			}
		}
		return function.run(memory, input, output, temporaryVariable, args);
	}

	public Type[] getParameterTypes() {
		return parameterTypes;
	}

	@SuppressWarnings("unchecked")
	public static String toString(Object object) {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		switch (type) {
		case ARRAY:
			List<ArrayItem> array = (List<ArrayItem>) object;
			Object value = array.get(0).getValue();
			if (Type.CHARACTER.isMatch(value) || Type.STRING.isMatch(value)) {
				StringBuilder result = new StringBuilder();
				for (ArrayItem item : array) {
					result.append(item);
				}
				return result.toString();
			}
			return ((List<ArrayItem>) object).toString();
		case BOOLEAN:
			return Boolean.toString((boolean) object);
		case CHARACTER:
			return Character.toString((char) object);
		case DECIMAL:
			return ((BigDecimal) object).toPlainString();
		case INTEGER:
			return ((BigInteger) object).toString();
		case OBJECT:
			return object.toString();
		case STRING:
			return ((CharSequence) object).toString();
		case FUNCTION:
			return toString(((A_RayCode) object).run(0).result); // TODO
		}
		return null;
	}

	public static BigDecimal toDecimal(Object object) {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		switch (type) {
		case ARRAY:
			return null;
		case BOOLEAN:
			return BigDecimal.ONE;
		case CHARACTER:
			return BigDecimal.valueOf((char) object);
		case DECIMAL:
			return (BigDecimal) object;
		case INTEGER:
			return new BigDecimal((BigInteger) object);
		case OBJECT:
			return null;
		case STRING:
			return new BigDecimal((String) object);
		case FUNCTION:
			return toDecimal(((A_RayCode) object).run(0).result); // TODO
		}
		return null;
	}

	public static Character toChar(Object object) {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		switch (type) {
		case ARRAY:
			return null;
		case BOOLEAN:
			return (boolean) object ? 't' : 'f';
		case CHARACTER:
			return (char) object;
		case DECIMAL:
			return null;
		case INTEGER:
			BigInteger num = (BigInteger) object;
			return num.bitCount() > Character.SIZE ? null
					: (char) num.intValue();
		case OBJECT:
			return null;
		case STRING:
			return ((CharSequence) object).charAt(0);
		case FUNCTION:
			return toChar(((A_RayCode) object).run(0).result); // TODO
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<ArrayItem> toArray(Object object) {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		List<ArrayItem> array = new ArrayList<>();
		switch (type) {
		case ARRAY:
			return ((List<ArrayItem>) object);
		case STRING:
			((String) object).chars().forEach(e -> array.add(new ArrayItem((char) e, type)));
			return array;
		default:
			array.add(new ArrayItem(object, type));
			return array;
		}
	}

	@SuppressWarnings("unchecked")
	public static Boolean toBoolean(Object object) {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		switch (type) {
		case ARRAY:
			return ((List<ArrayItem>) object).size() != 0;
		case BOOLEAN:
			return (boolean) object;
		case CHARACTER:
			return (char) object != '\0';
		case DECIMAL:
			return !((BigDecimal) object).equals(BigDecimal.ZERO);
		case INTEGER:
			return !((BigInteger) object).equals(BigInteger.ZERO);
		case OBJECT:
			return object != null;
		case STRING:
			return ((CharSequence) object).length() != 0;
		case FUNCTION:
			return toBoolean(((A_RayCode) object).run(0).result); // TODO
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static BigInteger toInteger(Object object) {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		switch (type) {
		case ARRAY:
			return BigInteger.valueOf(((List<ArrayItem>) object).size());
		case BOOLEAN:
			return (boolean) object ? BigInteger.ONE : BigInteger.ZERO;
		case CHARACTER:
			return BigInteger.valueOf((char) object);
		case DECIMAL:
			return ((BigDecimal) object).toBigInteger();
		case INTEGER:
			return (BigInteger) object;
		case OBJECT:
			return null;
		case STRING:
			return new BigInteger((String) object);
		case FUNCTION:
			return toInteger(((A_RayCode) object).run(0).result); // TODO
		}
		return null;
	}
	
	public static A_RayCode toFunction(Object object) {
		if (Type.getMatch(object) == Type.FUNCTION) {
			return (A_RayCode) object;
		}
		return new A_RayCode("", "") {
			
			@Override
			protected FunctionResult run(int index) {
				return new FunctionResult(object, 1);
			}
			
		};
	}

}