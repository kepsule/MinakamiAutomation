package jar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/** DB操作クラス<br>
 * Singleton */
public class DbConnector {

	private DbConnector(){}
	private static DbConnector connector = new DbConnector();
	public static DbConnector getInstance() {
		return connector;
	}

	/** 接続 */
	public Connection getConnect(String url, Properties props) throws SQLException {
		return 	DriverManager.getConnection(url, props);
	}

	/** statement生成 */
	public Statement getStmt(String url, Properties props) throws SQLException {
		return getConnect(url, props).createStatement();
	}

	/** SQL実行（CRUD操作いずれもOK） */
	public void execute(String url, Properties props, String sql) throws SQLException {
		getConnect(url, props).createStatement().execute(sql);
	}

	/** SELECT実行 */
	public ResultSet getRs(String url, Properties props, String sql) throws SQLException {
		return getStmt(url, props).executeQuery(sql);
	}

}
