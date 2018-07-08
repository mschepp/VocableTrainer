package trainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Vokabeltrainer {

	private String dbPath;
	private int languageId = 1;
	private int GermanId = 2;
	private int answerId = 2;
	private DataBaseAdministrator db;
	private ResultSet actVocResultSet;
	private String[] colums;
	private String[] actVoc;
	private long allVocN = 0;
	private ArrayList<Integer> askedIds = new ArrayList<>();
	private String[] help= {};

	public Vokabeltrainer(String dbPath) {
		super();
		this.dbPath = dbPath;
		this.db = new DataBaseAdministrator(dbPath);
		this.colums = db.determineColumns();
		this.initRandomVocabulary();

	}
	

	public String getActVocable() {
		return this.actVoc[this.languageId];
	}

	public String getNextVocable() {
		try {
			this.actVoc=getVocable().toArray(help);
			return getActVocable() ;
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			this.db.closeConnection();
		}
		return "Error getting next Voc";
		
	}

	public String getSolution() {
		return "solution from voc trainer";
	}

	public void initRandomVocabulary() {
		try {
			setAllVocN();
			ArrayList<String> row = getVocable();
			String[] help = {};
			this.actVoc = row.toArray(help);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			this.db.closeConnection();
		}

	}
	
	public void setAllVocN() {
		String sql="SELECT COUNT(*) N FROM " + this.db.getTableName();
		try {
			ResultSet rs=this.db.executeSQLWithResult(sql);
			this.allVocN=rs.getInt("N");
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			this.db.closeConnection();
		}
		
	}
	

	public ArrayList<String> getVocable() throws SQLException {
		String sql = getAllVocSQL();
		ArrayList<String> row = new ArrayList<>();
		ResultSet vocResultSet = this.db.executeSQLWithResult(sql);
		int lastIdx=-1;
		if(this.askedIds.size()==this.allVocN) {
			lastIdx=this.askedIds.get(askedIds.size()-1);
			this.askedIds.clear();
		}
		while (vocResultSet.next()) {
			int idx = vocResultSet.getInt(colums[0]);
			if(askedIds.contains(idx) || idx==lastIdx)
				continue;
			askedIds.add(idx);
			row.add(Integer.toString(vocResultSet.getInt(colums[0])));
			for (int i = 1; i < this.colums.length; i++) {
				row.add(vocResultSet.getString(this.colums[i]));
			}
			break;
		}
		return row;
	}

	public String getAllVocSQL() {
		String sql = "SELECT ";
		this.colums = this.db.getColumns();
		for (int i = 0; i < this.colums.length; i++) {
			sql = sql + this.colums[i];
			if (i != this.colums.length - 1)
				sql = sql + ",";
		}
		sql = sql + " FROM " + this.db.getTableName() + " ORDER BY RANDOM()";
		return sql;
	}
}
