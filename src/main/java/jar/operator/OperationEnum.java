package jar.operator;

import static com.codeborne.selenide.Selenide.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import jar.bean.CommonDataBean;
import jar.bean.OperationDataBean;
import jar.db.DbOperator;
import jar.util.AppException;
import jar.util.AssertException;

/** 操作Enum
 *  <p>
 *  Excelのインプットに対応する処理を定義する。
 *  </p>
 *  */
public enum OperationEnum {

	CLICK {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement(odb).click();
		}
	},
	DOUBLE_CLICK {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement(odb).doubleClick();
		}
	},
	SEND_KEYS {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement(odb).sendKeys(odb.getInputData());
		}
	},
	PRESS_ENTER {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement(odb).pressEnter();
		}
	},
	END {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
		}
	},
	VALUE_CHECK {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			/* inputと一致していなければ例外スロー */
			if (!odb.getInputData().equals(getElement(odb).val())) {
				throw new AssertException(
						"Expected is : " + odb.getInputData()
						+ "\nBut actual is :" + getElement(odb).val());
			}
		}
	},
	EXIST {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* inputと一致していなければ例外スロー */
			if (!getElement(odb).exists()) {
				throw new AssertException(
						"element is Not Exist");
			}
		}
	},
	NOT_EXIST {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* inputと一致していれば例外スロー */
			if (getElement(odb).exists()) {
				throw new AssertException(
						"element is Exist");
			}
		}
	},
	/** プルダウン */
	TEXT_CHECK {

		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* inputと一致していなければ例外スロー */
			if (!odb.getInputData().equals(getElement(odb).getText())) {
				throw new AssertException(
						"Expected is : " + odb.getInputData()
						+ "\nBut actual is :" + getElement(odb).val());
			}
		}
	},
	/** 要素のエビデンス取得 */
	ELEMENT_CAPTURE {

		@Override
		public void operate(OperationDataBean odb) throws AppException {

			getElement(odb).screenshot().renameTo(
					CommonDataBean.getEvidenceFolder().resolve("IMAGE" +
							CommonDataBean.getEvidenceNumAndIncrement() + ".png").toFile());
		}
	},
	/** 画像ハードコピー */
	IMAGE_CAPTURE {

		/** デフォルトだとbuiled/test/resources直下
		 * @throws IOException */
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			String name =
					screenshot("IMAGE" +
							CommonDataBean.getEvidenceNumAndIncrement());
			try {
				Files.move(Paths.get(name),
						Paths.get(CommonDataBean.getEvidenceFolder()
								.resolve(Paths.get(name).getFileName()).toString()),
								StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new AppException("IOException", "IMAGEPICTURE failed");
			}
		}
	},
	/** SQL実行 */
	EXECUTE_SQL {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			try {
				DbOperator.getInstance().execute(
					CommonDataBean.getDbPath(), CommonDataBean.getDbProps(),
					odb.getInputData());
			} catch (SQLException e) {
				throw new AppException("SQLException", this.name() + "failed");
			}
		}
	},

	/** SELECTし、レコードのデータをテスト */
	EXECUTE_SQL_AND_ASSERT {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* DBに接続 */
			try (ResultSet rs = DbOperator.getInstance().getRs(
					CommonDataBean.getDbPath(), CommonDataBean.getDbProps(),
					odb.getInputData())) {
				rs.next();
				if (!odb.getInputData().equals(
						rs.getString(odb.getTargetNum()))) {
					throw new AssertException(
							"Expected is : " + odb.getInputData()
							+ "\nBut actual is :" + getElement(odb).val());
				}
			} catch (SQLException e) {
				throw new AppException("SQLException", this.name() + "failed");
			}
		}
	},

	/** Selectし、該当レコードがないことをテスト */
	EXECUTE_SQL_AND_ASSERT_NO_RECORD {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* DBに接続 */
			try (ResultSet rs = DbOperator.getInstance().getRs(
					CommonDataBean.getDbPath(), CommonDataBean.getDbProps(),
					odb.getInputData())) {
				if (rs.next()) {
					throw new AssertException("Record is exist");
				}
			} catch (SQLException e) {
				throw new AppException("SQLException", this.name() + "failed");
			}
		}
	},
	HTML_CAPTURE {

		@Override
		public void operate(OperationDataBean odb) throws AppException {

			File output =
					CommonDataBean.getEvidenceFolder().resolve("HTML" +
					CommonDataBean.getEvidenceNumAndIncrement() + ".html").toFile();

			try {
				output.createNewFile();
				try (BufferedWriter bw =
					new BufferedWriter(new FileWriter(output, true))) {
				bw.flush();
				bw.write(WebDriverRunner.getWebDriver().getPageSource());
			}} catch (IOException e) {
				throw new AppException("IOException", "HTML failed");
			}
		}

	}
	;

	/** Element取得メソッド */
	private static SelenideElement getElement(OperationDataBean odb) {
		return
			odb.getElementEnum().createElement(
				odb.getElementName(), odb.getTargetNum());
	}

	/** 操作定義 */
	public abstract void operate(OperationDataBean odb) throws AppException;
}
