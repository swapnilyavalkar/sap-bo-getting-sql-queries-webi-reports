import java.io.IOException;


public class MainProgram {
	
	
	public static String SOURCE_CMS_IP = "";
	public static final String SOURCE_CMS_USN = "";
	public static String SOURCE_CMS_PWD = "";
	public static String SOURCE_REPORT_ID;
	public static String[] SOURCE_REPORT_ID_ARRAY = {"760528"}; # MENTION SI_ID OF REPORT
	
	public static String SOURCE_REPORT_NAME = null;
	
	public static String TOKEN = null;
	
	public static String TEMP_LOCAL_FOLDER = System.getProperty("java.io.tmpdir"); 
				
	public static void main(String[] args) throws IOException {
			
		
		//Login to Source BO Server....
    	TOKEN = Util.logonServer(SOURCE_CMS_IP, SOURCE_CMS_PWD);
    	
    	for (int i=0; i<SOURCE_REPORT_ID_ARRAY.length;i++){
    	//Get and Set Report name and File names
    	SOURCE_REPORT_ID = SOURCE_REPORT_ID_ARRAY[i];
    	String report_name = Util.getReportName(SOURCE_REPORT_ID_ARRAY[i],TOKEN);
    	
    	//Get the FHSQL Queries
    	Util.getQueries(SOURCE_REPORT_ID_ARRAY[i],TOKEN, report_name);
    	
    	}
        
    	//Log Off from Source BO Server....
    	Util.logoffServer(SOURCE_CMS_IP);
    	
    	System.out.println("Files are saved at : "+ System.getProperty("user.dir"));
    		
	}
		
	 	
}

