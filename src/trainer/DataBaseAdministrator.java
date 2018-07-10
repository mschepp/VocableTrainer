package trainer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class DataBaseAdministrator {

	private Connection conn;

	private ArrayList<String> cols = new ArrayList<>();
	private ArrayList<String> types = new ArrayList<>();
	private String path;
	private String url;
	private String tableName;
	// private PreparedStatement act_stmt_prep;
	// Statement act_stmt;

	public DataBaseAdministrator(String path) {
		super();
		this.path = path;
		this.url = "jdbc:sqlite:" + this.path;
		determineColumns();
		determineTableName();
	}

	public DataBaseAdministrator(String path, String[] cols, String[] types) {
		super();
		this.path = path;
		this.url = "jdbc:sqlite:" + this.path;
		File dbFile = new File(this.path);

		if (dbFile.isFile()) {
			determineColumns();
			if (this.cols.size() == 0) {
				this.cols = new ArrayList<>(Arrays.asList(cols));
				this.types = new ArrayList<>(Arrays.asList(types));
			} else {
				determineTableName();
				System.out.println("Database already exist. Going to determine columns from table");
			}
		} else {
			this.cols = new ArrayList<>(Arrays.asList(cols));
			this.types = new ArrayList<>(Arrays.asList(types));
		}
	}

	public Connection getConn() {
		return this.conn;
	}

	public void createDb() {
		createDb(this.path, this.cols, this.types);
	}

	public void createDb(String path, ArrayList<String> cols, ArrayList<String> types) {
		this.conn = null;
		String sql;
		this.tableName = "vocabulary";
		sql = "CREATE TABLE IF NOT EXISTS vocabulary(id INTEGER NOT NULL PRIMARY KEY,\n";
		for (int i = 0; i < cols.size(); i++) {
			if (cols.get(i).equalsIgnoreCase("id")) {
				continue;
			}
			sql += cols.get(i);
			sql += " ";
			sql += types.get(i);
			if (!(i == cols.size() - 1))
				sql += ",\n";
		}
		sql += ");";

		try {
			this.conn = DriverManager.getConnection(this.url);
			Statement stmt = this.conn.createStatement();
			// create a new table
			stmt.execute(sql);
			this.conn.close();
			this.conn = null;
			determineColumns();
		} catch (SQLException e) {
			System.out.println("Error creating db");
			System.out.println(e.getMessage());
		} finally {
			closeConnection();
		}

	}

	public void createDb(String path, String[] cols, String[] types) {
		createDb(path, new ArrayList<>(Arrays.asList(cols)), new ArrayList<>(Arrays.asList(types)));
	}

	public void createDb(String dbName) {
		String[] cols = { "Japanisch", "Japanisch_Romaji", "Kanji", "Deutsch", "Kanji_Level", "Vokabel_Level",
				"Kapitel" };
		String[] types = { "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT" };
		createDb(dbName, cols, types);
	}

	public boolean deleteDb() {
		File dbFile = new File(this.path);
		return dbFile.delete();
	}

	public int insertLine(String line) throws DataFormatException {
		String[] row = line.split("\t");
		if (row.length > this.cols.size() || row.length < this.cols.size() - 1)
			throw new DataFormatException("line to short or to long");

		String valuesSql = "";
		String sql = "INSERT INTO vocabulary (";

		// INSERT INTO vocabulary (id,col1,col2,...) VALUES (null/?,?,?,...);
		for (int i = 0; i < this.cols.size(); i++) {
			sql += this.cols.get(i);
			if (!(i == this.cols.size() - 1)) {
				sql += " , ";
			}
		}
		for (int i = 0; i < row.length; i++) {
			valuesSql += "?";
			if (!(i == row.length - 1)) {
				valuesSql += " , ";
			}
		}

		sql += ") VALUES (";
		valuesSql += ");";

		if (row.length != this.cols.size()) {
			sql += "null,";
		}
		sql += valuesSql;
		try {
			return executeUpdateSQL(sql, row);
		} catch (SQLException e) {
			System.out.println("sql: " + sql);
			System.out.println("Error inserting line");
			System.out.println(e.getMessage());
		} finally {
			closeConnection();
		}
		return -1;
	}

	public void insertMultLine(String multLine) throws DataFormatException {
		String[] lines = multLine.split(System.lineSeparator());
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].isEmpty())
				continue;
			insertLine(lines[i]);
		}
	}

	public void determineTableName() {
		String sql = "SELECT name FROM sqlite_master WHERE type= 'table'";
		try {
			ResultSet rs = executeSQLWithResult(sql);
			int count = 0;
			while (rs.next()) {
				if (count > 0) {
					throw new RuntimeException("more than one table in database.");
				}
				this.tableName = rs.getString("name");
				count++;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnection();
		}
	}

	public String getTableDefinition() {
		String sql = "SELECT sql FROM sqlite_master WHERE  type= 'table'";
		try {
			ResultSet rs = executeSQLWithResult(sql);
			if (rs.next())
				return rs.getString("sql");
			else
				return "";

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnection();
		}
		return "";
	}

	public void closeConnection() {
		try {
			if (this.conn != null) {
				this.conn.close();
				this.conn = null;
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public ArrayList<String> determineColumns() {
		ArrayList<String> cols = new ArrayList<>();
		ArrayList<String> colTypes = new ArrayList<>();

		String tableDef = getTableDefinition();
		tableDef = tableDef.replaceAll("\\t", " ");
		tableDef = tableDef.replaceAll("\\n", "");
		tableDef = tableDef.replaceAll("\\r", "");
		tableDef = tableDef.replaceAll("\\)", "");
		tableDef = tableDef.replaceAll("`", "");
		if (!tableDef.isEmpty()) {
			String columnString = tableDef.split("\\(", 2)[1];
			String[] columns = columnString.split(",");
			for (int i = 0; i < columns.length; i++) {
				if (columns[i].contains("(") && (columns[i].toUpperCase()).contains("PRIMARY KEY"))
					continue;
				String[] column = columns[i].split(" ");
				if (column.length == 2) {
					cols.add(column[0]);
					colTypes.add(column[1]);
				} else {
					int j = 0;
					for (; j < column.length; j++) {
						if (!column[j].isEmpty()) {
							cols.add(column[j]);
							j++;
							break;
						}
					}
					for (; j < column.length; j++) {
						if (!column[j].isEmpty()) {
							colTypes.add(column[j]);
							break;
						}
					}
				}
			}
			this.cols = cols;
			this.types = colTypes;
		}
		return this.cols;
	}

	public ArrayList<String> getColumns() {
		return this.cols;
	}

	public ResultSet executeSQLWithResult(String sql) throws SQLException {
		this.conn = DriverManager.getConnection(this.url);
		PreparedStatement stmt_prep = this.conn.prepareStatement(sql);
		ResultSet rs = stmt_prep.executeQuery();
		return rs;
	}

	public int executeUpdateSQL(String sql, String[] row) throws SQLException {
		this.conn = DriverManager.getConnection(this.url);
		// Statement stmt = this.conn.createStatement();
		PreparedStatement stmt_prep = this.conn.prepareStatement(sql);
		// create a new table
		for (int i = 0; i < row.length; i++) {
			stmt_prep.setString(i + 1, row[i]);
		}
		return stmt_prep.executeUpdate();
	}

	public int executeUpdateSQL(String sql) throws SQLException {
		String[] row = {};
		return executeUpdateSQL(sql, row);
	}

	public String getTableName() {
		return this.tableName;
	}

}
