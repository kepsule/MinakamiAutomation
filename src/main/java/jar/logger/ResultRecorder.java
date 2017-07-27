package jar.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Sheet;

import jar.enums.ConditionEnum;

public class ResultRecorder {

	private static final ResultRecorder rr = new ResultRecorder();
	private ResultRecorder() {}
	public static final ResultRecorder getInstance() {return rr;}

	/** 結果ファイル初期化 */
	public void flushResultRecord() throws IOException {

		try (BufferedWriter bw =
				new BufferedWriter(new FileWriter("testresult.txt"))) {
			bw.flush();
		}
	}

	/** 結果ファイル追記 */
	public void resultRecord(Path path, Sheet sheet, ConditionEnum ce) throws IOException {

		try (BufferedWriter bw =
				new BufferedWriter(new FileWriter("testresult.txt", true))) {
			bw.write(
					 path.toString() + " " + sheet.getSheetName() + "\n" + ce.name());
			bw.newLine();
		}
	}
}
