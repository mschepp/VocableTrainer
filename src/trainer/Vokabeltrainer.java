package trainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Vokabeltrainer {
	
	private String dbPath;
	private int languageId=1;
	private int GermanId=-1;
	private int answerId;
	private DataBaseAdministrator db;
	private ResultSet actVocResultSet;
	private String[] colums;
	private String[] actVoc;
	private long allVocN=0;
	private ArrayList<Integer> askedIds=new ArrayList<>();
	
	//private String[] actVoc;
	
	
	/*//Python sniplet from my python voc trainer
	 *
        self.initVocableTrainer()        
        self.GermanId=None
        for i in range(len(self.tableCreate)):
            if self.tableCreate[i].find("Deutsch")!=-1:
                self.GermanId=i
                break
        self.AnswerId=self.GermanId
        #print(self.GermanId)
        self.languageId=1*/

	public Vokabeltrainer(String dbPath) {
		super();
		this.dbPath = dbPath;
		this.db=new DataBaseAdministrator(dbPath);
		this.colums=db.determineColumns();
		this.initRandomVocabulary();
		
	}
	
	
	public String getActVocable() {
		return this.actVoc[this.languageId];
		//return "actVoc";
	}
	
	
	public String getNextVocable() {
		return "nextVoc";
	}

	
	public String getSolution() {
		return "solution from voc trainer";
	}
	
	public void initRandomVocabulary() {
		String sql="SELECT ";
		this.colums=this.db.getColumns();
		for(int i=0;i<this.colums.length;i++) {
			sql=sql+this.colums[i];
			if(i!=this.colums.length-1)
				sql=sql+",";
		}
		sql=sql+" FROM "+ this.db.getTableName() +" ORDER BY RANDOM()";
		ArrayList<String> row=new ArrayList<>();
		try {
			ResultSet vocResultSet=this.db.executeSQLWithResult(sql);
			vocResultSet.next();
			row.add(Integer.toString(vocResultSet.getInt(colums[0])));
			askedIds.add(vocResultSet.getInt(colums[0]));
			for(int i=1;i<this.colums.length;i++) {
				row.add(vocResultSet.getString(this.colums[i]));
			}
			String[] help= {};
			this.actVoc=row.toArray(help);
					
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			this.db.closeConnection();
		}
		
		
	}
}
