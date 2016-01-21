package library;

import java.util.List;

public interface RunnableFunction<T> {

	public T run(List<ArrayItem> memory, InputIterator input,
			StringBuilder output, MutableObject temporaryVariable,
			Object[] args) throws LoopFlag;

}