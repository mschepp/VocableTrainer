package trainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Vokabeltrainer {

	protected int askId = 1;
	protected int answerId = -1;
	private DataBaseAdministrator db;
	protected ArrayList<String> colums;
	protected String[] actVoc;
	private long allVocN = 0;
	private ArrayList<Integer> askedIds = new ArrayList<>();
	private String[] help = {};
	protected boolean germanSearched = true;

	/**
	 * creates a Vokabeltrainer Object with the vocable that should be asked in a
	 * database file given by dbPath. A first random vocable is also determined.
	 * 
	 * @param dbPath is the path of the database file
	 */
	public Vokabeltrainer(String dbPath) {
		super();
		this.db = new DataBaseAdministrator(dbPath);
		this.colums = db.getColumns();
		this.initRandomVocabulary();
		this.answerId = determineGermanId();
	}

	/**
	 * 
	 * @return the index of the column in which the actual asked information is
	 *         stored
	 */
	public int getAskId() {
		return askId;
	}

	/**
	 * Sets the index of the column from which the information that is asked after
	 * setting is stored
	 * 
	 * @param askId
	 */
	public void setAskId(int askId) {
		this.askId = askId;
	}

	/**
	 * 
	 * @return the index of the column in which the actual solution information is
	 *         stored
	 */
	public int getAnswerId() {
		return answerId;
	}

	/**
	 * Sets the index of the column from which the information for the solution is
	 * read
	 * 
	 * @param answerId
	 */
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	/**
	 * 
	 * @return the string of the asked information
	 */
	public String getAskedInformation() {
		return this.actVoc[this.askId];
	}

	/**
	 * Replaces the actual vocable with the next vocable by calling
	 * {@link #getVocable() getVocable}
	 * 
	 * @return the now asked information of the next vocable by calling
	 *         {@link #getAskedInformation() getActVocable}
	 */
	public String getNextVocable() {
		try {
			this.actVoc = getVocable().toArray(help);
			return getAskedInformation();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			this.db.closeConnection();
		}
		return "Error getting next Voc";

	}

	/**
	 * 
	 * @return the information which is search for
	 */
	public String getSolution() {
		return this.actVoc[this.answerId];
	}

	/**
	 * Determines the number of vocable entries by calling {@link #setAllVocN()
	 * setAllVocN} and sets the first vocable by calling {@link #getVocable()
	 * getVocable}
	 */
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

	/**
	 * determines the number of vocables stored in the database
	 */
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

	/**
	 * Fetches one random vocable from the database which wasn't asked already. If
	 * all vocables which are stored in the database have been asked a random
	 * vocable will be asked a again until all vocables are asked twice and so on
	 * 
	 * @return an ArrayList with the column entries for the picked vocable
	 * @throws SQLException if a database access error occurs; for example when the
	 *                      Statement is closed or it doesn't produce an output
	 */
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

	public int determineGermanId() {
		return this.colums.indexOf("Deutsch");
	}

	public boolean isRight(String text) {
		return (text.trim()).equals((this.actVoc[this.answerId]).trim());
	}

	/**
	 * checks if the by the user supplied solution is valid for any vocable that has
	 * the same asked information but can have a different solution than the actual
	 * asked vocable
	 * 
	 * @param text is the text which should be check if it is valid solution for the
	 *             asked information
	 * @return <strong>true</strong>: if there is a vocable with the same asked
	 *         information like the actual vocable (not excluding the actual
	 *         vocable) and text is a valid solution for one of these vocables <br>
	 *         <strong>false</strong>: if there is no vocable with the same asked
	 *         information for which text is a valid solution
	 */
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

	/**
	 * switches the asked information with the searched information
	 */
	public void reverse() {
		int help = this.askId;
		this.askId = this.answerId;
		this.answerId = help;
		this.germanSearched = !this.germanSearched;
	}

	/**
	 * 
	 * @return <strong>true:</strong> if the searched Information is German <br>
	 *         <strong>false:</strong> if the search Information is not German <br>
	 */
	public boolean isGermanSearched() {
		return this.germanSearched;
	}

	/**
	 * 
	 * @return all information stored in the database for the actual vocable. The
	 *         information doesn't have to be in a predefined order
	 */
	public String[] getActVocInfo() {
		return actVoc;
	}

}
