package work.flow;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import work.room.Room;
import work.utils.Constants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 2018/6/29
 * @author  dylan.
 * Home: http://www.devdylan.cn
 */
public class WorkFlow implements Runnable {

	private String identifier = UUID.randomUUID().toString();
	private AtomicLong clock;
	private PropertyChangeSupport changeSupport;
	private LinkedHashMap<String, Room> rooms;

	public WorkFlow() {
		clock = new AtomicLong(0L);
		changeSupport = new PropertyChangeSupport(this);
		rooms = new LinkedHashMap<>(Constants.ROOM_PER_WORKFLOW);
	}

	public void addClockListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removeClockListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void addRoom(Room room) {
		room.setWorkFlow(this);
		rooms.put(room.getRoomId(), room);
	}

	public boolean addAndStartRoom(Room room) throws NoSuchFieldException, IllegalAccessException {
		addRoom(room);
		return startRoom(room);
	}

	public boolean startRoomWithRoomId(String roomId) throws NoSuchFieldException, IllegalAccessException {
		return startRoom(rooms.get(roomId));
	}

	private boolean startRoom(Room room) throws NoSuchFieldException, IllegalAccessException {
		return room != null && room.start(null);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public AtomicLong getClock() {
		return clock;
	}

	public LinkedHashMap<String, Room> getRooms() {
		return rooms;
	}

	public void ticktock() {
		long oldValue = clock.longValue();
		long newValue = clock.incrementAndGet();
		changeSupport.firePropertyChange("clock", oldValue, newValue);
	}

	@Override
	public void run() {
		ticktock();
	}
}
