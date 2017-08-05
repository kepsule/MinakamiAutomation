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
	private String message;
	private OperationDataBean odb;

	public AppException(){}
	public AppException(String name){
		this.name = name;
	}
	public AppException(String name, String message) {
		this.name = name;
		this.message = message;
	}

	public void setOperationDataBean(OperationDataBean odb) {
		this.odb = odb;
	}

	/** エラーメッセージ取得 */
	public String getErrMessage() {
		return "throws：" + name + "\nDetail：" + message;
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
