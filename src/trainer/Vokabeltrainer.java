package trainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Vokabeltrainer {

	private String dbPath;
	protected int languageId = 1;
	protected int GermanId = -1;
	protected int answerId = -1;
	private DataBaseAdministrator db;
	private ResultSet actVocResultSet;
	private ArrayList<String> colums;
	protected String[] actVoc;
	private long allVocN = 0;
	private ArrayList<Integer> askedIds = new ArrayList<>();
	private String[] help= {};

	public Vokabeltrainer(String dbPath) {
		super();
		this.dbPath = dbPath;
		this.db = new DataBaseAdministrator(dbPath);
		this.colums = db.determineColumns();
		this.initRandomVocabulary();
		determineGermanId();
		this.answerId=this.GermanId;

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
		return this.actVoc[this.answerId];
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
			int idx = vocResultSet.getInt(colums.get(0));
			if(askedIds.contains(idx) || idx==lastIdx)
				continue;
			askedIds.add(idx);
			row.add(Integer.toString(vocResultSet.getInt(colums.get(0))));
			for (int i = 1; i < this.colums.size(); i++) {
				row.add(vocResultSet.getString(this.colums.get(i)));
			}
			break;
		}
		return row;
	}

	public String getAllVocSQL() {
		String sql = "SELECT ";
		this.colums = this.db.getColumns();
		for (int i = 0; i < this.colums.size(); i++) {
			sql = sql + this.colums.get(i);
			if (i != this.colums.size() - 1)
				sql = sql + ",";
		}
		sql = sql + " FROM " + this.db.getTableName() + " ORDER BY RANDOM()";
		return sql;
	}
	
	public void determineGermanId() {
		this.GermanId=this.colums.indexOf("Deutsch");
	}
}
