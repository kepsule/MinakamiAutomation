package jar.executor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import jar.App;
import jar.analyzer.excel.ExcelAnalyzer;
import jar.bean.OperationDataBean;
import jar.logger.MinakamiLogger;
import jar.logger.ResultRecorder;
import jar.notification.ConditionEnum;
import jar.operator.FileDataExecutor;
import jar.util.AppException;

/** ファイル実行 */
public class FileExecutor {

	/** Excelファイル実行 */
	public void executeFile(Path testDataPath)
			throws EncryptedDocumentException, InvalidFormatException,
					FileNotFoundException, IOException, InterruptedException, AppException {

		/* SheetにのIteratorに変換 */
		Iterator<Sheet> iter =
				WorkbookFactory.create(
						new FileInputStream(testDataPath.toFile())).sheetIterator();

		/* 全シートを順次実行 */
		iter.forEachRemaining(

			sheet -> {

				try {

					/* パスのファイルからBeanに変換 */
					MinakamiLogger.info(
							"実行開始：" + testDataPath.toString() + sheet.getSheetName());
					List<OperationDataBean> odbList =
							new ExcelAnalyzer().analyzeExcel(testDataPath, sheet.getSheetName());
					new FileDataExecutor().fileDataExecute(odbList);
					ResultRecorder.getInstance().resultRecord(
							testDataPath, sheet, ConditionEnum.SUCCESS);

				} catch (Throwable t) {

					/* エラー発生時、トレースを出力し次のシートへ */
					try {
						ResultRecorder.getInstance().resultRecord(
								testDataPath, sheet, ConditionEnum.FAILED);

					} catch (IOException e) {
						e.printStackTrace();
					}
					App.errHandling(t);
				}
				MinakamiLogger.info("実行開終了：" + testDataPath.toString() + sheet.getSheetName());
			});

	}

}
