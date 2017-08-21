package jar.notification;

import java.io.IOException;

import org.riversun.slacklet.SlackletService;

import jar.logger.ResultReader;

/** Slack通知処理 */
public class SlackNotifier {

	private static final SlackNotifier instance = new SlackNotifier();
	private SlackNotifier() {}
	public static SlackNotifier getInstance() {return instance;}

	/** Slackで通知する。 */
	public void notifyBySlack() throws IOException {

		SlackletService slackService =
				new SlackletService(NotifierConst.slackToken);
		slackService.start();

	    /* 結果読み取りクラスを生成 */
	    ResultReader result = ResultReader.create();

	    /* 実行結果出力。成功・失敗・成功率を出力・送信する。 */
	    StringBuilder sb = new StringBuilder();
	    result.getReadResultMap().
	    	forEach((k, v) -> sb.append(k + " : " + v + " "));
	    slackService.sendMessageTo(NotifierConst.channelName, sb.toString() + "\n");
	    slackService.sendMessageTo(
	    		NotifierConst.channelName,
	    		"SUCCESS RATE : " +
	    		result.getSuccessPercentage().toString() + "%");

		slackService.stop();
	}
}
