package jar.enums;

import jar.connector.BrowserConnector;
import jar.connector.FireFoxConnector;

public enum BrowserEnum {

	FireFox(new FireFoxConnector()),
	InternetExplorer(null),
	GoogleChrome(null);

	private BrowserConnector con;
	private BrowserEnum(BrowserConnector con){
		this.con = con;
	}

	public void connect(){
		con.setUp();
	}
}
