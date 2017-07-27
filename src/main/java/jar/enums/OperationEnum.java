package jar.enums;

import static com.codeborne.selenide.Selenide.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import com.codeborne.selenide.SelenideElement;

import jar.bean.CommonDataBean;
import jar.bean.OperationDataBean;
import jar.db.DbConnector;
import jar.util.AppException;
import jar.util.AssertException;

public enum OperationEnum {

	CLICK {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement.apply(odb).click();
		}
	},
	DOUBLE_CLICK {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement.apply(odb).doubleClick();
		}
	},
	SEND_KEYS {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement.apply(odb).sendKeys(odb.getInputData());
		}
	},
	PRESS_ENTER {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement.apply(odb).pressEnter();
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
			if (!odb.getInputData().equals(getElement.apply(odb).val())) {
				throw new AssertException(
						"Expected is : " + odb.getInputData()
						+ "\nBut actual is :" + getElement.apply(odb).val());
			}
		}
	},
	EXIST {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* inputと一致していなければ例外スロー */
			if (!getElement.apply(odb).exists()) {
				throw new AssertException(
						"element is Not Exist");
			}
		}
	},
	NOT_EXIST {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* inputと一致していれば例外スロー */
			if (getElement.apply(odb).exists()) {
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
			if (!odb.getInputData().equals(getElement.apply(odb).getText())) {
				throw new AssertException(
						"Expected is : " + odb.getInputData()
						+ "\nBut actual is :" + getElement.apply(odb).val());
			}
		}
	},
	ELEMENT_CAPTURE {

		@Override
		public void operate(OperationDataBean odb) throws AppException {

			getElement.apply(odb).screenshot().renameTo(
					CommonDataBean.getEvidenceFolder().resolve("IMAGE" +
							CommonDataBean.getEvidenceNumAndIncrement() + ".png").toFile());
		}
	},
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
	EXECUTE_SQL {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* SQL実行 */
			try {
				DbConnector.getInstance().execute(
					CommonDataBean.getDbPath(), CommonDataBean.getDbProps(),
					odb.getInputData());
			} catch (SQLException e) {
				throw new AppException("SQLException", this.name() + "failed");
			}
		}
	},
	EXECUTE_SQL_AND_ASSERT{
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* DBに接続 */
			try (ResultSet rs = DbConnector.getInstance().getRs(
					CommonDataBean.getDbPath(), CommonDataBean.getDbProps(),
					odb.getInputData())) {
				rs.next();
				if (!odb.getInputData().equals(
						rs.getString(odb.getTargetNum()))) {
					throw new AssertException(
							"Expected is : " + odb.getInputData()
							+ "\nBut actual is :" + getElement.apply(odb).val());
				}
			} catch (SQLException e) {
				throw new AppException("SQLException", this.name() + "failed");
			}
		}
	},
	EXECUTE_SQL_AND_ASSERT_NO_RECORD{
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* DBに接続 */
			try (ResultSet rs = DbConnector.getInstance().getRs(
					CommonDataBean.getDbPath(), CommonDataBean.getDbProps(),
					odb.getInputData())) {
				if (rs.next()) {
					throw new AssertException("Record is exist");
				}
			} catch (SQLException e) {
				throw new AppException("SQLException", this.name() + "failed");
			}
		}
	}
	;

	/** Element取得メソッド */
	private static Function<OperationDataBean, SelenideElement> getElement =
			odb -> odb.getElementEnum().createElement(
					odb.getElementName(), odb.getTargetNum());
	public abstract void operate(OperationDataBean odb) throws AppException;
}
