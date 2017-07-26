package jar.logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import jar.enums.ConditionEnum;

public class ResultReader {

	public Map<ConditionEnum, Integer> readResult() throws IOException {

		Map<ConditionEnum, Integer> map = new HashedMap<>();
		map.put(ConditionEnum.SUCCESS, 0);
		map.put(ConditionEnum.FAILED, 0);

		Consumer<ConditionEnum> increment = ce -> map.replace(ce, map.get(ce) + 1);

		try (BufferedReader br =
				new BufferedReader(new FileReader("testresult.txt"))) {

			br.lines().forEach(
					line ->
					increment.accept(ConditionEnum.valueOf(line)));
		}
		return map;
	}
}
