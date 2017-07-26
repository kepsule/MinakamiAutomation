package jar.notification;

import java.io.IOException;

import org.riversun.slacklet.SlackletService;

import jar.logger.ResultReader;

public class SlackNotifier {

	private static final SlackNotifier sn = new SlackNotifier();
	private SlackNotifier() {}
	public static SlackNotifier getInstance() {return sn;}

	public void notifyBySlack() throws IOException {

		SlackletService slackService = new SlackletService(
				"xoxp-209416897524-209292195411-208827104129-32d1a1228fc3bcab6fa82588e07d27e4");
	    try {
			slackService.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    String channelName = "testresult";

	    StringBuilder sb = new StringBuilder();
	    new ResultReader().readResult()
	    	.forEach((k, v) -> sb.append(k.name() + " : " + v + " "));
	    slackService.sendMessageTo(channelName, sb.toString());

	    try {
			slackService.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
