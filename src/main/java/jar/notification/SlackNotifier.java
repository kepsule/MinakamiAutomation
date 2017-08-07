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

	/** Slackで通知する。 */
	public void notifyBySlack() throws IOException {

		SlackletService slackService =
				new SlackletService(NotifierConst.slackToken);
	    try {
			slackService.start();
		} catch (IOException e) {
			e.printStackTrace();
			App.errHandling(e);
		}

	    StringBuilder sb = new StringBuilder();
	    ResultReader.getInstance().readResult()
	    	.forEach((k, v) -> sb.append(k + " : " + v + " "));
	    slackService.sendMessageTo(NotifierConst.channelName, sb.toString());

	    try {
			slackService.stop();
		} catch (IOException e) {
			App.errHandling(e);
		}
	}
}
