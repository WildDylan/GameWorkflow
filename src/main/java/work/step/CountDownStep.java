package work.step;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2018/6/29
 * @author dylan.
 * Home: http://www.devdylan.cn
 */
public class CountDownStep extends AbstractGameStep {
	Logger logger = LoggerFactory.getLogger(CountDownStep.class);

	private Integer countDownTime = 30;
	private Integer count = 0;

	public CountDownStep(String name, Object data) {
		super(name, data);
	}

	@Override
	public Pair<Boolean, Object> check() {
		logger.debug(getRoomId() + " checked! " + (countDownTime - count));
		count ++;
		if ( countDownTime <= count ) {
			return Pair.with(true, getData());
		}
		return Pair.with(false, getData());
	}
}
