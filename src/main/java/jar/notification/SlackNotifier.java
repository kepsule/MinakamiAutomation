package jar.notification;

import java.io.IOException;

import org.riversun.slacklet.SlackletService;

import jar.App;
import jar.logger.ResultReader;

public class SlackNotifier {

	private static final SlackNotifier sn = new SlackNotifier();
	private SlackNotifier() {}
	public static SlackNotifier getInstance() {return sn;}

	/** Slackのトークン名を指定 */
	private static final String slackToken =
			"xoxp-209416897524-209292195411-219579231879-0dd39dbf8d3fb4724cc662bf9180d24d";

	/** Slackのチャンネル名 */
	private static final String channelName = "testresult";

	public void notifyBySlack() throws IOException {

		SlackletService slackService =
				new SlackletService(slackToken);
	    try {
			slackService.start();
		} catch (IOException e) {
			e.printStackTrace();
			App.errHandling.accept(e);
		}

	    StringBuilder sb = new StringBuilder();
	    new ResultReader().readResult()
	    	.forEach((k, v) -> sb.append(k + " : " + v + " "));
	    slackService.sendMessageTo(channelName, sb.toString());

	    try {
			slackService.stop();
		} catch (IOException e) {
			App.errHandling.accept(e);
		}
	}
}
