package trainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Vokabeltrainer {

	protected int askId = 1;
	protected int GermanId = -1;
	protected int answerId = -1;
	private DataBaseAdministrator db;
	protected ArrayList<String> colums;
	protected String[] actVoc;
	private long allVocN = 0;
	private ArrayList<Integer> askedIds = new ArrayList<>();
	private String[] help = {};
	protected boolean germanSearched = true;

	public Vokabeltrainer(String dbPath) {
		super();
		this.db = new DataBaseAdministrator(dbPath);
		this.colums = db.getColumns();
		this.initRandomVocabulary();
		determineGermanId();
		this.answerId = this.GermanId;

	}

	public int getAskId() {
		return askId;
	}

	public void setAskId(int askId) {
		this.askId = askId;
	}

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public String getActVocable() {
		return this.actVoc[this.askId];
	}

	public String getNextVocable() {
		try {
			this.actVoc = getVocable().toArray(help);
			return getActVocable();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
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
		String sql = "SELECT COUNT(*) N FROM " + this.db.getTableName();
		try {
			ResultSet rs = this.db.executeSQLWithResult(sql);
			this.allVocN = rs.getInt("N");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			this.db.closeConnection();
		}

	}

	public ArrayList<String> getVocable() throws SQLException {
		String sql = getAllVocSQL();
		ArrayList<String> row = new ArrayList<>();
		ResultSet vocResultSet = this.db.executeSQLWithResult(sql);
		int lastIdx = -1;
		if (this.askedIds.size() == this.allVocN && this.allVocN != 0) {
			lastIdx = this.askedIds.get(askedIds.size() - 1);
			this.askedIds.clear();
		}
		while (vocResultSet.next()) {
			int idx = vocResultSet.getInt(colums.get(0));
			if (this.allVocN != 1 && (askedIds.contains(idx) || idx == lastIdx))
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
		this.GermanId = this.colums.indexOf("Deutsch");
	}

	public boolean isRight(String text) {
		return (text.trim()).equals((this.actVoc[this.answerId]).trim());
	}

	public boolean isPossibleSolution(String text) {
		String sql = "SELECT " + this.colums.get(this.answerId) + " FROM " + this.db.getTableName() + " WHERE "
				+ this.colums.get(this.askId) + " LIKE '%" + this.actVoc[this.askId] + "%';";
		try {
			ResultSet rs = this.db.executeSQLWithResult(sql);
			while (rs.next()) {
				if ((text.trim()).equals(rs.getString(this.colums.get(this.answerId))))
					return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			this.db.closeConnection();
		}
		return false;
	}

	public void reverse() {
		int help = this.askId;
		this.askId = this.answerId;
		this.answerId = help;
		this.germanSearched = !this.germanSearched;
	}

	public boolean isGermanSearched() {
		return this.germanSearched;
	}

	public String[] getActVocInfo() {
		return actVoc;
	}

}
