package policy_verify;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.util.Arrays;
import java.util.List;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

public class verifyclient {
    
  
    //private String[][] matrix=null;
  //  private String[][] matrix2=null;
  //  private String[][] matrix3=null;
   // public String[][] newmat=null;
  //  public String [][] getMatrix(){return matrix;}
   

    
   // public String[][] matrix= new String[10][10]; 
    public verifyclient() {
       
    }
  
   // private static String[][] matrix;
    /**
     * @param args
     * @throws ClientProtocolException
     * @throws IOException
     */
    
    // String[][] travel (String searchtype, String policyno, String cnic, String passport_no)
    public String[][] travel (String searchtype, String policyno, String cnic, String passport_no) throws IOException, Exception
    { 
    //                     String searchtype="1";
    //                    String policyno="2645022256/08/2016";
    //                            String passport_no="";
    //                    String cnic="4220153143637";
                    //4220153143637
                    //2645022256/08/2016
                    //AB9893633
                    //ER5149132
                    //3740504559131
                    //2255001145/02/2016
                    String [][] resmat= null;
                    String [][]mat=new String[2][2];
           String response=null;
                    JsonObject result3= new JsonObject();             
                    try{ Client c = Client.create();
                    
                    WebResource resource =
                        c.resource("http://online.efuinsurance.com:9003/ePolicyVerify_WebApp/policy_verify/travel/travel?");
                    
                    //c.resource("http://localhost:7101/travelservice-ViewController/resources/travel/travel?");
                    MultivaluedMap queryParams = new MultivaluedMapImpl();
                     
                              if (cnic!=null&&cnic.isEmpty()==false){  queryParams.add("doctype",searchtype );
                       queryParams.add("policystr",policyno );
                        queryParams.add("CNIC",cnic );
                    }
                    else if ((passport_no!=null&&passport_no.isEmpty()==false) ){
                                               queryParams.add("doctype",searchtype );
                                               queryParams.add("policystr",policyno );
                                                   queryParams.add("passno", passport_no);                
                                               }
                     response = resource.queryParams(queryParams).get(String.class);
                        
           }
                               catch(Exception ex)
                               {                       
                               mat[0][0]= "red";
                               mat[0][1]=ex.getMessage();
                                   return mat;}
                   
                    try{ 
                        String[] rows = response.split(",");
                    resmat = new String[rows.length][2];
                    JsonParser jp;
                    jp = new JsonParser(); //from gson
                    JsonElement root;
                    root = jp.parse(response);
                    JsonObject result = root.getAsJsonObject();
                    JsonArray result2 = result.get("Policy Schedule").getAsJsonArray();
                          int length1 = result2.size();
                        int len =response.length();
                            if (len>22)  {
                     for(int i=0; i<length1; i++)
                    { result3 = result2.get(i).getAsJsonObject();}
                    Set<Map.Entry<String, JsonElement>> entries = result3.entrySet(); 
                       resmat[0][0]="green";
                        resmat[0][1]="length of web service responce"+rows.length;
                      int h=1;
                    for (Map.Entry<String, JsonElement> entry: entries) {
                        if (h<=21){
                        resmat[h][0]=entry.getKey().toString().replaceAll("\"", "");
                        resmat[h][1]=entry.getValue().toString().replaceAll("\"", "");             
                        }h++;    
                        }} else {
                                resmat[0][0]= "red";}
                            return resmat;
                        }            
                               catch(Exception ex)
                               {                       
                                mat[0][0]= "red";
                               mat[0][1]=ex.getMessage();
                                   return mat;}   
       
       
          // System.out.println(response);
           //          System.out.println(Arrays.deepToString(matrix));
           //         System.out.println(matrix.length);
           //          System.out.println(entries.size());
                //     System.out.println(entries);
                //     System.out.println(queryParams.toString());
       }
    
    // static void main(String[] args)  String[][] marinepolicy(String searchtype, String policyno, String clientcode)
    public String[][] marinepolicy(String searchtype, String policyno, String clientcode) throws IOException, Exception
     {
         
                 //          String policyno="2511035041/08/2016";
                 //                     String clientcode ="224626";
                 
            String response="";
            String[][] newmat=null;
    String[][] mat = new String[2][2];
            JsonObject result3= new JsonObject();             
           try {
               Client c = Client.create();
      WebResource resource =
          
          c.resource("http://online.efuinsurance.com:9003/ePolicyVerify_WebApp/policy_verify/marine/marine?");
         // c.resource("http://localhost:7101/travelservice-ViewController/resources/marine/marine?");
            MultivaluedMap queryParams = new MultivaluedMapImpl();             
                      if ((clientcode!=null&&clientcode.isEmpty()==false) ){
               queryParams.add("doctype",searchtype );
               queryParams.add("policystr",policyno );
                   queryParams.add("clientcode", clientcode);                
               }                      
             response = resource.queryParams(queryParams).get(String.class);
            }
                                catch(Exception ex)
                                {                       
                                mat[0][0]= "red";
                                mat[0][1]=ex.getMessage();
                                    return mat;}
                    
            try{ 
                String[] rows = response.split(",");
            newmat = new String[rows.length][2];
            JsonParser jp;
            jp = new JsonParser(); //from gson
            JsonElement root;
            root = jp.parse(response);
            JsonObject result = root.getAsJsonObject();
            JsonArray result2 = result.get("Policy Schedule").getAsJsonArray();
                  int length1 = result2.size();
                    if (response.length()>22)  { 
             for(int i=0; i<length1; i++)
            { result3 = result2.get(i).getAsJsonObject();}
            Set<Map.Entry<String, JsonElement>> entries = result3.entrySet(); 
              
                  newmat[0][0]="green";
                newmat[0][1]="length of web service responce"+rows.length;
              int h=1;
            for (Map.Entry<String, JsonElement> entry: entries) {
                if (h<=21){
                newmat[h][0]=entry.getKey().toString().replaceAll("\"", "");
                newmat[h][1]=entry.getValue().toString().replaceAll("\"", "");             
                }h++;    
                }} else 
                    {
                        newmat[0][0]= "red";}
                    return newmat;
                }            
                       catch(Exception ex)
                       {                       
                        mat[0][0]= "red";
                       mat[0][1]=ex.getMessage();
                           return mat;}   
            
            
            // System.out.println(response);
            //          System.out.println(Arrays.deepToString(matrix));
            //         System.out.println(matrix.length);
            //          System.out.println(entries.size());
            //     System.out.println(entries);
            //     System.out.println(queryParams.toString());
            }
    public String[][]firepolicy(String searchtype, String policyno, String gst, String ntn, String nic) throws IOException, Exception
   // public static void main(String[] args)throws IOException, Exception
    {
         
           // String searchtype="1";
              //     String policyno="2643032744/06/2016";
               //               String nic =null;
                //                String gst="1700981302728";
                  //                          String ntn=null;
                                                
                
                String [][] resmat= null;
                String [][]mat=new String[2][2];
                String response=null;
                JsonObject result3= new JsonObject();             
                try{ Client c = Client.create();
                
                WebResource resource =
                    c.resource("http://online.efuinsurance.com:9003/ePolicyVerify_WebApp/policy_verify/fire/fire?");
                    //http://online.efuinsurance.com:9003/ePolicyVerify_WebApp/policy_verify/travel/travel?
               
                MultivaluedMap queryParams = new MultivaluedMapImpl();
                 
                          if (nic!=null&&nic.isEmpty()==false){  queryParams.add("doctype",searchtype );
                   queryParams.add("policystr",policyno );
                    queryParams.add("nic",nic );
                }
                else if ((ntn!=null&&ntn.isEmpty()==false) ){
                                           queryParams.add("doctype",searchtype );
                                           queryParams.add("policystr",policyno );
                                               queryParams.add("ntn", ntn);                
                                           }
                    else if (gst!=null&&gst.isEmpty()==false){
                                          queryParams.add("doctype",searchtype );
                               queryParams.add("policystr",policyno );
                                queryParams.add("gst",gst );
                            }
                 response = resource.queryParams(queryParams).get(String.class);
                    
                }
                           catch(Exception ex)
                           {                       
                           mat[0][0]= "red";
                           mat[0][1]=ex.getMessage();
                             //  return mat;
                              }
                
                try{ 
                    String[] rows = response.split(",");
                resmat = new String[rows.length][2];
                JsonParser jp;
                jp = new JsonParser(); //from gson
                JsonElement root;
                root = jp.parse(response);
                JsonObject result = root.getAsJsonObject();
                JsonArray result2 = result.get("Policy Schedule").getAsJsonArray();
                      int length1 = result2.size();
                        if (response.length()>22)  {
                 for(int i=0; i<length1; i++)
                { result3 = result2.get(i).getAsJsonObject();}
                Set<Map.Entry<String, JsonElement>> entries = result3.entrySet(); 
                   resmat[0][0]="green";
                    resmat[0][1]="length of web service responce"+rows.length;
                  int h=1;
                for (Map.Entry<String, JsonElement> entry: entries) {
                    if (h<=21){
                    resmat[h][0]=entry.getKey().toString().replaceAll("\"", "");
                    resmat[h][1]=entry.getValue().toString().replaceAll("\"", "");             
                    }h++;    
                }} else {
                            resmat[0][0]= "red";}
                        return resmat; 
                    
                    }            
                           catch(Exception exp)
                           {                       
                            mat[0][0]= "red";
                           mat[0][1]=exp.getMessage();
                               return mat;
                          } 
               // System.out.println(response);
               // System.out.println(Arrays.deepToString(resmat));
            }
    public String[][] vpolicy(String searchtype, String policyno, String reg, String eng, String chass, String certno) throws IOException, Exception
    {   
//           String searchtype="1";
//               String policyno="3844045583/05/2016";
//                          String reg =null;
//                              String eng=null;
//                                         String chass="4036283";
//                                             String certno=null;
        
        String response="";
            JsonObject result3= new JsonObject(); 
           String[][] matrix2=null;
            String[][] mat = new String[2][2];
            try{ Client c = Client.create();
            WebResource resource =
               c.resource("http://online.efuinsurance.com:9003/ePolicyVerify_WebApp/policy_verify/vehicle/vehicle?");
          //  c.resource("http://localhost:7101/travelservice-ViewController/resources/vehicle/vehicle?");
            MultivaluedMap queryParams = new MultivaluedMapImpl();             
                      if ((reg.isEmpty()==false) ){
               queryParams.add("doctype",searchtype );
               queryParams.add("policystr",policyno );
                   queryParams.add("reg", reg);                
               }
            else if (reg.isEmpty()&&eng.isEmpty()==false&&chass.isEmpty()&&certno.isEmpty()){
                          queryParams.add("doctype",searchtype );
               queryParams.add("policystr",policyno );
                queryParams.add("eng",eng );
            }
            else if (reg.isEmpty()&&eng.isEmpty()&&chass.isEmpty()==false&&certno.isEmpty()){
                          queryParams.add("doctype",searchtype );
               queryParams.add("policystr",policyno );
                queryParams.add("chass",chass );}
    else if (reg.isEmpty()&&eng.isEmpty()&&chass.isEmpty()&&certno.isEmpty()==false){
                          queryParams.add("doctype",searchtype );
               queryParams.add("policystr",policyno );
                queryParams.add("certno",certno );
            }        
            response = resource.queryParams(queryParams).get(String.class);
           }
                               catch(Exception ex)
                               {                       
                               mat[0][0]= "red";
                               mat[0][1]=ex.getMessage();
                                   return mat;}
                   
           try{ 
               String[] rows = response.split(",");
           matrix2 = new String[rows.length][2];
           JsonParser jp;
           jp = new JsonParser(); //from gson
           JsonElement root;
           root = jp.parse(response);
           JsonObject result = root.getAsJsonObject();
           JsonArray result2 = result.get("Policy Schedule").getAsJsonArray();
                 int length1 = result2.size();
               if (response.length()>1){
            for(int i=0; i<length1; i++)
           { result3 = result2.get(i).getAsJsonObject();}
           Set<Map.Entry<String, JsonElement>> entries = result3.entrySet(); 
              matrix2[0][0]="green";
               matrix2[0][1]="length of web service responce"+rows.length;
             int h=1;
           for (Map.Entry<String, JsonElement> entry: entries) {
               if (h<=21){
               matrix2[h][0]=entry.getKey().toString().replaceAll("\"", "");
               matrix2[h][1]=entry.getValue().toString().replaceAll("\"", "");             
               }h++;    
           }} else { 
                   matrix2[0][0]= "red";
                   }
                   return matrix2;
               }            
                      catch(Exception ex)
                      {                       
                       mat[0][0]= "red";
                      mat[0][1]=ex.getMessage();
                          return mat;}   
           
           
           // System.out.println(response);
           //          System.out.println(Arrays.deepToString(matrix));
           //         System.out.println(matrix.length);
           //          System.out.println(entries.size());
           //     System.out.println(entries);
           //     System.out.println(queryParams.toString());
           }
    
    
    
}
//           String breaker= "{\"Policy Schedule\":[{\"Product_Plan\":\"Gold\",\"Date_Of_Issuance\":\"2016-02-16 02:43:13.0\",\"EMAIL\":\"zaheer_ali_shah@yahoo.com\",\"Insured\":\"ZAHEER ALISHAH\",\"Date_Of_Birth\":\"1965-11-27 00:00:00.0\",\"Policy_Ticket_No\":\"0\",\"Package_Type\":\"Worldwide Including USA,Canada,Australia\",\"Policy_No\":\"2255001145/02/2016\",\"Travel_Tenure\":\"365\",\"CNIC\":\"3740504559131\",\"Issued_At\":\"Rawalpindi\",\"Type_Of_Coverage\":\"Individual\",\"Age\":\"51\",\"Postal_Address\":\"23-B, RAH-E-AMAN ROADNEW  LALA ZARRAWALPINDI\",\"Passport_No\":\"ER5149132\"}]}";         
// JSONObject obj = new JSONObject(response);          
//          JSONArray jArray = new JSONArray();
//         //jArray=(JSONArray) obj.get("Policy Schedule");
//          jArray.put(obj);
//         int  size = jArray.length();
//          for ( int i = 0; i < size; i++)
//             {            
//            objectInArray = jArray.getJSONObject(i);        
//        }
//                  for(int a = 0; a<matrix.length; a++)
//          { for(int b = 0; b<1; b++)
//              { System.out.print(matrix[a][b]); }  System.out.println(); }
//         
         // System.out.print(matrix[8][0]);
        //  System.out.print(response);
         // System.out.println(Arrays.deepToString(matrix));
        //System.out.println(objectInArray.toString());       
      // System.out.print(response);

             
         
