package library;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class A_RayCode {

	public static final String FILENAME_EXTENSTION = ".br";

	protected final String code;
	protected final InputIterator input;
	protected final StringBuilder output;
	protected final List<ArrayItem> memory;
	protected final MutableObject temporaryVariable;

	protected static final Map<String, Function<?>> functions = new HashMap<>();

	protected static Predicate<Character> isSeparator = new Predicate<Character>() {

		@Override
		public boolean test(Character c) {
			return c == ',' || c == ' ' || c == ';' || c == '\n';
		}

	};

	protected static Predicate<Character> isNumberSeparator = new Predicate<Character>() {

		@Override
		public boolean test(Character c) {
			return !Character.isDigit(c);
		}

	};

	static {
		// template
		functions.put("", new Function<Void>(new Type[] {},
				new RunnableFunction<Void>() {

					@Override
					public Void run(List<ArrayItem> memory, InputIterator input,
							StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return null;
					}

				}));
		functions.put("i", new Function<String>(new Type[] {},
				new RunnableFunction<String>() {

					@Override
					public String run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return input.nextCharsUntil(isSeparator);
					}

				}));
		functions.put("I", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return new BigInteger(input.nextCharsUntil(
								isNumberSeparator));
					}

				}));
		functions.put("s", new Function<Boolean>(new Type[] {},
				new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						char result = input.next();
						return result != '\0';
					}

				}));
		functions.put("S", new Function<Boolean>(new Type[] {},
				new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						String result = input.nextCharsUntil(isSeparator
								.negate());
						return !result.isEmpty();
					}

				}));
		functions.put("p", new Function<Boolean>(new Type[] { Type.OBJECT },
				new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						output.append(Function.toString(args[0]));
						return true;
					}

				}));
		functions.put("c", new Function<Character>(new Type[] {},
				new RunnableFunction<Character>() {

					@Override
					public Character run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return input.next();
					}

				}));
		functions.put("C", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return BigInteger.valueOf(input.next());
					}

				}));
		functions.put("r", new Function<String>(new Type[] {},
				new RunnableFunction<String>() {

					@Override
					public String run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return input.nextChars(input.getNumRemaining());
					}

				}));
		functions.put("d", new Function<List<ArrayItem>>(new Type[] {
				Type.STRING }, new RunnableFunction<List<ArrayItem>>() {

					@Override
					public List<ArrayItem> run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						List<ArrayItem> result = new ArrayList<>();
						new ArrayList<String>(args[0].toString().split(
								"(,| |;|\n)+")).stream().forEach(e -> result
										.add(new ArrayItem(e, Type.STRING)));
						return result;
					}

				}));
		functions.put("a", new Function<List<ArrayItem>>(new Type[] {
				Type.ARRAY, Type.OBJECT },
				new RunnableFunction<List<ArrayItem>>() {

					@Override
					public List<ArrayItem> run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						List<ArrayItem> result = Function.toArray(args[0]);
						result.add(new ArrayItem(args[1], Type.getMatch(
								args[1])));
						return result;
					}

				}));
		functions.put("A", new Function<List<ArrayItem>>(new Type[] {
				Type.ARRAY, Type.OBJECT, Type.INTEGER },
				new RunnableFunction<List<ArrayItem>>() {

					@Override
					public List<ArrayItem> run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						List<ArrayItem> result = Function.toArray(args[0]);
						result.add(((BigInteger) args[2]).intValue(),
								new ArrayItem(args[1], Type.getMatch(args[1])));
						return result;
					}

				}));
		functions.put("g", new Function<Object>(new Type[] { Type.ARRAY,
				Type.INTEGER }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						List<ArrayItem> result = Function.toArray(args[0]);
						return result.get(Function.toInteger(args[1])
								.intValue()).getValue();
					}

				}));
		functions.put("G", new Function<Object>(new Type[] { Type.ARRAY,
				Type.INTEGER, Type.OBJECT }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						List<ArrayItem> result = Function.toArray(args[0]);
						return result.set(Function.toInteger(args[1])
								.intValue(), new ArrayItem(args[2], Type
										.getMatch(args[2])));
					}

				}));
		functions.put("f", new Function<Boolean>(new Type[] { Type.FUNCTION },
				new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						boolean result = false;
						LoopCode code = new LoopCode(((A_RayCode) args[0]).code,
								memory, input, output, temporaryVariable);
						boolean keepRunning = true;
						while (keepRunning) {
							try {
								keepRunning = (boolean) code.run().result;
							} catch (LoopFlag e) {
								if (e.getAction() == Action.BREAK) {
									keepRunning = false;
								}
							}
							result = true;
						}
						return result;
					}

				}));
		functions.put("F", new Function<Boolean>(new Type[] { Type.ARRAY,
				Type.FUNCTION }, new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						boolean result = false;
						IteratorCode code = new IteratorCode(Function.toArray(
								args[0]), ((A_RayCode) args[0]).code, memory,
								input, output, temporaryVariable);
						boolean keepRunning = true;
						while (keepRunning) {
							try {
								keepRunning = (boolean) code.run().result;
							} catch (LoopFlag e) {
								if (e.getAction() == Action.BREAK) {
									keepRunning = false;
								}
							}
							result = true;
						}
						return result;
					}

				}));
		functions.put("?", new Function<Boolean>(new Type[] { Type.BOOLEAN,
				Type.FUNCTION, Type.FUNCTION },
				new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						boolean result = Function.toBoolean(args[0]);
						A_RayCode code = (A_RayCode) (result ? args[1]
								: args[2]);
						code.run();
						return result;
					}

				}));
		functions.put(" ", new Function<A_RayCode>(new Type[] {},
				new RunnableFunction<A_RayCode>() {

					@Override
					public A_RayCode run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return new A_RayCode("", memory, input, output,
								temporaryVariable);
					}

				}));
		functions.put("e", new Function<List<ArrayItem>>(new Type[] {},
				new RunnableFunction<List<ArrayItem>>() {

					@Override
					public List<ArrayItem> run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return memory;
					}

				}));
		functions.put("E", new Function<List<ArrayItem>>(new Type[] {
				Type.ARRAY }, new RunnableFunction<List<ArrayItem>>() {

					@Override
					public List<ArrayItem> run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						List<ArrayItem> result = new ArrayList<>(memory);
						memory.clear();
						memory.addAll(Function.toArray(args[0]));
						return result;
					}

				}));
		functions.put("l", new Function<BigInteger>(new Type[] { Type.ARRAY },
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return BigInteger.valueOf(Function.toArray(args[0])
								.size());
					}

				}));
		functions.put("t", new Function<Object>(new Type[] {},
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return temporaryVariable.getValue();
					}

				}));
		functions.put("T", new Function<Object>(new Type[] { Type.OBJECT },
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						Object result = temporaryVariable.getValue();
						temporaryVariable.setValue(args[0]);
						return result == null ? new Object() : result;
					}

				}));
		functions.put(":", new Function<List<ArrayItem>>(new Type[] {
				Type.ARRAY, Type.INTEGER },
				new RunnableFunction<List<ArrayItem>>() {

					@Override
					public List<ArrayItem> run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						int length = Function.toInteger(args[1]).intValue();
						List<ArrayItem> result = new ArrayList<>();
						getAllPerms(Function.toArray(args[0]), result,
								new ArrayList<ArrayItem>(), length, 0, 0);
						return result;
					}

					private void getAllPerms(List<ArrayItem> array,
							List<ArrayItem> fullList,
							List<ArrayItem> currentList, int requiredLength,
							int currentLength, int index) {
						if (requiredLength == currentLength) {
							fullList.add(new ArrayItem(new ArrayList<>(
									currentList), Type.ARRAY));
							currentList.remove(currentLength - 1);
							return;
						}
						for (int i = index, max = array.size() - (requiredLength
								- currentLength); i <= max; i++) {
							Object object = array.get(i).getValue();
							currentList.add(new ArrayItem(object, Type.getMatch(
									object)));
							getAllPerms(array, fullList, currentList,
									requiredLength, currentLength + 1, i + 1);
						}
						if (currentLength > 0) {
							currentList.remove(currentLength - 1);
						}
					}

				}));
		functions.put("m", new Function<Object>(new Type[] { Type.ARRAY },
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						List<ArrayItem> array = Function.toArray(args[0]);
						return Collections.min(array).getValue();
					}

				}));
		functions.put("M", new Function<Object>(new Type[] { Type.ARRAY },
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						List<ArrayItem> array = Function.toArray(args[0]);
						return Collections.max(array).getValue();
					}

				}));
		functions.put("b", new Function<Object>(new Type[] {},
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						throw new LoopFlag(Action.BREAK);
					}

				}));
		functions.put("B", new Function<Object>(new Type[] {},
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						throw new LoopFlag(Action.CONTINUE);
					}

				}));
		functions.put("R", new Function<Boolean>(new Type[] { Type.STRING,
				Type.STRING }, new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						return Function.toString(args[1]).matches(Function
								.toString(args[0]));
					}

				}));
		functions.put("u", new Function<List<ArrayItem>>(new Type[] {
				Type.INTEGER }, new RunnableFunction<List<ArrayItem>>() {

					@Override
					public List<ArrayItem> run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						BigInteger to = Function.toInteger(args[0]);
						List<ArrayItem> result = new ArrayList<>(to.intValue());
						while (!to.equals(BigInteger.ONE)) {
							to = to.subtract(BigInteger.ONE);
							result.add(0, new ArrayItem(to, Type.INTEGER));
						}
						return result;
					}

				}));
		functions.put("U", new Function<List<ArrayItem>>(new Type[] {
				Type.INTEGER, Type.INTEGER },
				new RunnableFunction<List<ArrayItem>>() {

					@Override
					public List<ArrayItem> run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						BigInteger from = Function.toInteger(args[0]);
						BigInteger to = Function.toInteger(args[1]);
						List<ArrayItem> result = new ArrayList<>();
						while (!from.equals(to)) {
							result.add(0, new ArrayItem(to, Type.INTEGER));
							from = from.add(BigInteger.ONE);
						}
						return result;
					}

				}));

		final BigInteger TWO = BigInteger.valueOf(2L);
		final BigInteger THREE = BigInteger.valueOf(3L);
		final BigInteger FOUR = BigInteger.valueOf(4L);
		final BigInteger FIVE = BigInteger.valueOf(5L);
		final BigInteger SIX = BigInteger.valueOf(6L);
		final BigInteger SEVEN = BigInteger.valueOf(7L);
		final BigInteger EIGHT = BigInteger.valueOf(8L);
		final BigInteger NINE = BigInteger.valueOf(9L);

		functions.put("0", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return BigInteger.ZERO;
					}

				}));
		functions.put("1", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return BigInteger.ONE;
					}

				}));
		functions.put("2", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return TWO;
					}

				}));
		functions.put("3", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return THREE;
					}

				}));
		functions.put("4", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return FOUR;
					}

				}));
		functions.put("5", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return FIVE;
					}

				}));
		functions.put("6", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return SIX;
					}

				}));
		functions.put("7", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return SEVEN;
					}

				}));
		functions.put("8", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return EIGHT;
					}

				}));
		functions.put("9", new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return NINE;
					}

				}));
		functions.put("+", new Function<Object>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if ((type1 == Type.DECIMAL || type1 == Type.INTEGER)
								&& (type2 == Type.DECIMAL
										|| type2 == Type.INTEGER)) {
							return addNumbers(args[0], type1, args[1], type2);
						} else if (type1 == Type.ARRAY) {
							return append(Function.toArray(args[0]), args[1]);
						} else if (type2 == Type.ARRAY) {
							return append(Function.toArray(args[1]), args[0]);
						} else if (type1 == Type.STRING
								|| type2 == Type.STRING) {
							return concat(args[0], args[1], type2);
						}
						return concat(args[0], args[1], type2);
					}

					private Object append(List<ArrayItem> list, Object object) {
						list.add(new ArrayItem(object, Type.getMatch(object)));
						return list;
					}

					private Object addNumbers(Object number1, Type type1,
							Object number2, Type type2) {
						return toBigDecimal(number1, type1).add(toBigDecimal(
								number2, type2));
					}

					private BigDecimal toBigDecimal(Object number, Type type) {
						return type == Type.INTEGER ? new BigDecimal(
								(BigInteger) number) : (BigDecimal) number;
					}

					private String concat(Object object1, Object object2,
							Type type2) throws LoopFlag {
						return Function.toString(object1) + Function.toString(
								object2);
					}

				}));
		functions.put("-", new Function<Object>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if ((type1 == Type.DECIMAL || type1 == Type.INTEGER)
								&& (type2 == Type.DECIMAL
										|| type2 == Type.INTEGER)) {
							return subtractNumbers(args[0], type1, args[1],
									type2);
						} else if (type1 == Type.ARRAY) {
							return remove(Function.toArray(args[0]), Function
									.toInteger(args[1]));
						} else if (type1 == Type.STRING
								&& type2 == Type.CHARACTER) {
							return Function.toString(args[0]).replaceAll(Pattern
									.quote(Function.toString(args[1])), "");
						}
						return null;
					}

					private List<ArrayItem> remove(List<ArrayItem> list,
							BigInteger index) {
						list.remove(index.intValue());
						return list;
					}

					private Object subtractNumbers(Object number1, Type type1,
							Object number2, Type type2) {
						return toBigDecimal(number1, type1).subtract(
								toBigDecimal(number2, type2));
					}

					private BigDecimal toBigDecimal(Object number, Type type) {
						return type == Type.INTEGER ? new BigDecimal(
								(BigInteger) number) : (BigDecimal) number;
					}

				}));
		functions.put("*", new Function<Object>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if ((type1 == Type.DECIMAL || type1 == Type.INTEGER)
								&& (type2 == Type.DECIMAL
										|| type2 == Type.INTEGER)) {
							return multiplyNumbers(args[0], type1, args[1],
									type2);
						}
						return null;
					}

					private Object multiplyNumbers(Object number1, Type type1,
							Object number2, Type type2) {
						return toBigDecimal(number1, type1).multiply(
								toBigDecimal(number2, type2));
					}

					private BigDecimal toBigDecimal(Object number, Type type) {
						return type == Type.INTEGER ? new BigDecimal(
								(BigInteger) number) : (BigDecimal) number;
					}

				}));
		functions.put("/", new Function<Object>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if ((type1 == Type.DECIMAL || type1 == Type.INTEGER)
								&& (type2 == Type.DECIMAL
										|| type2 == Type.INTEGER)) {
							return divideNumbers(args[0], type1, args[1],
									type2);
						}
						return null;
					}

					private Object divideNumbers(Object number1, Type type1,
							Object number2, Type type2) {
						return toBigDecimal(number1, type1).divide(toBigDecimal(
								number2, type2));
					}

					private BigDecimal toBigDecimal(Object number, Type type) {
						return type == Type.INTEGER ? new BigDecimal(
								(BigInteger) number) : (BigDecimal) number;
					}

				}));
		functions.put("^", new Function<Object>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if ((type1 == Type.DECIMAL || type1 == Type.INTEGER)
								&& (type2 == Type.DECIMAL
										|| type2 == Type.INTEGER)) {
							return pow(args[0], type1, args[1], type2);
						}
						return null;
					}

					private Object pow(Object number1, Type type1,
							Object number2, Type type2) {
						return toBigDecimal(number1, type1).pow(toBigDecimal(
								number2, type2).intValue());
					}

					private BigDecimal toBigDecimal(Object number, Type type) {
						return type == Type.INTEGER ? new BigDecimal(
								(BigInteger) number) : (BigDecimal) number;
					}

				}));
		functions.put("%", new Function<Object>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if ((type1 == Type.DECIMAL || type1 == Type.INTEGER)
								&& (type2 == Type.DECIMAL
										|| type2 == Type.INTEGER)) {
							return mod(args[0], type1, args[1], type2);
						}
						return null;
					}

					private Object mod(Object number1, Type type1,
							Object number2, Type type2) {
						return toBigDecimal(number1, type1).remainder(
								toBigDecimal(number2, type2));
					}

					private BigDecimal toBigDecimal(Object number, Type type) {
						return type == Type.INTEGER ? new BigDecimal(
								(BigInteger) number) : (BigDecimal) number;
					}

				}));
		functions.put("=", new Function<Boolean>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if (type1 == type2) {
							return args[0].equals(args[1]);
						} else if (type1 == Type.CHARACTER
								&& type2 == Type.STRING) {
							return Character.toString((char) args[0]).equals(
									args[1]);
						} else if (type2 == Type.CHARACTER
								&& type1 == Type.STRING) {
							return Character.toString((char) args[1]).equals(
									args[0]);
						}
						return args[0].equals(args[1]); // TODO
					}

				}));
		functions.put("<", new Function<Boolean>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						return Function.compare(args[0], args[1]) < 0;
					}

				}));
		functions.put(">", new Function<Boolean>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Boolean>() {

					@Override
					public Boolean run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						return Function.compare(args[0], args[1]) > 0;
					}

				}));
		functions.put("&", new Function<Object>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if (type1 == Type.INTEGER && type2 == Type.INTEGER) {
							return Function.toInteger(args[0]).and(Function
									.toInteger(args[1]));
						}
						return Function.toBoolean(args[0]) && Function
								.toBoolean(args[1]);
					}

				}));
		functions.put("|", new Function<Object>(new Type[] { Type.OBJECT,
				Type.OBJECT }, new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if (type1 == Type.INTEGER && type2 == Type.INTEGER) {
							return Function.toInteger(args[0]).or(Function
									.toInteger(args[1]));
						}
						return Function.toBoolean(args[0]) || Function
								.toBoolean(args[1]);
					}

				}));
		functions.put("!", new Function<Object>(new Type[] { Type.OBJECT },
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args)
									throws LoopFlag {
						Type type1 = Type.getMatch(args[0]);
						Type type2 = Type.getMatch(args[1]);
						if (type1 == Type.INTEGER && type2 == Type.INTEGER) {
							return Function.toInteger(args[0]).not();
						}
						return !Function.toBoolean(args[0]);
					}

				}));

		// TODO add built-ins
	}

	public A_RayCode(String code, File input) {
		this(code, new ArrayList<>(), new InputIterator(input),
				new StringBuilder(), new MutableObject(null));
	}

	public A_RayCode(String code, String input) {
		this(code, new ArrayList<>(), new InputIterator(input),
				new StringBuilder(), new MutableObject(null));
	}

	protected A_RayCode(String code, List<ArrayItem> memory,
			InputIterator input, StringBuilder output,
			MutableObject temporaryVariable) {
		this.code = code.replaceAll(",", "}{");
		this.input = input;
		this.output = output;
		this.memory = memory;
		this.temporaryVariable = temporaryVariable;
	}

	public String runAndGetOutput() {
		input.reset();
		output.delete(0, output.length());
		memory.clear();

		try {
			run();
		} catch (LoopFlag e) {
			return "Loop flag not resolved\n";
		}

		return output.toString();
	}

	@Override
	public String toString() {
		return code;
	}

	protected FunctionResult run() throws LoopFlag {
		// run
		FunctionResult value = new FunctionResult(null, 0);
		do {
			value = run(value.currentIndex);
		} while (value.result != null);
		return value;
	}

	protected FunctionResult run(int index) throws LoopFlag {
		if (index >= code.length()) {
			return new FunctionResult(null, index);
		}
		char c = code.charAt(index);
		String functionName = Character.toString(c);
		int endIndex = index;
		final String functionCode;
		Function<?> function;
		switch (c) {
		case '$':
			endIndex = code.indexOf('$', ++index);
			functionName = code.substring(index, endIndex);
			break;
		case '#':
			endIndex = code.indexOf('{', ++index);
			functionName = code.substring(index, endIndex);
			functionCode = code.substring(endIndex + 1,
					endIndex = indexOfMatchingClose(endIndex, '}'));
			function = new Function<Object>(new Type[] {},
					new RunnableFunction<Object>() {

						@Override
						public Object run(List<ArrayItem> memory,
								InputIterator input, StringBuilder output,
								MutableObject temporaryVariable,
								Object[] args) {
							return new A_RayCode(functionCode, memory, input,
									output, temporaryVariable);
						}

					});
			functions.put(functionName, function);
			return new FunctionResult(function.run(memory, input, output,
					temporaryVariable, new Object[] {}), endIndex + 1);
		case '{':
			return new FunctionResult(new A_RayCode(code.substring(++index,
					endIndex = indexOfMatchingClose(index - 1, '}')), memory,
					input, output, temporaryVariable), endIndex + 1);
		case '"':
			endIndex = code.indexOf('"', ++index);
			return new FunctionResult(code.substring(index, endIndex), endIndex
					+ 1);
		case '\'':
			endIndex = code.indexOf('\'', ++index);
			try {
				return new FunctionResult(new BigDecimal(code.substring(index,
						endIndex)), endIndex + 1);
			} catch (NumberFormatException e) {
				return new FunctionResult(null, endIndex + 1);
			}
		case '[':
			endIndex = indexOfMatchingClose(index, ']');
			return new FunctionResult(createNewArray(code.substring(index + 1,
					endIndex++)), endIndex);
		}
		index = endIndex;
		function = functions.get(functionName);
		int numOfArguments = function.getParameterTypes().length;
		Object[] args = new Object[numOfArguments];
		index++;
		for (int i = 0; i < numOfArguments; i++) {
			FunctionResult argResult = run(index);
			args[i] = argResult.result;
			index = argResult.currentIndex;
		}
		return new FunctionResult(function.run(memory, input, output,
				temporaryVariable, args), index);
	}

	private List<ArrayItem> createNewArray(String substring) throws LoopFlag {
		String[] array = substring.split("\\}\\{");
		List<ArrayItem> result = new ArrayList<>();
		for (String string : array) {
			result.add(new ArrayItem(new A_RayCode(string, memory, input,
					output, temporaryVariable).run().result, Type.OBJECT));
		}
		return result;
	}

	private int indexOfMatchingClose(int index, char close) {
		char open = code.charAt(index);
		for (int i = index + 1, count = 1; i < code.length(); i++) {
			if (code.charAt(i) == open) {
				count++;
			} else if (code.charAt(i) == close) {
				count--;
			}
			if (count == 0) {
				return i;
			}
		}
		return -1; // TODO error
	}

	protected static class FunctionResult {

		protected final Object result;
		protected final int currentIndex;

		protected FunctionResult(Object result, int currentIndex) {
			this.result = result;
			this.currentIndex = currentIndex;
		}

	}

}