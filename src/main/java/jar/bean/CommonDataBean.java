package jar.bean;

import java.nio.file.Path;
import java.util.Properties;

import jar.enums.BrowserEnum;

public class CommonDataBean {

	/** Excelファイルパス */
	private static Path excelPath;

	/** シート名 */
	private static String sheetName;

	/** ブラウザ */
	private static BrowserEnum browser;

	/** スタートのURL */
	private static String startUrl;

	/** エビデンス格納パス */
	private static Path evidenceFolder;

	/** 格納番号 */
	private static int evidenceNum = 1;

	/** DBのURL */
	private static String dbPath;

	/** DBのUser */
	private static String dbUser;

	/** DBのパスワード */
	private static String dbPassword;

	/**
	 * コンストラクタ
	 * staticではないデータを持ちまわる場合使用 */
	public CommonDataBean(CommonDataBean cdb) {
	}

	/** ビルダーパターンがいいな */
	/** コンストラクタ */
	public CommonDataBean(
			Path excelPath, String sheetName,
			BrowserEnum browser, String startUrl, Path evidenceFolder,
			String dbPath, String dbUSer, String dbPassword) {
		CommonDataBean.excelPath = excelPath;
		CommonDataBean.sheetName = sheetName;
		CommonDataBean.browser = browser;
		CommonDataBean.startUrl = startUrl;
		CommonDataBean.evidenceFolder = evidenceFolder;

		CommonDataBean.dbPath = dbPath;
		CommonDataBean.dbUser = dbUSer;
		CommonDataBean.dbPassword = dbPassword;
	}

	public BrowserEnum getBrowser() {
		return browser;
	}

	public String getStartUrl() {
		return startUrl;
	}

	/** ファイル名のみ取得 */
	public String getExcelFileName() {
		return excelPath.getFileName().toString();
	}

	public Path getExcelPath() {
		return excelPath;
	}

	public String getSheetName() {
		return sheetName;
	}

	public static Path getEvidenceFolder() {
		return evidenceFolder;
	}

	public static String getEvidenceNumAndIncrement(){
		return String.format("%04d", evidenceNum++);
	}

	public static String getDbPath() {
		return dbPath;
	}

	public static Properties getDbProps() {
		Properties props = new Properties();
		props.setProperty("user", dbUser);
		props.setProperty("password", dbPassword);
		return props;
	}
}
