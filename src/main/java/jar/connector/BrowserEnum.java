package jar.connector;

public enum BrowserEnum {

	/** FireFox以外未実装 */
	FireFox(new FireFoxConnector()),
	InternetExplorer(null),
	GoogleChrome(null);

	private BrowserConnector con;
	private BrowserEnum(BrowserConnector con){
		this.con = con;
	}

	/** 接続処理<p>
	 * 接続の際の共通処理、各ブラウザの接続処理呼び出し</p> */
	public void connect(){
		con.setUp();
	}
}
