import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.flow.Work;
import work.player.Player;
import work.room.Room;
import work.step.ContentDescribeStep;
import work.step.CountDownStep;
import work.step.EveryOneCountDownStep;

/**
 * 2018/6/29
 * @author  dylan.
 * Home: http://www.devdylan.cn
 */
public class WorkFlowTest {
	public static void main(String[] args) throws Exception {
		Logger logger = LoggerFactory.getLogger(WorkFlowTest.class);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {

				}
			}
		}).start();

		long size = 1000000;
		for (int i = 0; i < size; i++) {
			Room room = new Room(i + "");

			Player player = new Player("user1");
			room.addPlayer(player);

			Player player2 = new Player("user2");
			room.addPlayer(player2);

			Player player3 = new Player("user3");
			room.addPlayer(player3);

			ContentDescribeStep contentDescribeStep = new ContentDescribeStep("contentDescribe", null);
			room.addStep(contentDescribeStep);

			CountDownStep countDownStep = new CountDownStep("CountDownStep", null);
			room.addStep(countDownStep);

			EveryOneCountDownStep everyOneCountDownStep = new EveryOneCountDownStep("EveryOneCountDownStep", null);
			room.addStep(everyOneCountDownStep);

			EveryOneCountDownStep countDownStep2 = new EveryOneCountDownStep("EveryOneCountDownStep2", null);
			room.addStep(countDownStep2);

			ContentDescribeStep contentDescribeStep1 = new ContentDescribeStep("contentDescribeStep1", null);
			room.addStep(contentDescribeStep1);

			if ( Work.getInstance().add(room) ) {
				logger.debug("room: " + i + " add succeed!");
			} else {
				logger.debug("room: " + i + " add failure!");
			}
		}
	}
}
