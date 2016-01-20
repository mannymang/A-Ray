package library;

import java.util.List;

public class IteratorCode extends LoopCode {

	public IteratorCode(List<ArrayItem> array, String code,
			List<ArrayItem> memory, InputIterator input, StringBuilder output,
			MutableObject temporaryVariable) {
		super(code, memory, input, output, temporaryVariable);
		functions.put(LoopCode.X, new Function<Object>(new Type[] {},
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<ArrayItem> memory,
							InputIterator input, StringBuilder output,
							MutableObject temporaryVariable, Object[] args) {
						return array.get(count.intValue()).getValue();
					}

				}));
	}

}
