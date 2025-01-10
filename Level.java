
public enum Level {
	EASY(190),//100ms delay between moves
	MEDIUM(120),//50ms delay between moves
	HARD(70);//20ms delay between moves
	
	
	private final int delay;
	
	Level(int delay){
		this.delay = delay;
	}
	
	public int getDelay() {
		return delay;
	}
}
