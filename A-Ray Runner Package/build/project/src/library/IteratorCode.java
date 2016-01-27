package library;

import java.util.List;
import java.util.Map;

public class IteratorCode extends LoopCode {

	public IteratorCode(List<Object> array, String code, List<Object> memory,
			InputIterator input, StringBuilder output,
			MutableObject temporaryVariable, Map<String, Object> variables) {
		super(code, memory, input, output, temporaryVariable, variables);
		functions.put(LoopCode.X, new Function<Object>(new Type[] {},
				new RunnableFunction<Object>() {

					@Override
					public Object run(List<Object> memory, InputIterator input,
							StringBuilder output,
							MutableObject temporaryVariable,
							Map<String, Object> variables, Object[] args) {
						return array.get(count.intValue());
					}

				}));
	}

}
