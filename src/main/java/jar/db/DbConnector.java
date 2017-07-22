package jar.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbConnector {

	private DbConnector(){}
	private static DbConnector connector = new DbConnector();
	public static DbConnector getInstance() {
		return connector;
	}

	public Connection getConnect(String url, Properties props) throws SQLException {
		return 	DriverManager.getConnection(url, props);
	}

	public Statement getStmt(String url, Properties props) throws SQLException {
		return getConnect(url, props).createStatement();
	}

	public ResultSet getRs(String url, Properties props, String sql) throws SQLException {
		return getStmt(url, props).executeQuery(sql);
	}

}
