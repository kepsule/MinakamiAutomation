package jar.operator;

import static com.codeborne.selenide.Selenide.*;

import java.util.List;

import jar.App;
import jar.bean.OperationDataBean;
import jar.util.AppException;

/** 解析済みデータ実行 */
public class FileDataExecutor {

	/** ファイル解析後のデータを受け取り、実行処理を呼び出す。 */
	public void fileDataExecute(List<OperationDataBean> odbList)
			throws InterruptedException, AppException {

		/* データがない場合、処理終了 */
		if (odbList == null || odbList.isEmpty()) {
			return;
		}

		/* ブラウザ選択 */
		odbList.get(0).getBrowser().connect();

		/* 接続 */
		open(odbList.get(0).getStartUrl());

		/* 操作 */
		odbList.iterator().forEachRemaining(
			odb -> {
				try {

					/* 操作処理 */
					odb.getOperationEnum().operate(odb);

					/* アプリケーション定義例外発生時
					 * 失敗した処理内容をセットし、共通処理を呼び出す */
				} catch (AppException e) {

					e.setOperationDataBean(odb);
					App.errHandling(e);
				}
			});

		close();
	}
}
