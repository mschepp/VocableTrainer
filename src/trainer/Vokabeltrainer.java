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
	private long allVocN=0;
	private ArrayList<Integer> askIds=new ArrayList<>();
	
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
		this.colums=db.determinColumns();
		
	}
	
	
	public String getActVocable() {
		try {
			this.actVocResultSet.next();
			this.actVocResultSet.getString("Deutsch");
		}
		catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		return "actVoc";
	}
	
	
	public String getNextVocable() {
		return "nextVoc";
	}

	
	public String getSolution() {
		return "solution from voc trainer";
	}
	
	public void initRandomVocabulary() {
		String sql="SELECT ";
		String[] cols=this.db.getColumns();
		for(int i=0;i<cols.length;i++) {
			sql=sql+cols[i];
			if(i!=cols.length-1)
				sql=sql+",";
		}
		sql=sql+" FROM "+ this.db.getTableName() +" ORDER BY RANDOM()";
		try {
			this.actVocResultSet=this.db.executeSQLWithResult(sql);
					
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			this.db.closeConnection();
		}
		
		
	}
}
