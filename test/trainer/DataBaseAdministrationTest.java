package trainer;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.zip.DataFormatException;

import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import trainer.DatabaseAdministrator;

class DataBaseAdministrationTest {
	
	private DatabaseAdministrator db;
	String path=System.getProperty("user.dir")+"/Database/Test/test.sql";
		
	
	@BeforeEach
	void setUp() throws Exception {
		String[] cols= {"col1","col2","col3"};
		String[] types = {"TEXT","TEXT","TEXT"};
		db=new DatabaseAdministrator(path, cols, types);
		
	}
	
	@AfterEach
	void tearDown() throws Exception {
		Connection conn=db.getConn();
		if(conn!=null)
			conn.close();
		File dbFile=new File(path);
		dbFile.delete();
	}

	@Test
	void createDbTest() {
		db.createDb();
		assertTrue(Files.isRegularFile(Paths.get(path)));
		
		
	}
	
	@Test
	void insertLineTest() {
		db.createDb();
		try {
			int result=db.insertLine("String1\tString2\tString2");
			assertNotEquals(result, -1);
			db.getTableName();
			db.getTableDefinition();
		}catch(DataFormatException e) {
			fail(e.getMessage());
		}
	}
	
	
	

}
