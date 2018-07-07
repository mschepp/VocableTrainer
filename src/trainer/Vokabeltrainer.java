package trainer;

import java.sql.ResultSet;

public class Vokabeltrainer {
	
	private String dbPath;
	private int languageId=1;
	private int GermanId=-1;
	private int answerId;
	private DatabaseAdministrator db;
	private ResultSet actVoc;
	
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
		this.db=new DatabaseAdministrator(dbPath);
	}
	
	
	public String getActVocable() {
		return "actVoc";
	}
	

}
