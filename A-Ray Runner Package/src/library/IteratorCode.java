package library;

import java.util.List;

public class IteratorCode extends LoopCode {

	public IteratorCode(List<Object> array, String code,
			List<Object> memory, InputIterator input, StringBuilder output,
			MutableObject temporaryVariable) {
		super(code, memory, input, output, temporaryVariable);
		functions.put(LoopCode.X, new Function<Object>(new Type[] {},
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<Object> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return array.get(count.intValue());
					}

				}));
	}

}
