package jar.operator;

import static com.codeborne.selenide.Selenide.*;

import java.util.Iterator;
import java.util.List;

import jar.bean.OperationDataBean;
import jar.util.AppException;

public class FileDataExecutor {

	public void fileDataExecute(List<OperationDataBean> odbList) throws InterruptedException, AppException {

		if (odbList == null || odbList.isEmpty()) {
			return;
		}

		/* ブラウザ選択 */
		odbList.get(0).getBrowser().connect();

		/* 接続 */
		open(odbList.get(0).getStartUrl());

		/* 操作 */
		Iterator<OperationDataBean> odbiter = odbList.iterator();
		while (odbiter.hasNext()) {
			OperationDataBean odb = odbiter.next();

			/* 操作処理 */
			try {
				odb.getOperationEnum().operate(odb);

				/* アプリケーション定義例外発生時 */
			} catch (AppException e) {

				e.setOperationDataBean(odb);
				throw e;
			}
		}

		close();
	}
}
