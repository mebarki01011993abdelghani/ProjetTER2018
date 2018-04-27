package tkgate.modele;

/**
 * A connection to a component is given by a wire and a boolean stating if the
 * wire is connected by its head, otherwise, it is connected by its tail
 * 
 * @author severine
 *
 */

public class Connection {
	Wire wire;
	boolean head;

	public Connection(Wire wire, boolean head) {
		super();
		this.wire = wire;
		this.head = head;
	}

	public Wire getWire() {
		return wire;
	}

	public void setWire(Wire wire) {
		this.wire = wire;
	}

	public boolean isHead() {
		return head;
	}

	public void setHead(boolean head) {
		this.head = head;
	}

	public boolean equals(Connection c) {
		return (this.head == c.head && this.wire == c.wire);
	}

	public Component oppositeComponent() {
		if (head)
			return wire.getTail();
		else
			return wire.getHead();
	}

	@Override
	public String toString() {
		String s;
		if (head)
			s = "head";
		else
			s = "tail";
		return s + wire.toString();
	}
}
