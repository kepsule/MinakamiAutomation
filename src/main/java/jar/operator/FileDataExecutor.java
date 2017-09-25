package jar.operator;

import static com.codeborne.selenide.Selenide.*;

import java.util.Iterator;
import java.util.List;

import jar.bean.OperationDataBean;
import jar.util.AppException;

/** 解析済みデータ実行 */
public class FileDataExecutor {

	/** ファイル解析後のデータを受け取り、実行処理を呼び出す。 */
	public void fileDataExecute(List<OperationDataBean> odbList) throws InterruptedException, AppException {

		/* データがない場合、処理終了 */
		if (odbList == null || odbList.isEmpty()) {
			return;
		}

		/* ブラウザ選択 */
		odbList.get(0).getBrowser().connect();

		/* 接続 */
		open(odbList.get(0).getStartUrl());

		/* 操作処理 */
		Iterator<OperationDataBean> iter = odbList.iterator();
		while (iter.hasNext()) {
			OperationDataBean odb = iter.next();
			odb.getOperationEnum().operate(odb);
		}

		close();
	}
}
