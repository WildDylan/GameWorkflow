package work.room;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.flow.WorkFlow;
import work.player.Player;
import work.step.AbstractGameStep;
import work.step.State;
import work.utils.MillisecondClock;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 2018/6/29
 * @author dylan.
 * Home: http://www.devdylan.cn
 */
public class Room {
	Logger logger = LoggerFactory.getLogger(Room.class);
	/**
	 * Add for clock changed
	 */
	private PropertyChangeListener listener;
	/**
	 * Room identifier
	 */
	private String roomId;
	/**
	 * Players who join room
	 */
	private LinkedHashMap<String, Player> players;
	/**
	 * Game steps
	 */
	private LinkedHashMap<String, AbstractGameStep> steps;
	/**
	 * Game state
	 */
	private State state;
	/**
	 * Game played game
	 */
	private long gameStartTime;
	/**
	 * Game played end game
	 */
	private long gameEndTime;
	/**
	 * owner work flow
	 */
	private WorkFlow workFlow;

	public Room(String roomId) {
		this.roomId = roomId;

		this.players = new LinkedHashMap<>();
		this.steps = new LinkedHashMap<>();
		this.state = State.WAIT;
		logger.debug("init rooM with id: " + roomId);
	}

	public void addPlayer(Player player) {
		logger.debug(getRoomId() + " addPlayer: " + player.getUserId());
		if ( players.size() > 0 ) {
			// Link
			Field tail = null;
			try {
				tail = players.getClass().getDeclaredField("tail");
				tail.setAccessible(true);
				Map.Entry entry = (Map.Entry) tail.get(players);
				Player playerTail = (Player) entry.getValue();
				playerTail.setNextPlayer(player);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		players.put(player.getUserId(), player);
	}

	public void addStep(AbstractGameStep step) {
		logger.debug(getRoomId() + " addStep: " + step.getName());
		step.setRoomId(getRoomId());
		steps.put(step.getName(), step);
	}

	public void removeStep(AbstractGameStep step) {
		logger.debug(getRoomId() + " removeStep: " + step.getName());
		steps.remove(step.getName());
	}

	public boolean start(Object data) {
		if ( state != State.WAIT || steps.size() < 1 || players.size() < 1 ) {
			logger.debug(getRoomId() + " start failure, check condition ! ");
			return false;
		}
		// player to steps
		setPlayerToSteps();

		// record  game time
		logger.debug(getRoomId() + " Game start! " + MillisecondClock.now());
		gameStartTime = MillisecondClock.now();

		// start steps one by one
		state = State.RUNNING;

		// add clock listener
		this.listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				ticktock();
			}
		};
		workFlow.addClockListener(listener);

		logger.debug(getRoomId() + " Listener added ! ");
		logger.debug(getRoomId() + " Ready for start game ! ");

		// Begin game, find first room in link hash map
		return startFirstWaitStep(data);
	}

	private void ticktock() {
		// Check running step rules
		if (checkAllStepFinished()) {
			gameEndTime = MillisecondClock.now();
			state = State.FINISHED;

			logger.debug(getRoomId() + " Game end! cost: " + (gameEndTime - gameStartTime));

			if ( Integer.valueOf(roomId) % 1000 == 0 ) {
				logger.info(roomId + " cost " + (gameEndTime - gameStartTime));
			}

			players.clear();
			steps.clear();

			workFlow.removeClockListener(listener);
			workFlow.getRooms().remove(getRoomId());

			return;
		}

		AbstractGameStep stepRunning = getFirstRunningStep();
		if ( stepRunning != null ) {
			// Next step
			Pair<Boolean, Object> pair = stepRunning.check();
			if ( pair.getValue0() ) {
				stepRunning.setState(State.FINISHED);
				logger.debug(getRoomId() + " Step: " + stepRunning.getName() + " was finished");
				// open next
				startFirstWaitStep(pair.getValue1());
			}
		}
	}

	/**
	 * Start wait step with data
	 * @param data data to next step
	 * @return boolean
	 */
	private boolean startFirstWaitStep(Object data) {
		AbstractGameStep step = getFirstWaitStep();
		return step != null && step.start(data);
	}

	private AbstractGameStep getFirstWaitStep() {
		for (Map.Entry<String, AbstractGameStep> entry : steps.entrySet()) {
			AbstractGameStep step = entry.getValue();
			if ( step.getState() == State.WAIT ) {
				return step;
			}
		}
		return null;
	}

	private AbstractGameStep getFirstRunningStep() {
		for (Map.Entry<String, AbstractGameStep> entry : steps.entrySet()) {
			AbstractGameStep step = entry.getValue();
			if ( step.getState() == State.RUNNING ) {
				return step;
			}
		}
		return null;
	}

	private boolean checkAllStepFinished() {
		for (Map.Entry<String, AbstractGameStep> entry : steps.entrySet()) {
			AbstractGameStep step = entry.getValue();
			if ( step.getState() != State.FINISHED ) {
				return false;
			}
		}
		return true;
	}

	private void setPlayerToSteps() {
		for (Map.Entry<String, AbstractGameStep> entry : steps.entrySet()) {
			AbstractGameStep step = entry.getValue();
			step.setPlayers(players);
		}
	}

	/* Getter and setters */

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public PropertyChangeListener getListener() {
		return listener;
	}

	public void setListener(PropertyChangeListener listener) {
		this.listener = listener;
	}

	public WorkFlow getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkFlow workFlow) {
		this.workFlow = workFlow;
	}
}
