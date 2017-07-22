package jar.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import jar.analyzer.ExcelAnalyzer;
import jar.bean.OperationDataBean;
import jar.operator.FileDataExecutor;
import jar.util.AppException;

public class AppController {

	public void execute(Path testDataPath, String sheetName)
			throws EncryptedDocumentException, InvalidFormatException,
					FileNotFoundException, IOException, InterruptedException, AppException {

		/* パスのファイルからBeanに変換 */
		List<OperationDataBean> odbList =
				new ExcelAnalyzer().analyzeExcel(testDataPath, sheetName);
		new FileDataExecutor().fileDataExecute(odbList);
	}
}
