package policy_verify;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.LinkedHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("fire")
public class firepolicy {
    public firepolicy() {
    }

    @GET
    @Path("fire")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData(
    @QueryParam ("doctype") String doctype, 
                                 @QueryParam("policystr") String policystr,
                                 @QueryParam("gst")String gst,
                                 @QueryParam("ntn")String ntn,
                                 @QueryParam("nic")String nic
        ) throws Exception {
        LinkedHashMap<String, String> jom = new LinkedHashMap<String, String>();
        String Query = null;
        int ok = 200; 
        String policyno=null;
        String branchid=null;
        String classid=null;
        String secondcheck=null;
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
              policyno = policystr.substring(4, 10); 
            branchid=policystr.substring(0,3);
            classid=policystr.substring(3,4);
        }}
             //||Integer.parseInt(doctype)==3
        
             /*EFU_MAST.CITY.city_nm*/      
             Query ="Select DISTINCT (um.BRANCH_ID || um.CLASS_ID|| TRIM(to_char(um.POLICY_NO,'000000')) || '/' || ";
            Query =Query+" to_char(um.ISSUE_DATE,'mm')||'/' ||um.ISSUE_YR) AS Policy_No "; 
             Query =Query+", TRIM(um.ISSUE_DATE) AS Date_Of_Issuance"; 
             Query =Query+", TRIM(um.RISK_START_DATE) AS From_";
             Query=Query+", TRIM(um.RISK_END_DATE) AS To_,ct.CITY_NM AS Issued_At,";
             Query=Query+"  br.BRANCH_NM as Branch_Name";
            Query =Query+", um.CLIENT_NM as Insured_, um.ADDRESS_1||' '||um.ADDRESS_2||' '||um.ADDRESS_3";
             Query=Query+" AS Postal_Address, um.INSURED_CNIC as CNIC_, um.INSURED_GST_NO as GST_";
             Query =Query+", um.INSURED_NTN as NTN_, TRIM(cm.CMODT_DESC) as"; 
             Query =Query+" Commodity_,um.SUMINSURED ,(um.RISK_END_DATE- um.RISK_START_DATE)"; 
             Query=Query+" as No_Of_days,";
            Query =Query+" (um.UW_DOC_ID) as doc_id, um.CLIENT_ID, um.COVER_NOTE_NO";
             Query=Query+" as Cover_Note, um.AGENT_ID as Agency_ID";
             Query =Query+" from UW_MAIN um "; 
             Query =Query+" left join EFU_MAST.BRANCHES br";
             Query=Query+" on um.BRANCH_ID =br.BRANCH_ID left join";
             Query=Query+" efu_mast.city ct on br.CITY_ID=ct.CITY_ID left join"; 
              Query =Query+" EFU_MAST.COMMODITIES cm on um.COMMODITY_ID=cm.COMMODITY_ID where ";
             Query=Query+" um.RISK_END_DATE>sysdate  and";           
              if(Integer.parseInt(doctype)==1) {
                Query =Query+" UM.POLICY_NO ="+policyno+" and um.POLICY_TYPE in ('P','C','E') and ";  
                    Query =Query+" um.BRANCH_ID= "+branchid+" and not (nvl(um.refund_cancel,' ')) in ('R','C')  and um.CLASS_ID="+classid;             }
           if (Integer.parseInt(doctype)==2){Query=Query+" UM.CERTIFICATE_NO="+policyno+"  and um.POLICY_TYPE='C' and ";
          Query =Query+" um.BRANCH_ID= "+branchid+" and um.CLASS_ID="+classid;             }
         if (Integer.parseInt(doctype)==3){Query=Query+" UM.COVER_NOTE_NO ="+policyno+" and um.POLICY_TYPE='P' ";}
        if(gst!=null) {Query=Query+" and TRIM(um.INSURED_GST_NO)= "+gst; }       
                if(ntn!=null) {Query=Query+" and TRIM(um.INSURED_NTN)= "+ntn; } 
                if(nic!=null) {Query=Query+" and TRIM(um.INSURED_CNIC)= "+nic; }
           //  Query=Query+" and rownum=1";
                 
             Class.forName("oracle.jdbc.driver.OracleDriver");
        //  Connection con =DriverManager.getConnection("jdbc:oracle:thin:@test.efuinsurance.com:1521:test","efu_gis", "test");
                Connection con =
             
              //      DriverManager.getConnection("jdbc:oracle:thin:@oda01-scan.efuinsurance.com:1521/efuprd.efuinsurance.com", "efu_gis", "PRODgis");
                DriverManager.getConnection("jdbc:oracle:thin:@adg.efuinsurance.com:1521/adg", "efu_gis", "PRODgis");
          PreparedStatement ps = con.prepareStatement(Query); 
               
//            if (Integer.parseInt(doctype)==1||Integer.parseInt(doctype)==2)   
//            { int s=1;
//                   ps.setString(s++, policyno);
//               ps.setString(s++, branchid); 
//               ps.setString(s++, classid);
//           
//               if(gst!=null){
//               ps.setString(s++, gst.trim());}
//               if(ntn!=null){
//               ps.setString(s++, ntn.trim());}
//            if(nic!=null){
//            ps.setString(s++, nic.trim());}
//        }
//              else if (Integer.parseInt(doctype)==3)   
//                { int s=1;
//                        ps.setString(s++, policyno);
//                       
//                            if(gst!=null){
//                            ps.setString(s++, gst.trim());}
//                            if(ntn!=null){
//                            ps.setString(s++, ntn.trim());}
//                            if(nic!=null){
//                            ps.setString(s++, nic.trim());}
//                            }
                           
       ps.executeQuery();
        rs= ps.getResultSet();
                jo = new JSONObject(); 
                while (rs.next())  {                
                
                        int total_rows = rs.getMetaData().getColumnCount();
                                     for (int i = 0; i < total_rows; i++) {                                            
                        Object value = rs.getObject(i + 1);
                             if (value != null) 
                             {jom.put(rs.getMetaData().getColumnLabel(i + 1)
                                 .toLowerCase(), rs.getObject(i + 1).toString());}
                        if (value == null)
                        {          jom.put(rs.getMetaData().getColumnLabel(i + 1)
                                 .toLowerCase(), " ");  }
                                     }         
                    } ja.put(jom);  o.put("Policy Schedule", ja);            
        con.close();  
                System.out.print(Query); 
            } 
     catch (Exception e) {             
     
          return Response.ok("{\"Error\": \""+Query+"  "+e.getMessage()+"  "+e.getStackTrace()+e.getCause()+"   the policy no is : "+policyno+"branch id : "+branchid+"classid : "+classid+" The second check : "+ntn+ "\"}",
                             MediaType.APPLICATION_JSON).build();     
     }             
        return Response.status(ok).entity(""+o+"").build();               
    }
}
