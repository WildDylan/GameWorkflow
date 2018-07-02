package work.step;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2018/6/29
 * @author dylan.
 * Home: http://www.devdylan.cn
 */
public class ContentDescribeStep extends AbstractGameStep {
	Logger logger = LoggerFactory.getLogger(ContentDescribeStep.class);

	private Integer totalTime = 10;
	private Integer count = 0;

	public ContentDescribeStep(String name, Object data) {
		super(name, data);
	}

	@Override
	public Pair<Boolean, Object> check() {
		logger.debug(getRoomId() + " checked! " + (totalTime - count));
		count ++;
		if ( totalTime <= count ) {
			return Pair.with(true, getData());
		}
		return Pair.with(false, getData());
	}

}
