package cCarre.genmap.events;

public class RemoveLengthGrilleEvent {
private int x;
	
	public RemoveLengthGrilleEvent(int x) {
		this.x = x;
		System.out.println("evented");
	}

	public int getX() {
		return x;
	}
}
