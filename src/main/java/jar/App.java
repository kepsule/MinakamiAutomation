package jar;

import java.nio.file.Paths;
import java.util.function.Consumer;

import jar.controller.AppController;
import jar.logger.Logging;
import jar.util.AppException;

/**
 * メインメソッド実行クラス
 */
public class App {

	/** ステータスコード。
	 * 0以外は例外発生とする。 */
	private static int exitCode = 0;

	/** mainメソッド。
	 *  引数にExcelのファイル名、シート名を受けとり、
	 *  処理を実行する。
	 *  例外は当メソッドでcatchする。*/
	public static void main(String[] args) {

		try {

			new AppController().execute(
					Paths.get("src/main/resources/Sample001.xlsx"), "Sheet1");

		} catch (Throwable t) {

			errHandling.accept(t);
		}

		System.exit(exitCode);
	}

	/** エラー時の処理 */
	private static final Consumer<Throwable> errHandling =
		t -> {
			exitCode = 1;
			if (t instanceof AppException) {
				Logging.error(((AppException)t).getErrMessage.get());
				Logging.error(((AppException)t).getOperationData.get());
			}
			t.printStackTrace();
	};
}
