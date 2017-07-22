package jar.bean;

import jar.enums.ElementEnum;
import jar.enums.OperationEnum;

public class OperationDataBean extends CommonDataBean {

	/** 操作 */
	private OperationEnum operationEnum;

	/** 要素 */
	private ElementEnum elementEnum;

	/** 要素名 */
	private String elementName;

	/** 要素番号 */
	private int targetNum = 0;

	/** 入力データ */
	private String inputData;

	/** コンストラクタ */
	public OperationDataBean(
			CommonDataBean cdb, OperationEnum oe,
			ElementEnum ee, String elmName, int targetnum, String input) {

		super(cdb);
		this.operationEnum = oe;
		this.elementEnum = ee;
		this.elementName = elmName;
		this.targetNum = targetnum;
		this.inputData = input;
	}


	public ElementEnum getElementEnum() {
		return elementEnum;
	}
	public String getElementName() {
		return elementName;
	}
	public int getTargetNum() {
		return targetNum;
	}
	public OperationEnum getOperationEnum() {
		return operationEnum;
	}
	public String getInputData() {
		return inputData;
	}
}
