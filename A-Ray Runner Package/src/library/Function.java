package library;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public final class Function<T> {

	private final Type[] parameterTypes;
	private final RunnableFunction<T> function;

	public Function(Type[] parameterTypes, RunnableFunction<T> function) {
		this.parameterTypes = parameterTypes;
		this.function = function;
	}

	public T run(List<Object> memory, InputIterator input, StringBuilder output,
			MutableObject temporaryVariable, Map<String, Object> variables,
			Object[] args) throws LoopFlag {
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
				break;
			case OBJECT:
				args[i] = toObject(args[i]);
			}
		}
		return function.run(memory, input, output, temporaryVariable, variables,
				args);
	}

	public Type[] getParameterTypes() {
		return parameterTypes;
	}

	@SuppressWarnings("unchecked")
	public static int compare(Object object1, Object object2) throws LoopFlag {
		if (object1 == null) {
			if (object2 == null) {
				return 0;
			}
			return -1;
		}
		if (object2 == null) {
			return 1;
		}
		Type type1 = Type.getMatch(object1);
		Type type2 = Type.getMatch(object2);
		if (type1 == Type.FUNCTION) {
			object1 = ((A_RayCode) object1).run(0).result;
			type1 = Type.getMatch(object1);
		}
		if (type2 == Type.FUNCTION) {
			object2 = ((A_RayCode) object2).run(0).result;
			type2 = Type.getMatch(object2);
		}
		if (type1 == type2) {
			return ((Comparable<Object>) object1).compareTo(object2);
		}
		if (type1 == Type.BOOLEAN || type2 == Type.BOOLEAN) {
			boolean bool1 = toBoolean(object1);
			boolean bool2 = toBoolean(object2);
			if (bool1) {
				return bool2 ? 0 : 1;
			} else {
				return bool2 ? -1 : 0;
			}
		}
		// TODO

		return 0;
	}

	private Object toObject(Object object) throws LoopFlag {
		if (Type.getMatch(object) == Type.FUNCTION) {
			return ((A_RayCode) object).run();
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	public static String toString(Object object) throws LoopFlag {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		switch (type) {
		case ARRAY:
			List<Object> array = (List<Object>) object;
			Object value = array.get(0);
			if (Type.CHARACTER.isMatch(value) || Type.STRING.isMatch(value)) {
				StringBuilder result = new StringBuilder();
				for (Object item : array) {
					result.append(item);
				}
				return result.toString();
			}
			return ((List<Object>) object).toString();
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

	public static BigDecimal toDecimal(Object object) throws LoopFlag {
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

	public static Character toChar(Object object) throws LoopFlag {
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
	public static List<Object> toArray(Object object) {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		List<Object> array = new ArrayList<>();
		switch (type) {
		case ARRAY:
			return ((List<Object>) object);
		case STRING:
			((String) object).chars().forEach(e -> array.add((char) e));
			return array;
		default:
			array.add(object);
			return array;
		}
	}

	@SuppressWarnings("unchecked")
	public static Boolean toBoolean(Object object) throws LoopFlag {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		switch (type) {
		case ARRAY:
			return ((List<Object>) object).size() != 0;
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
	public static BigInteger toInteger(Object object) throws LoopFlag {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		}
		switch (type) {
		case ARRAY:
			return BigInteger.valueOf(((List<Object>) object).size());
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

	@SuppressWarnings("unchecked")
	public static Object copy(Object object) {
		Type type = Type.getMatch(object);
		if (type == null) {
			return null;
		} else if (type == Type.ARRAY) {
			return new ArrayList<>(((List<Object>) object));
		}
		return object;
	}

}
