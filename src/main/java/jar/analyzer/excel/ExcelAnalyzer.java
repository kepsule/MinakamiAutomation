package jar.analyzer.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
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
import jar.connector.BrowserEnum;
import jar.operator.ElementEnum;
import jar.operator.OperationEnum;
import jar.util.FileAnalyzerException;

/** Excel解析 */
public class ExcelAnalyzer {

	/** Pathで与えられたExcelファイルのSheet名をIteratorで返却する。 */
	public Iterator<Sheet> getSheetNameIterator(Path excelPath)
			throws InvalidFormatException, EncryptedDocumentException, FileNotFoundException, IOException{

		/* PathからExcelデータに変換 */
		return
				WorkbookFactory.create(
						new FileInputStream(excelPath.toFile())).sheetIterator();
	}

	/** Excelファイルを解析し、テスト実行データのBeanを生成する。
	 * @throws FileAnalyzerException */
	public List<OperationDataBean> analyzeExcel(Path excelPath, String sheetName)
				throws EncryptedDocumentException, InvalidFormatException,
						FileNotFoundException, IOException, FileAnalyzerException {

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

		/* cellのnull check処理 */
		Function<Cell, String> getCellValueAsString =
			cell -> cell == null ? "" : cell.getStringCellValue();

		/* エビデンス格納パス デフォルトはカレントディレクトリ */
		Path evidenceFolder;
		String evidence =
			getCellValueAsString.apply(
				sheet.getRow(
					ExcelDataEnum.EVIDENCE_CELL.getRow())
						.getCell(ExcelDataEnum.EVIDENCE_CELL.getCol(),
							Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
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
		int rowCounter = ExcelDataEnum.START_ROW.getRow();

		/* 最初のCellを取得 */
		Cell cell =
				sheet.getRow(ExcelDataEnum.START_ROW.getRow()).getCell(
				rowCounter, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

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
					sheet.getRow(rowCounter)
						.getCell(ExcelDataEnum.OPERATION_COL.getCol(),
								Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
							.getStringCellValue());

			/* 要素のタイプ */
			ElementEnum elm = null;
			String element = sheet.getRow(rowCounter)
					.getCell(ExcelDataEnum.TAG_COL.getCol(),
							Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getStringCellValue();
			if ("".equals(element)) {
				elm = ElementEnum.unused;
			} else if (!"".equals(element)) {
				elm = ElementEnum.valueOf(element);
			}

			/* 要素名 */
			Cell nameCell =
				sheet.getRow(rowCounter)
						.getCell(ExcelDataEnum.NAME_COL.getCol(),
								Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			String name;
			if (CellType.NUMERIC == cell.getCellTypeEnum()) {
				name = String.valueOf(cell.getNumericCellValue());
			} else {
				name = nameCell.getStringCellValue();
			}

			/* NumericCellのcheck */
			Function<Cell, Integer> numericChk =
				targetCell ->
					CellType.NUMERIC == targetCell.getCellTypeEnum() ?
							(int) targetCell.getNumericCellValue() :
								CellType.BLANK == targetCell.getCellTypeEnum() ?
									0 :	Integer.valueOf(targetCell.getStringCellValue());

			/* 要素の配列番号 */
			int num = 0;
			Cell numCell =
				sheet.getRow(rowCounter)
						.getCell(ExcelDataEnum.NUM_COL.getCol(),
								Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			num = numericChk.apply(numCell);

			/* 入力値 */
			String input =
				sheet.getRow(rowCounter)
						.getCell(ExcelDataEnum.INPUT_COL.getCol(),
								Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getStringCellValue();

			/* 操作Beanを生成し、次の行へ */
			odbList.add(new OperationDataBean(cdb, oe, elm, name, num, input));
			rowCounter++;
			Row row = sheet.getRow(rowCounter);
			if (row == null) {
				break;
			}

			/* 次の行のセルを取得 */
			cell = row
					.getCell(ExcelDataEnum.OPERATION_COL.getCol(),
							Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		}

		return odbList;
	}

}
