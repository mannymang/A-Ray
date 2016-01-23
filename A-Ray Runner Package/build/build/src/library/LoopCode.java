package library;

import java.math.BigInteger;
import java.util.List;

public class LoopCode extends A_RayCode {
	
	protected static final String X = "x";
	
	protected BigInteger count = BigInteger.ZERO;
	protected boolean out = false;
	
	private final Function<?> oldXFunction; 
	
	public LoopCode(String code, List<ArrayItem> memory, InputIterator input, StringBuilder output, MutableObject temporaryVariable) {
		super(code, memory, input, output, temporaryVariable);
		oldXFunction = functions.get(X);
		functions.put(X, new Function<BigInteger>(new Type[] {},
				new RunnableFunction<BigInteger>() {

					@Override
					public BigInteger run(List<ArrayItem> memory, InputIterator input,
							StringBuilder output, MutableObject temporaryVariable, Object[] args) {
						return count;
					}

				}));
	}
	
	@Override
	public FunctionResult run() throws LoopFlag {
		// run
		FunctionResult value = new FunctionResult(null, 0);
		do {
			value = run(value.currentIndex);
		} while (value.result != null);
		count = count.add(BigInteger.ONE);
		if (out) {
			functions.put(X, oldXFunction);
		}
		return new FunctionResult(!out, value.currentIndex);
	}
	
	@Override
	protected FunctionResult run(int index) throws LoopFlag {
		if (index >= code.length()) {
			return new FunctionResult(null, index);
		}
		switch (code.charAt(index)) {
		case 'b':
			out = true;
		case 'B':
			index = code.length();
		}
		return super.run(index); // TODO args
	}

}
