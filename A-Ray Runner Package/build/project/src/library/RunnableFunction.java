package library;

import java.util.List;
import java.util.Map;

public interface RunnableFunction<T> {

	public T run(List<Object> memory, InputIterator input, StringBuilder output,
			MutableObject temporaryVariable, Map<String, Object> variables,
			Object[] args) throws LoopFlag;

}
