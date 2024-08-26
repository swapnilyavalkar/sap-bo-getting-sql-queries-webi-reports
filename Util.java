import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Util {
	
	public static String baseURI = "http://" +MainProgram.SOURCE_CMS_IP + ":6405/biprws";
	
	public static OkHttpClient client = new OkHttpClient();
	public static MediaType mediaType = MediaType.parse("application/xml");
	public static String finalfilename ; 
	private static int PRETTY_PRINT_INDENT_FACTOR;
	public static String filepath = System.getProperty("user.dir");
	public static String query = "";
	public static String finalQuery = "";
	
	public static String logonServer(String sOURCE_CMS_IP, String sOURCE_CMS_PWD) throws IOException {
		
		String token = null;
		
		RequestBody body = RequestBody.create(mediaType, "<attrs xmlns=\"http://www.sap.com/rws/bip\">\n"
				+ "<attr name=\"password\" type=\"string\">"
				+ MainProgram.SOURCE_CMS_PWD
				+ "</attr>\n    "
				+ "<attr name=\"clientType\" type=\"string\"></attr>\n    <attr name=\"auth\" type=\"string\" "
				+ "possibilities=\"secEnterprise,secLDAP,secWinAD,secSAPR3\">secEnterprise</attr>\n    "
				+ "<attr name=\"userName\" type=\"string\">"
				+ MainProgram.SOURCE_CMS_USN
				+ "</attr>\n</attrs>");
		
		Request request = new Request.Builder()				
		  .url(baseURI+"/logon/long")
		  .post(body)
		  .addHeader("Content-Type", "application/xml")
		  .addHeader("Cache-Control", "no-cache") 
		  .build();
		 		
		try {
			
			Response response = client.newCall(request).execute();
			
			String xmlresponse = response.body().string();
            
			JSONObject xmlJSONObj = XML.toJSONObject(xmlresponse);
            
			String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
            
			JSONObject jObject  = new JSONObject(jsonPrettyPrintString);
            						
            token = jObject.getJSONObject("entry").getJSONObject("content").getJSONObject("attrs").getJSONObject("attr").getString("content"); // get data object
        	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return token;
	}

	public static void getQueries(String sourceReportId, String TOKEN, String report_name) throws IOException {

	      	String dpID = null;
	        String dpName = null;
			String dataSourceType = null;
			
		Request request = new Request.Builder()
				  .url(baseURI+"/raylight/v1/documents/"+sourceReportId+"/dataproviders")
				  .get()
				  .addHeader("Content-Type", "application/xml")
				  .addHeader("X-SAP-LogonToken", "\""+ TOKEN + "\"")
				  .addHeader("Cache-Control", "no-cache")
				  .build();

		Response response = client.newCall(request).execute();
		
		String xmlresponse = response.body().string();
        				
		JSONObject xmlJSONObj = XML.toJSONObject(xmlresponse);
        
		String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        
		JSONObject jObject  = new JSONObject(jsonPrettyPrintString);
        
        if (jObject.getJSONObject("dataproviders").get("dataprovider") instanceof JSONArray){
        
        JSONArray dataprovider = jObject.getJSONObject("dataproviders").getJSONArray("dataprovider");
        
        System.out.println("Count of Queries: "+dataprovider.length() + "\n");
        
        for (int i = 0; i < dataprovider.length(); i++){
               	
        	dpID = dataprovider.getJSONObject(i).getString("id");
        	dpName = dataprovider.getJSONObject(i).getString("name");
        	
        	dataSourceType = dataprovider.getJSONObject(i).getString("dataSourceType");
        	
        	if (dataSourceType.equals("fhsql")){
	    		
        		getFHSQLQuery(dpID, dpName, TOKEN, report_name);
	    		
	    	}else{
	    	
	    		System.out.println("THIS IS UNIVERSE BASED REPORT !!!\n");
	    		getUniverseQuery(dpID, dpName, TOKEN, report_name);
	    	}
        	
        }		
        }else{
		
		JSONObject dataprovider = jObject.getJSONObject("dataproviders").getJSONObject("dataprovider");
		
		dpID = dataprovider.getString("id");
    	dpName = dataprovider.getString("name");
    	
    	dataSourceType = dataprovider.getString("dataSourceType");
    	
	    	if (dataSourceType.equals("fhsql")){
	    		
	    		getFHSQLQuery(dpID, dpName, TOKEN, report_name);
	    		
	    	}else{
	    	
	    		System.out.println("THIS IS UNIVERSE BASED REPORT !!!\n");
	    		getUniverseQuery(dpID, dpName, TOKEN, report_name);
	    	}
	        	
	}
        
}	


	private static void getFHSQLQuery(String dpID, String dpName, String TOKEN, String report_name) throws IOException {
		
		Request request = new Request.Builder()
				  .url(baseURI+"/raylight/v1/documents/"+MainProgram.SOURCE_REPORT_ID+"/dataproviders/"+dpID)
				  .get()
				  .addHeader("Content-Type", "application/xml")
				  .addHeader("X-SAP-LogonToken", "\""+ TOKEN + "\"")
				  .addHeader("Accept", "application/xml")
				  .addHeader("Cache-Control", "no-cache")
				  .build();

		Response response = client.newCall(request).execute();
		
		String xmlresponse = response.body().string();
        
		JSONObject xmlJSONObj = XML.toJSONObject(xmlresponse);
        
		String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        
		JSONObject jObject  = new JSONObject(jsonPrettyPrintString);
        
        JSONObject properties = jObject.getJSONObject("dataprovider").getJSONObject("properties");
        
        JSONArray property = properties.getJSONArray("property");  
        
        String finalpath = filepath+"\\"+report_name+"_"+dpName+".txt";
        
    	OutputStream os = new FileOutputStream(new File(finalpath));
    	
        for (int i = 0; i < property.length(); i++){
        	
        	if(property.getJSONObject(i).getString("key").equals("sql")){
        		
        		query = property.getJSONObject(i).getString("content");
        		String query_check = "";
        		
        		if (query_check.equalsIgnoreCase(query))
        			System.out.println("Query Found: "+ report_name);
        		os.write(query.getBytes(), 0, query.length());
             	os.close();
             	
         	}
        	
      }
       
	}
	
	private static void getUniverseQuery(String dpID, String dpName, String TOKEN, String report_name) throws IOException {
		//This will generate the queries of universe based reports
		
		
		Request request = new Request.Builder()
		  .url(baseURI+"/raylight/v1/documents/"+MainProgram.SOURCE_REPORT_ID+"/dataproviders/"+dpID)
		  .get()
		  .addHeader("Content-Type", "application/xml")
		  .addHeader("X-SAP-LogonToken", "\""+ TOKEN + "\"")
		  .addHeader("Cache-Control", "no-cache")
		  .build();

		Response response = client.newCall(request).execute();
		
		String xmlresponse = response.body().string();
        
		JSONObject xmlJSONObj = XML.toJSONObject(xmlresponse);
        
		String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        
		JSONObject jObject  = new JSONObject(jsonPrettyPrintString);
        
        String query = jObject.getJSONObject("dataprovider").getString("query");
        
        String finalpath = filepath+"\\"+report_name+"_"+dpName+".txt";
    	OutputStream os = new FileOutputStream(new File(finalpath));
      
		os.write(query.getBytes(), 0, query.length());
     	os.close();
		
	}

	public static void logoffServer(String TOKEN) throws IOException {
		
		RequestBody body = RequestBody.create(mediaType, "");
		
		Request request = new Request.Builder()
		  .url(baseURI+"/logoff")
		  .post(body)
		  .addHeader("Content-Type", "application/xml")
		  .addHeader("X-SAP-LogonToken", "\""+ TOKEN + "\"")
		  .build();

		Response response = client.newCall(request).execute();
		
		
	}

	public static String getReportName(String sOURCE_REPORT_ID, String TOKEN) throws IOException {
				
			Request request = new Request.Builder()
				  .url(baseURI+"/infostore/"+sOURCE_REPORT_ID)
				  .get()
				  .addHeader("Content-Type", "application/xml")
				  .addHeader("X-SAP-LogonToken", "\""+ TOKEN + "\"")
				  .build();

		Response response = client.newCall(request).execute();
		
		String xmlresponse = response.body().string();
	    
		JSONObject xmlJSONObj = XML.toJSONObject(xmlresponse);
	    
		String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
	    
		JSONObject jObject  = new JSONObject(jsonPrettyPrintString);
		String report_name  = jObject.getJSONObject("entry").getJSONObject("title").getString("content");
				
		return report_name;
	}
	
}