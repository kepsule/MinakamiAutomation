package jar.util;

import jar.bean.OperationDataBean;

/**
 * 当アプリケーションが出力する例外
 * 当例外を継承する
 * @author Keisuke
 *
 */
public class AppException extends Exception {

	private String name;
	private OperationDataBean odb;

	public AppException(){}
	public AppException(String name){
		this.name = name;
	}
	public AppException(String name, String message) {
		super(message);
		this.name = name;
	}

	public AppException(String name, String message, Throwable cause) {
		super(message, cause);
		this.name = name;
	}

	public void setOperationDataBean(OperationDataBean odb) {
		this.odb = odb;
	}

	/** エラーメッセージ取得 */
	public String getErrMessage() {
		return "throws：" + name + "\nDetail：" + getMessage();
	}

	/** 処理内容取得 */
	public String getOperationData() {

		return
		"elementName is :" +
			odb.getElementName() +
			"\nelementTag is : " +
			odb.getElementEnum().name();
	}

}
