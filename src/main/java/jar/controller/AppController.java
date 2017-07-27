package jar.controller;

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
import jar.analyzer.ExcelAnalyzer;
import jar.bean.OperationDataBean;
import jar.enums.ConditionEnum;
import jar.logger.MinakamiLogger;
import jar.logger.ResultRecorder;
import jar.operator.FileDataExecutor;
import jar.util.AppException;

public class AppController {

	public void execute(Path testDataPath)
			throws EncryptedDocumentException, InvalidFormatException,
					FileNotFoundException, IOException, InterruptedException, AppException {

		Iterator<Sheet> iter =
				WorkbookFactory.create(
						new FileInputStream(testDataPath.toFile())).sheetIterator();

		iter.forEachRemaining(
				sheet -> {

					try {
						/* パスのファイルからBeanに変換 */

						MinakamiLogger.info("実行開始：" + testDataPath.toString() + sheet.getSheetName());
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
						App.errHandling.accept(t);
					}
					MinakamiLogger.info("実行開終了：" + testDataPath.toString() + sheet.getSheetName());
				}

				);



	}

//	public void execute(Path testDataPath, String sheetName)
//			throws EncryptedDocumentException, InvalidFormatException,
//					FileNotFoundException, IOException, InterruptedException, AppException {
//
//		/* パスのファイルからBeanに変換 */
//		List<OperationDataBean> odbList =
//				new ExcelAnalyzer().analyzeExcel(testDataPath, sheetName);
//		new FileDataExecutor().fileDataExecute(odbList);
//	}
}
