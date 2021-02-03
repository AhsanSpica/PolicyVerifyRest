package policy_verify;


import javax.ws.rs.GET;
import javax.ws.rs.Path;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("vehicle")
public class vehicleservice {
    public vehicleservice() {
    }

    @GET
        @Path("vehicle")
        @Consumes(MediaType.TEXT_PLAIN)
        @Produces(MediaType.APPLICATION_JSON)
        public Response getData(
        @QueryParam ("doctype") String doctype, 
                                     @QueryParam("policystr") String policystr,
                                     @QueryParam("reg")String reg,
                                     @QueryParam("eng")String eng,
                                     @QueryParam("chass")String chass, 
                                     @QueryParam("certno")String certno
            ) throws Exception {
            LinkedHashMap<String, String> jom = new LinkedHashMap<String, String>();

            String Query = null;
            int ok = 200; 
            String policyno=null;
            String branchid=null;
            String classid=null;
            String regcheck=null;
            String engcheck=null;
            String certcheck=null;
            String chasscheck=null;
            String covernote="";
            //takaful removed with br.comp_id=1 as constraint
        /*the Deployment Descriptor (or DD) of this web application which
         * is actually a webservice is a web.xml file 
         * */
            ResultSet rs=null;
        JSONObject o=new JSONObject();
            JSONObject jo=new JSONObject();
            JSONArray data = new JSONArray();             
        JSONArray ja=new JSONArray();
         
             try{
                     if(( Integer.parseInt(doctype)==1)||( Integer.parseInt(doctype)==2)||( Integer.parseInt(doctype)==3)||( Integer.parseInt(doctype)==4)){ 
                         if (policystr.indexOf('/', 10)!=-1){
                             covernote=policystr.substring(0, 10);
                               policyno = policystr.substring(4, 10); 
                             branchid=policystr.substring(0,3);
                             classid=policystr.substring(3,4);
                         }}
                    if((Integer.parseInt(doctype)==1) ||(Integer.parseInt(doctype)==2))
                 { Query = " Select (um.BRANCH_ID || um.CLASS_ID || ";
                 Query =Query+"TRIM(to_char(um.POLICY_NO,'000000')) || '/' ||";
                Query =Query+" to_char(um.ISSUE_DATE,'mm')||'/' ||um.ISSUE_YR) AS Policy_No"; 
                 Query =Query+", TRIM(um.ISSUE_DATE) AS Date_Of_Issuance";
                Query =Query+", TRIM(um.RISK_START_DATE) AS From_, TRIM(um.RISK_END_DATE) AS To_";
                 Query =Query+", br.BRANCH_NM as Branch_Name,  trim(um.CLIENT_NM) as"; 
                Query =Query+" Insured, um.ADDRESS_1||' '||um.ADDRESS_2||' '||um.ADDRESS_3 AS Postal_Address";
                 Query =Query+", um.ADDRESS_3 as city_";
                 Query =Query+ ",  TRIM(vh.DESCRIPTION) as Make,mm.MODEL_YEAR as Model_";
                 Query =Query+", TRIM(vh.CUBIC_CAPICITY_A) AS CC, TRIM(mm.REG_NO) as REG_NO, TRIM(mm.CHASIS_NO) as CHASIS_NO";
                Query =Query+", TRIM(mm.ENGINE_NO) as ENGINE_NO ";
                    Query=Query+", um.UW_DOC_ID";
                 Query=Query+" from UW_MAIN um left join MOTOR_MAIN mm";
                 Query =Query+" on um.UW_DOC_ID=mm.UW_DOC_ID"; 
              //  Query =Query+" left join PREM_SHARES ps on um.uw_doc_id = ps.uw_doc_id";
               Query =Query+ " left join EFU_MAST.BRANCHES br on um.BRANCH_ID = br.BRANCH_ID and br.COMP_ID=1"; 
               Query =Query+ " left join EFU_MAST.VEHICLE vh on mm.VEHICLE_ID = vh.VEHICLE_ID where";  
                   
                  if(Integer.parseInt(doctype)==1) {
                    Query =Query+" UM.POLICY_NO ='"+policyno +"' and um.POLICY_TYPE in ('P','C','E') and";
                        Query =Query+" um.BRANCH_ID='"+branchid+"' and um.CLASS_ID='"+classid+"'  and not (nvl(um.refund_cancel,' ')) in ('R','C') and ";
                    }
               if (Integer.parseInt(doctype)==2){Query=Query+" UM.CERTIFICATE_NO='"+policyno+"' and um.POLICY_TYPE in ('P','C','E') and";
                                                     Query =Query+" um.BRANCH_ID='"+branchid+"' and um.CLASS_ID='"+classid+"' and ";  
                                                 }}
             if (Integer.parseInt(doctype)==3){
                     Query = " Select (UM.UW_DOC_ID";
                                     Query =Query+" ||'/' ||to_char(um.ISSUE_DATE,'mm')||'/' ||um.ISSUE_YR) AS Policy_No"; 
                                      Query =Query+", TRIM(um.ISSUE_DATE) AS Date_Of_Issuance";
                                     Query =Query+", TRIM(um.RISK_START_DATE) AS From_, TRIM(um.RISK_END_DATE) AS To_";
                                      Query =Query+", br.BRANCH_NM as Branch_Name,  um.CLIENT_NM as"; 
                                     Query =Query+" Insured, um.ADDRESS_1||' '||um.ADDRESS_2||' '||um.ADDRESS_3 AS Postal_Address";
                                      Query =Query+", um.ADDRESS_3 as city_";
                                      Query =Query+ ",  TRIM(vh.DESCRIPTION) as Make,mm.MODEL_YEAR as Model_";
                                      Query =Query+", TRIM(vh.CUBIC_CAPICITY_A) AS CC, TRIM(mm.REG_NO) as REG_NO, TRIM(mm.CHASIS_NO) as CHASIS_NO";
                                     Query =Query+", TRIM(mm.ENGINE_NO) as ENGINE_NO ";
                                         Query=Query+", um.UW_DOC_ID";
                                      Query=Query+" from UW_MAIN um left join MOTOR_MAIN mm";
                                      Query =Query+" on um.UW_DOC_ID=mm.UW_DOC_ID"; 
                                   //  Query =Query+" left join PREM_SHARES ps on um.uw_doc_id = ps.uw_doc_id";
                                    Query =Query+ " left join EFU_MAST.BRANCHES br on um.BRANCH_ID = br.BRANCH_ID and br.COMP_ID=1"; 
                                    Query =Query+ " left join EFU_MAST.VEHICLE vh on mm.VEHICLE_ID = vh.VEHICLE_ID where";  
                 
                 Query=Query+" UM.UW_DOC_ID='"+covernote+"' and";}
            if(reg!=null) {Query=Query+"  TRIM(mm.REG_NO)= '"+reg+"' and"; regcheck=reg;}
            if(eng!=null){ Query=Query+" TRIM(mm.ENGINE_NO)='"+eng+"' and";engcheck=eng;  }
                    if(chass!=null){ Query=Query+" TRIM(mm.CHASIS_NO)='"+chass+"' and";chasscheck=chass;  }
//                    if(certno!=null){ Query=Query+" TRIM(UM.CERTIFICATE_NO)='"+certno+"' ";certcheck=certno;  }
                    Query =Query+"  um.RISK_END_DATE>sysdate  "; 
           
                
                 Class.forName("oracle.jdbc.driver.OracleDriver");
              //   Connection con =DriverManager.getConnection("jdbc:oracle:thin:@test.efuinsurance.com:1521:test","efu_gis", "test");
              Connection con =
                    DriverManager.getConnection("jdbc:oracle:thin:@adg.efuinsurance.com:1521/adg", "efu_gis", "PRODgis");
               //  DriverManager.getConnection("jdbc:oracle:thin:@oda01-scan.efuinsurance.com:1521/efuprd.efuinsurance.com", "efu_gis", "PRODgis");
              Statement ps = con.createStatement();  
                 
           ps.executeQuery(Query);
            rs= ps.getResultSet();
                 String cityvalue="";
                    jo = new JSONObject();
                    while (rs.next()) 
                               {           
                            int total_rows = rs.getMetaData().getColumnCount();
                                         for (int i = 0; i < total_rows; i++) {                                            
                            Object value = rs.getObject(i + 1);
                                 if (value != null) 
                                 {
                                     jo.put(rs.getMetaData().getColumnLabel(i + 1)
                                     .toLowerCase(), rs.getObject(i + 1).toString());}
                            if (value == null)
                            {          jo.put(rs.getMetaData().getColumnLabel(i + 1)
                                     .toLowerCase(), " ");  }
                                         }         
// jo.put("Policy_No",rs.getString("Policy_No"));
//                                   jo.put("Date_Of_Issuance",rs.getString("Date_Of_Issuance"));
//                            jo.put("From", rs.getString("From_"));
//                            jo.put("To", rs.getString("To_"));
//                            jo.put("Branch_Name",rs.getString("Branch_Name"));    
//                            jo.put("Insured",rs.getString("Insured"));
//                            jo.put("Postal_Address",rs.getString("Postal_Address"));                    
//                            jo.put("make", rs.getString("Make"));
//                            jo.put("model", rs.getString("Model_"));
//                            jo.put("CC", rs.getString("CC"));
//                            jo.put("REG_NO", rs.getString("REG_NO"));
//                            jo.put("CHASIS_NO", rs.getString("CHASIS_NO"));
//                            jo.put("ENGINE_NO", rs.getString("ENGINE_NO")); 
//                        cityvalue=rs.getString("city_");
//                            if (cityvalue==null||cityvalue.isEmpty())
//                            {cityvalue="'No value provided in database'";}
//                            jo.put("City", cityvalue);                            
                                   ja.put(jo);                         
                        } 
                    o.put("Policy Schedule", ja);
            con.close();    
                  //  System.out.print(Query);
                } 
         catch (Exception e) {             
         
              return Response.ok("{\"Error\": \""+e.getClass()+e.getLocalizedMessage()+e.getMessage()
                +"the covernote no  "+covernote+Query+"   the policy no is : "+policyno  +" The second check : "+reg+certno+chass+eng+ "\"}",
                                 MediaType.APPLICATION_JSON).build();     
         }              
            
            return Response.status(ok).entity(""+o+"").build();               
        }
}
