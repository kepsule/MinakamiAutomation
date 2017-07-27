package jar.logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import jar.enums.ConditionEnum;

/** 結果読み取り */
public class ResultReader {

	/** 読み込み対象ファイル */
	private static final String readFile = "testresult.txt";

	/** Mapで成功数、失敗数をカウントする */
	public Map<String, Integer> readResult() throws IOException {

		/* keyはConditionEnumの文字列とする */
		Map<String, Integer> map = new HashedMap<>();
		map.put(ConditionEnum.SUCCESS.name(), 0);
		map.put(ConditionEnum.FAILED.name(), 0);

		/* カウント操作を定義 */
		Consumer<String> increment =
				condition -> map.replace(condition, map.get(condition) + 1);

				/* 一行ずつカウントし、カウント数をMapに再格納する
				 * ConditionEnumにない文字列は対象外 */
		try (BufferedReader br =
				new BufferedReader(new FileReader(readFile))) {

			br.lines().filter(line -> map.containsKey(line))
				.forEach(line -> increment.accept(line));
		}

		return map;
	}
}
