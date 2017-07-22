package jar.analyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import jar.bean.CommonDataBean;
import jar.bean.OperationDataBean;
import jar.constant.ExcelConst;
import jar.enums.BrowserEnum;
import jar.enums.ElementEnum;
import jar.enums.ExcelDataEnum;
import jar.enums.OperationEnum;

public class ExcelAnalyzer {

	public List<OperationDataBean> analyzeExcel(Path excelPath, String sheetName)
				throws EncryptedDocumentException, InvalidFormatException,
						FileNotFoundException, IOException {

		/* PathからExcelデータに変換 Sheetを指定 */
		Workbook wb = WorkbookFactory.create(
				new FileInputStream(excelPath.toFile()));
		Sheet sheet = wb.getSheet(sheetName);

		/* ブラウザ設定 */
		BrowserEnum browser =
				BrowserEnum.valueOf(
						sheet.getRow(
								ExcelDataEnum.BROWSER_CELL.getRow())
									.getCell(ExcelDataEnum.BROWSER_CELL.getCol())
										.getStringCellValue());

		/* URL設定 */
		String startUrl =
				sheet.getRow(
					ExcelDataEnum.URL_CELL.getRow())
						.getCell(ExcelDataEnum.URL_CELL.getCol())
							.getStringCellValue();

		/* エビデンス格納パス */
		Path evidenceFolder;
		String evidence =
			sheet.getRow(
				ExcelDataEnum.EVIDENCE_CELL.getRow())
					.getCell(ExcelDataEnum.EVIDENCE_CELL.getCol(),
							Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)
						.getStringCellValue();
		if ("".equals(evidence)) {
			evidenceFolder = Paths.get(".");
		} else {
			evidenceFolder = Paths.get(evidence);
		}

		/* エビデンスフォルダ生成 */
		 evidenceFolder.toFile().mkdir();
		 evidenceFolder.resolve(excelPath.getFileName()).toFile().mkdir();
		 evidenceFolder.resolve(
				 excelPath.getFileName()).resolve(sheetName).toFile().mkdir();
		 evidenceFolder = evidenceFolder
				 .resolve(excelPath.getFileName()).resolve(sheetName);

		 /* DBの情報 */
		 String dbPath =
				 getCellValueAsString.apply(
				 sheet.getRow(
					ExcelDataEnum.DBPATH_CELL.getRow())
					.getCell(ExcelDataEnum.DBPATH_CELL.getCol(),
							Row.MissingCellPolicy.RETURN_NULL_AND_BLANK));
		 String dbUser =
				 getCellValueAsString.apply(
				 sheet.getRow(
					ExcelDataEnum.DBUSER_CELL.getRow())
					.getCell(ExcelDataEnum.DBUSER_CELL.getCol(),
							Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
		 String dbPassword =
				 getCellValueAsString.apply(
				 sheet.getRow(
					ExcelDataEnum.DBPASSWORD_CELL.getRow())
					.getCell(ExcelDataEnum.DBPASSWORD_CELL.getCol(),
							Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));

		CommonDataBean cdb =
				new CommonDataBean(excelPath, sheetName, browser, startUrl,
						evidenceFolder, dbPath, dbUser, dbPassword);

		/* 操作内容をOperationDataBeanに詰め、FileDataBeanに設定 */
		int colCounter = ExcelDataEnum.START_CELL.getRow();

		/* 最初のCellを取得 */
		Cell cell =
				sheet.getRow(ExcelDataEnum.START_CELL.getRow()).getCell(
				colCounter, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

		List<OperationDataBean> odbList = new ArrayList<>();

		/* 操作内容がブランク、もしくはENDのとき読み取り終了 */
		while(
				!(
				"".equals(cell.getStringCellValue())
				||
				ExcelConst.END_POINT.equals(cell.getStringCellValue()))) {

			/* 操作 */
			OperationEnum oe =
				OperationEnum.valueOf(
					sheet.getRow(colCounter)
						.getCell(ExcelConst.OPERATION_CELL,
								Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
							.getStringCellValue());

			/* 要素のタイプ */
			ElementEnum elm = null;
			String element = sheet.getRow(colCounter)
					.getCell(ExcelConst.TAG_CELL,
							Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getStringCellValue();
			if ("".equals(element)) {
				elm = ElementEnum.unused;
			} else if (!"".equals(element)) {
				elm = ElementEnum.valueOf(element);
			}

			/* 要素名 */
			Cell nameCell =
				sheet.getRow(colCounter)
						.getCell(ExcelConst.NAME_CELL,
								Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			String name;
			if (CellType.NUMERIC == cell.getCellTypeEnum()) {
				name = String.valueOf(cell.getNumericCellValue());
			} else {
				name = nameCell.getStringCellValue();
			}

			/* 要素の配列番号 */
			int num = 0;
			Cell numCell =
				sheet.getRow(colCounter)
						.getCell(ExcelConst.NUM_CELL,
								Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			num = numericChk.apply(numCell);

			/* 入力値 */
			String input =
				sheet.getRow(colCounter)
						.getCell(ExcelConst.INPUT_CELL,
								Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getStringCellValue();

			/* 操作Beanを生成し、次の行へ */
			odbList.add(new OperationDataBean(cdb, oe, elm, name, num, input));
			colCounter++;

			Row row = sheet.getRow(colCounter);
			if (row == null) {
				break;
			}

			cell = row
					.getCell(ExcelConst.OPERATION_CELL,
							Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		}

		return odbList;
	}

	/** NumericCellのcheck */
	private Function<Cell, String> getCellValueAsString =
		cell -> cell == null ? "" : cell.getStringCellValue();

	/** NumericCellのcheck */
	private Function<Cell, Integer> numericChk =
		cell ->
			CellType.NUMERIC == cell.getCellTypeEnum() ?
					(int) cell.getNumericCellValue() :
						CellType.BLANK == cell.getCellTypeEnum() ?
							0 :	Integer.valueOf(cell.getStringCellValue());
}
