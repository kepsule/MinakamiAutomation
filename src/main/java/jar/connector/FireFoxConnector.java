package jar.connector;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;

import jar.constant.Const;

public class FireFoxConnector implements BrowserConnector {

	/** FireFoxで実施の場合の前準備 */
	@Override
	public void setUp() {

		Configuration.browser = WebDriverRunner.MARIONETTE;
		System.setProperty("webdriver.gecko.driver", Const.geckoPath);
	}

}
