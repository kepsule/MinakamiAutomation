package jar;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import jar.executor.FileExecutor;
import jar.logger.MinakamiLogger;
import jar.logger.ResultRecorder;
import jar.notification.SlackNotifier;
import jar.util.AppException;

/**
 * メインメソッド実行クラス
 */
public class App {

	/** ステータスコード。
	 * 0以外は例外発生とする。 */
	private static int exitCode = 0;

	/** 成功時のメッセージ */
	private static final String successMessage =
			"all test success";

	/** mainメソッド。
	 *  引数にExcelのファイル名（の配列）を受けとり、
	 *  処理を実行する。。*/
	public static void main(String[] args) {

		/* 結果ファイル初期化 */
		try {
			ResultRecorder.getInstance().flushResultRecord();
		} catch (IOException e) {
			errHandling(e);
		}

		/* Excel実行 */
		IntStream.range(0, args.length).forEach(
				i -> 	{
					try {

						new FileExecutor().executeFile(Paths.get(args[i]));

					} catch (Throwable e) {
						errHandling(e);
					}
				});

		/* 結果のフィードバック
		 * ロガー書き込み及び通知 */
		try {
			if (exitCode == 0) {
				MinakamiLogger.info(successMessage);
				ResultRecorder.getInstance().resultRecord(successMessage);
			}
			SlackNotifier.getInstance().notifyBySlack();
		} catch (IOException e) {
			errHandling(e);
		}
		System.exit(exitCode);
	}

	/** エラー時の処理
	 * <p>
	 * エラートレースを出力し、exitCodeを変更する。
	 *  </p>
	 *  */
	public static final void errHandling(Throwable t) {

		exitCode = 1;
		MinakamiLogger.error(t.getMessage(), t);

		if (t instanceof AppException) {
			MinakamiLogger.error(((AppException)t).getErrMessage());
			MinakamiLogger.error(((AppException)t).getOperationData());
		}
	}
}
