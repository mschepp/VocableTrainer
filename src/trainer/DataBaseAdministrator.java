package trainer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class DataBaseAdministrator {

	private Connection conn;

	private String[] cols= {};
	private String[] types= {};
	private String path;
	private String url;
	private String tableName;
	//private PreparedStatement act_stmt_prep;
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
		File dbFile= new File(this.path);
	
		if(dbFile.isFile()) {
			determineColumns();
			if(this.cols.length==0) {
				this.cols = cols;
				this.types = types;				
			}
			else {
				determineTableName();
				System.out.println("Database already exist. Going to determine columns from table");
			}
		}else {			
			this.cols = cols;
			this.types = types;
		}
	}

	public Connection getConn() {
		return this.conn;
	}

	public void createDb() {
		createDb(this.path, this.cols, this.types);
	}

	public void createDb(String path, String[] cols, String[] types) {
		this.conn = null;
		String sql;
		this.tableName = "vocabulary";
		sql = "CREATE TABLE IF NOT EXISTS vocabulary(id INTEGER NOT NULL PRIMARY KEY,\n";
		for (int i = 0; i < this.cols.length; i++) {
			if(this.cols[i].equalsIgnoreCase("id")) {
				continue;
			}
			sql += this.cols[i];
			sql += " ";
			sql += this.types[i];
			if (!(i == this.cols.length - 1))
				sql += ",\n";
		}
		sql += ");";

		try {
			this.conn = DriverManager.getConnection(this.url);
			Statement stmt= this.conn.createStatement();
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
		if (row.length > this.cols.length  || row.length < this.cols.length-1)
			throw new DataFormatException("line to short or to long");

		String valuesSql = "";
		String sql = "INSERT INTO vocabulary (";

		// INSERT INTO vocabulary (id,col1,col2,...) VALUES (null/?,?,?,...);
		for (int i = 0; i < this.cols.length; i++) {
			sql += this.cols[i];
			if (!(i == this.cols.length - 1)) {
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

		if (row.length != this.cols.length ) {
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
		}
		finally {
			closeConnection();
		}
	}
	
	public String getTableDefinition() {
		String sql="SELECT sql FROM sqlite_master WHERE  type= 'table'";
		try {
			ResultSet rs = executeSQLWithResult(sql);
			if(rs.next())
				return rs.getString("sql");
			else return "";
			

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
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
	
	public String[] determineColumns() {
		ArrayList<String> res=new ArrayList<>();
		ArrayList<String> colTypes=new ArrayList<>();
		String[] result= {};
		
		String tableDef=getTableDefinition();
		tableDef=tableDef.replaceAll("\\t", " ");
		tableDef=tableDef.replaceAll("\\n", "");
		tableDef=tableDef.replaceAll("\\r", "");
		tableDef=tableDef.replaceAll("\\)", "");
		tableDef=tableDef.replaceAll("`", "");
		if(!tableDef.isEmpty()) {
			String columnString=tableDef.split("\\(",2)[1];
			String[] columns=columnString.split(",");
			for(int i=0;i<columns.length;i++) {
				if(columns[i].contains("(") && (columns[i].toUpperCase()).contains("PRIMARY KEY"))
					continue;
				String[] col=columns[i].split(" ");
				if(col.length==2) {
					res.add(col[0]);
					colTypes.add(col[1]);
				}
				else {
					int j=0;
					for(;j<col.length;j++) {
						if(!col[j].isEmpty()) {
							res.add(col[j]);
							j++;
							break;
						}
					}
					for(;j<col.length;j++) {
						if(!col[j].isEmpty()) {
							colTypes.add(col[j]);
							break;
						}
					}
				}
			}
			this.cols=res.toArray(result);
			this.types=colTypes.toArray(result);
		}
		return this.cols;
	}
	
	public String[] getColumns() {
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
		String[] row= {};
		return executeUpdateSQL(sql, row);
	}

	public String getTableName() {
		return this.tableName;
	}

	
	
}
