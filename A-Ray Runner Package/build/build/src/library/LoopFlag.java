package library;

public class LoopFlag extends Exception {

	private static final long serialVersionUID = 2275239478370374878L;
	
	private final Action action;
	
	public LoopFlag(Action action) {
		this.action = action;
	}
	
	public Action getAction() {
		return action;
	}

}
