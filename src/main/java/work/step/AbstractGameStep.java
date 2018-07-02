package work.step;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.player.Player;

import java.util.*;

/**
 * 2018/6/29
 * @author dylan.
 * Home: http://www.devdylan.cn
 */
public abstract class AbstractGameStep {
	private Logger fatherLogger = LoggerFactory.getLogger("Step");
	/**
	 * step in room id
	 */
	private String roomId;
	/**
	 * step name
	 */
	private String name;
	/**
	 * step identifier
	 */
	private String identifier;
	/**
	 * data back to user in this step
	 */
	private Object data;
	/**
	 * Prev step's data
	 */
	private Object prevData;
	/**
	 * room state
	 */
	private State state;
	/**
	 * Players
	 */
	private LinkedHashMap<String, Player> players;

	private AbstractGameStep(String name, String identifier, Object data) {
		this.name = name;
		this.identifier = identifier;
		this.data = data;
		this.state = State.WAIT;
	}

	public AbstractGameStep(String name, Object data) {
		this(name, UUID.randomUUID().toString(), data);
	}

	private AbstractGameStep() {}

	public boolean start(Object data) {
		if ( state != State.WAIT ) {
			return false;
		}
		state = State.RUNNING;
		prevData = data;
		fatherLogger.debug(getRoomId() + " " + getName() + " Started!");
		return true;
	}

	/**
	 * check rules
	 * @return check rules every ticktock
	 */
	public abstract Pair<Boolean, Object> check();

	/* getter and setter */

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Object getPrevData() {
		return prevData;
	}

	public void setPrevData(Object prevData) {
		this.prevData = prevData;
	}

	public LinkedHashMap<String, Player> getPlayers() {
		return players;
	}

	public void setPlayers(LinkedHashMap<String, Player> players) {
		this.players = players;
	}
}
