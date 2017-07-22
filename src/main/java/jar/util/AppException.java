package jar.util;

import java.util.function.Supplier;

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

	public void setOperationDataBean(OperationDataBean odb) {
		this.odb = odb;
	}
	public AppException(){}
	public AppException(String name){
		this.name = name;
	}
	public AppException(String name, OperationDataBean odb) {
		this.name = name;
		this.odb = odb;
	}
	public AppException(String name, String message) {
		this.name = name;
		this.message = message;
	}
	public AppException(String name, String message, OperationDataBean odb) {
		this.name = name;
		this.message = message;
		this.odb = odb;
	}

	public Supplier<String> getErrMessage =
		() -> "throws：" + name + "\nDetail：" + message;

	public Supplier<String> getOperationData =
		() ->
			"elementName is :" +
			odb.getElementName() +
			"\nelementTag is : " +
			odb.getElementEnum().name();

}
