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
	DOUBLECLICK {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement.apply(odb).doubleClick();
		}
	},
	SENDKEYS {
		@Override
		public void operate(OperationDataBean odb) throws AppException {
			getElement.apply(odb).sendKeys(odb.getInputData());
		}
	},
	PRESSENTER {
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
	VALUECHECK {
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
	EXISTS {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* inputと一致していなければ例外スロー */
			if (!getElement.apply(odb).exists()) {
				throw new AssertException(
						"element is Not Exists");
			}
		}
	},
	/** プルダウン */
	TEXTCHECK {

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
	ELEMENTCAPTURE {

		@Override
		public void operate(OperationDataBean odb) throws AppException {

			getElement.apply(odb).screenshot().renameTo(
					CommonDataBean.getEvidenceFolder().resolve("IMAGE" +
							CommonDataBean.getEvidenceNumAndIncrement() + ".png").toFile());
		}
	},
	IMAGECAPTURE {

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
	EXECUTESQL {
		@Override
		public void operate(OperationDataBean odb) throws AppException {

			/* DBに接続 */
			try (ResultSet rs = DbConnector.getInstance().getRs(
					CommonDataBean.getDbPath(), CommonDataBean.getDbProps(),
					odb.getInputData())) {
			} catch (SQLException e) {
				throw new AppException("SQLException", this.name() + "failed");
			}
		}
	},
	EXECUTESQLANDASSERT{
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
	EXECUTESQLANDASSERTNORECORD{
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
