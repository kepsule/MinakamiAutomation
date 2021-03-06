package jar.connector;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;

/** FireFoxで実施の場合の前準備 */
public class FireFoxConnector implements BrowserConnector {

	/** GeckoDriverの相対パス */
	private static final String geckoPath =
			"lib\\geckodriver-v0.16.1-win64\\geckodriver.exe";

	@Override
	public void setUp() {

		Configuration.browser = WebDriverRunner.MARIONETTE;
		System.setProperty("webdriver.gecko.driver", geckoPath);
	}

}
