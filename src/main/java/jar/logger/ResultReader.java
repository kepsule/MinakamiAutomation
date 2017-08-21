package jar.logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import jar.notification.ConditionEnum;

/** 結果読み取り */
public class ResultReader {

	/** 読み込み対象ファイル */
	private static final String readFile = "testresult.txt";

	private Map<String, Integer> readResultMap;

	private ResultReader() {}

	/** 結果読み取りインスタンス生成<p>
	 * 生成時点の成功・失敗数をフィールドに持つ。</p> */
	public static final ResultReader create()
			throws FileNotFoundException, IOException {

		ResultReader instance = new ResultReader();

		/* keyはConditionEnumの文字列とする
		 * SUCCESSを先に出したい */
		Map<String, Integer> map = new LinkedHashMap<>();
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

		instance.readResultMap = map;
		return instance;
	}

	/**
	 * 成功率算出<p>
	 * %表記に合わせ、小数点第３位四捨五入した値を算出する。</p> */
	public Double getSuccessPercentage() {

		double successCount = readResultMap.get(ConditionEnum.SUCCESS.name());
		double failedCount = readResultMap.get(ConditionEnum.FAILED.name());
		double result = successCount / (successCount + failedCount);

		return
				BigDecimal.valueOf(result).setScale(4, RoundingMode.HALF_UP)
					.multiply(new BigDecimal(100)).doubleValue();
	}

	/**  */
	public Map<String, Integer> getReadResultMap() {
		return readResultMap;
	}

}
