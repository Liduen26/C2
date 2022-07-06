package cCarre.genmap.events;

public class ChangeHeightEvent {
	int height;
	public ChangeHeightEvent(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}
}
