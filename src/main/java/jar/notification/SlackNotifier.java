package jar.notification;

import java.io.IOException;

import org.riversun.slacklet.SlackletService;

import jar.App;
import jar.logger.ResultReader;

/** Slack通知処理 */
public class SlackNotifier {

	private static final SlackNotifier sn = new SlackNotifier();
	private SlackNotifier() {}
	public static SlackNotifier getInstance() {return sn;}

	/** Slackのトークンを指定 */
	private static final String slackToken =
			"xoxp-209416897524-209292195411-222590603987-2c1108d6a211222b9478ac568b74ace7";

	/** Slackのチャンネル名 */
	private static final String channelName = "testresult";

	/** Slackで通知する。 */
	public void notifyBySlack() throws IOException {

		SlackletService slackService =
				new SlackletService(slackToken);
	    try {
			slackService.start();
		} catch (IOException e) {
			e.printStackTrace();
			App.errHandling(e);
		}

	    StringBuilder sb = new StringBuilder();
	    ResultReader.getInstance().readResult()
	    	.forEach((k, v) -> sb.append(k + " : " + v + " "));
	    slackService.sendMessageTo(channelName, sb.toString());

	    try {
			slackService.stop();
		} catch (IOException e) {
			App.errHandling(e);
		}
	}
}
