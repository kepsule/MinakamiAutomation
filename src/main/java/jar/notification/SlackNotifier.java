package jar.notification;

import java.io.IOException;

import org.riversun.slacklet.SlackletService;

import jar.App;
import jar.logger.ResultReader;

/** Slack通知処理 */
public class SlackNotifier {

	private static final SlackNotifier instance = new SlackNotifier();
	private SlackNotifier() {}
	public static SlackNotifier getInstance() {return instance;}

	/** Slackのトークンを指定 */
	private static final String slackToken =
			"xoxp-209416897524-209292195411-223791639047-9305b632ba04d36f4650f79e66dcafd3";

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
