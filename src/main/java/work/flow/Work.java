package work.flow;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import work.room.Room;
import work.utils.Constants;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 2018/7/2
 * @author dylan.
 * Home: http://www.devdylan.cn
 */
public class Work {
	private LinkedHashMap<String, WorkFlow> flows;
	private ScheduledExecutorService service;

	public static Work getInstance() {
		return Work.WorkInstance.INSTANCE.getInstance();
	}

	/**
	 * Add room to work flow
	 * @param room game room
	 */
	public boolean add(Room room) throws NoSuchFieldException, IllegalAccessException {
		// get first instance
		Field tail = null;
		WorkFlow tailWorkFlow = null;
		try {
			tail = flows.getClass().getDeclaredField("tail");
			tail.setAccessible(true);
			Map.Entry entry = (Map.Entry) tail.get(flows);
			tailWorkFlow = (WorkFlow) entry.getValue();
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		if ( tailWorkFlow != null && tailWorkFlow.getRooms().size() >= Constants.ROOM_PER_WORKFLOW ) {
			WorkFlow workFlow = new WorkFlow();
			flows.put(workFlow.getIdentifier(), workFlow);
			service.scheduleAtFixedRate(workFlow, 0, 1, TimeUnit.SECONDS);

			tailWorkFlow = workFlow;
		}

		return tailWorkFlow != null && tailWorkFlow.addAndStartRoom(room);
	}

	private enum WorkInstance {
		/**
		 * Instance for work flow
		 */
		INSTANCE;

		private Work instance;

		WorkInstance() {
			instance = new Work();
			// work flows
			instance.flows = new LinkedHashMap<>(Constants.WORKFLOW_PER_WORK);
			// default work flow
			WorkFlow workFlow = new WorkFlow();
			instance.flows.put(workFlow.getIdentifier(), workFlow);

			instance.service = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("game-timer-pool-%d").daemon(true).build());
			instance.service.scheduleAtFixedRate(workFlow, 0, 1, TimeUnit.SECONDS);
		}

		public Work getInstance() {
			return instance;
		}
	}
}
