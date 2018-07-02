package work.step;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.player.Player;

/**
 * 2018/6/29
 * @author dylan.
 * Home: http://www.devdylan.cn
 */
public class EveryOneCountDownStep extends AbstractGameStep {
	Logger logger = LoggerFactory.getLogger(EveryOneCountDownStep.class);
	/**
	 * every one has 30 second
	 */
	private Integer everyOneTime = 30;
	private Integer count = 0;
	private Player currentPlayer;

	public EveryOneCountDownStep(String name, Object data) {
		super(name, data);
	}

	@Override
	public boolean start(Object data) {
		// set default count down user
		currentPlayer = this.getPlayers().entrySet().iterator().next().getValue();
		return super.start(data);
	}

	@Override
	public Pair<Boolean, Object> check() {
		if ( count == 0 ) {
			currentPlayer.openVoice();
			logger.debug(getRoomId() + " " + currentPlayer.getUserId() + " open voice!");
		}

		logger.debug(getRoomId() + " " + currentPlayer.getUserId() + " in voicing " + (everyOneTime - count));
		count ++;

		if ( everyOneTime <= count ) {
			logger.debug(getRoomId() + " " + currentPlayer.getUserId() + " close voice!");
			currentPlayer.closeVoice();
			count = 0;
			Player nextPlayer = currentPlayer.getNextPlayer();
			if ( nextPlayer != null ) {
				currentPlayer = nextPlayer;
			} else {
				logger.debug(getRoomId() + " EveryOne voice ended");
				return Pair.with(true, getData());
			}
		}

		return Pair.with(false, getData());
	}
}
