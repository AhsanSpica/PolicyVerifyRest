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

@Path("travel")
public class travelservice {
    public travelservice() {
    }

    @GET
       @Path("travel")
       @Consumes(MediaType.TEXT_PLAIN)
       @Produces(MediaType.APPLICATION_JSON)
       public Response getData( @QueryParam ("doctype") String doctype, @QueryParam("policystr") String policystr,@QueryParam("passno")
                               String passno,@QueryParam("CNIC")
                               String CNIC) throws Exception {
           LinkedHashMap<String, String> jom = new LinkedHashMap<String, String>();
           String Query = null;
           int ok = 200; 
           String policyno=null;
           int policyint=0;
           String branchid=null;
           String classid=null;
           String niccheck=null;
           String passcheck=null;
           LinkedHashMap<String, String> jon = new LinkedHashMap<String, String>();
          String can="";
           /*the Deployment Descriptor (or DD) of this web application which
            * is actually a webservice is a web.xml file 
            * */
           int iter=0;
           ResultSet rs=null;
           ResultSet rs2=null;
           ResultSet rs5=null;
       JSONObject o=new JSONObject();
           JSONObject jo=new JSONObject();
           JSONArray data = new JSONArray();             
       JSONArray ja=new JSONArray();
       String cq="";
       String dq="";
           //takaful removed with br.comp_id=1 as constraint
        
            
       try{  if(( Integer.parseInt(doctype)==1)||( Integer.parseInt(doctype)==2)||( Integer.parseInt(doctype)==3)||( Integer.parseInt(doctype)==4))
                   { 
                       if (policystr.indexOf('/', 10)!=-1){
                             policyno = policystr.substring(4, 10); 
                             policyint=Integer.parseInt( policyno) ;
                           branchid=policystr.substring(0,3);
                           classid=policystr.substring(3,4);
                       }}
           }
           catch (Exception e) {             
           
                return Response.ok("{\"Error\": \""+e.getCause() +e.getLocalizedMessage()+e.getMessage()+"the policy no "+policyno  + "\"}",
                                   MediaType.APPLICATION_JSON).build();     
           }  
                   Class.forName("oracle.jdbc.driver.OracleDriver");
           Connection con =
          // DriverManager.getConnection("jdbc:oracle:thin:@adg.efuinsurance.com:1521/adg", "efu_gis", "PRODgis");
             //  DriverManager.getConnection("jdbc:oracle:thin:@oda01-scan.efuinsurance.com:1521/efuprd.efuinsurance.com", "efu_gis", "PRODgis");
             DriverManager.getConnection("jdbc:oracle:thin:@oda01-scan.efuinsurance.com:1521/efuprd.efuinsurance.com", "efu_gis", "PRODgis");
           
                dq=" select um.CLASS_ID,TRIM(nvl(um.REFUND_CANCEL,'OK')) rc from UW_MAIN um left join ETRAVEL  e on um.UW_DOC_ID=e.UW_DOC_ID" ;
                dq=dq+" where TRIM(UM.POLICY_NO) ="+policyno+" and TRIM(um.BRANCH_ID)="+branchid+" ";
              //  dq=dq+"  and TRIM(um.REFUND_CANCEL) IN ('R','C')   ";
                dq=dq+" and um.POLICY_TYPE in ";
                dq=dq+" ('E') and um.CLASS_ID="+ classid;
                   try{ 
                
               
                       PreparedStatement ps4 = con.prepareStatement(dq);  
                   ps4.executeQuery();
                   rs5= ps4.getResultSet();
                   while (rs5.next()){can=rs5.getString(2).toString();
                   }
                               }
           catch (Exception e) {             
           
                return Response.ok("{\"Error for refund check query \": \""+dq+"    "+e.getCause() +e.getLocalizedMessage()+e.getMessage()+"the policy no "+policyno  + "\"}",
                                   MediaType.APPLICATION_JSON).build();     
           }
           
                   if (can.equals("C")||can.equals("R") ){
            return Response.ok("{\"Canceled\": \""+can + "\"}",
                                MediaType.APPLICATION_JSON).build();        }
                      else{
               cq="   select st.UW_DOC_ID  as doc_from_student from uw_main u,    STUDENT_TRAVEL st   where   ";
                 cq=cq+"   u.POLICY_NO ="+policyno+" and u.POLICY_TYPE in ('P') and u.BRANCH_ID="+branchid+" and u.CLASS_ID=5 ";
          cq=cq+" and u.uw_doc_id = st.uw_doc_id  (+)    " ;
               
               
                   PreparedStatement ps3 = con.prepareStatement(cq);  
                       try{
                           ps3.executeQuery();
                    rs2= ps3.getResultSet();
                while (rs2.next()){ jon.put( rs2.getMetaData().getColumnName(1),rs2.getString(1));
                }
                           }
               catch (Exception e) {             
               
                    return Response.ok("{\"Error\": \""+e.getCause() +e.getLocalizedMessage()+e.getMessage()+"the policy no "+policyno  + "\"}",
                                       MediaType.APPLICATION_JSON).build();     
               } 
                       
                   if(!jon.containsKey("doc_from_student")){
                       Query = "Select DISTINCT (um.BRANCH_ID || um.CLASS_ID || TRIM(to_char(um.POLICY_NO,'000000')) || '/' ||";
     Query =Query+" to_char(um.ISSUE_DATE,'mm')||'/'||um.ISSUE_YR) AS Policy_No, TRIM( um.ISSUE_DATE) AS Date_Of_Issuance,ct.CITY_NM AS Issued_At,";
    Query =Query+"   TRIM(um.RISK_START_DATE) AS From_DT, TRIM(um.RISK_END_DATE) AS To_DT,(TRIM(FIRST_NM)||TRIM(MIDDLE_NM)||TRIM(LAST_NM))";
    Query =Query+"  AS Insured, TRIM(etravel.HOME_ADDRESS_1)||' '||TRIM(etravel.HOME_ADDRESS_2)||' '||TRIM(etravel.HOME_ADDRESS_3) AS Postal_Address"; 
       Query=Query+", nvl(TRIM(etravel.HOME_ADDRESS_3),'-') City_Name, br.BRANCH_NM as Branch_Name";
                Query =Query+", TRIM(etravel.PASSPORT_NO) AS Passport_No, TRIM(etravel.CONTACT_NO) AS Contact, TRIM(etravel.CNIC) AS CNIC,"; 
                Query =Query+ " TRIM(etravel.EMAIL) AS EMAIL, TRIM(ETRAVEL.TICKET_NO) AS Policy_Ticket_No, TRIM(E_TRAVEL_MATRIX.PACKAGE_NAME) AS Package_Type,";
                Query =Query+ " TRIM(E_TRAVEL_MATRIX.product_name) AS Product_Plan, TRIM(etravel.ins_birth_dt) AS Date_Of_Birth";
                  Query =Query+ ", (TRIM(ct1.Full_NM)||' '||TRIM(cttwo.FULL_NM)||' '||TRIM(ct3.FULL_NM)||' '||TRIM(ct4.FULL_NM)) AS Destinations";
                  Query =Query+", TRIM(E_TRAVEL_MATRIX.coverage_name) AS Type_Of_Coverage, TRIM(ett.TRAVEL_TENURE_DESC)   AS Travel_Tenure,";                
                  Query =Query+ " ROUND(MONTHS_BETWEEN(SYSDATE,etravel.ins_birth_dt)/12) AS Age, um.UW_DOC_ID";
                  Query =Query+ " from etravel  left join UW_MAIN um on um.UW_DOC_ID=etravel.UW_DOC_ID ";
                      Query =Query+ "left join EFU_MAST.E_TRAVEL_TENURE ett on etravel.TENURE_MAX_STAY=ett.TRAVEL_TENURE_ID ";
                   Query =Query+" left JOIN E_TRAVEL_MATRIX ON ETRAVEL.TRV_PACKAGE=E_TRAVEL_MATRIX.PACKAGE_ID AND";
                   Query =Query+" etravel.coverage=E_TRAVEL_MATRIX.coverage_ID AND";
                  Query =Query+ " etravel.PRODUCT_PLAN=E_TRAVEL_MATRIX.PRODUCT_ID AND";
                  Query =Query+ " etravel.TENURE_MAX_STAY=E_TRAVEL_MATRIX.tenure_id AND";
                 Query =Query+ " E_TRAVEL_MATRIX.client_id=999999 and um.comp_id=1";
                   Query=Query+" right join EFU_MAST.BRANCHES br on um.BRANCH_ID=br.BRANCH_ID AND BR.COMP_ID=1 ";
                   Query=Query+" LEFT join efu_mast.city ct on br.CITY_ID=ct.CITY_ID";
                  Query =Query+ " LEFT join efu_mast.Country ct1 on etravel.Country_Visit_1=ct1.COUNTRY_ID";
                 Query =Query+ " LEFT join efu_mast.Country cttwo on etravel.Country_Visit_2=cttwo.COUNTRY_ID";
                  Query =Query+ " LEFT join efu_mast.Country ct3 on etravel.Country_Visit_3=ct3.COUNTRY_ID";
                  Query =Query+ " LEFT join efu_mast.Country ct4 on etravel.Country_Visit_4=ct4.COUNTRY_ID";
                  Query =Query+ "  where um.RISK_END_DATE>sysdate and";}
                   else{
                Query="  Select DISTINCT (um.BRANCH_ID || um.CLASS_ID || TRIM(to_char(um.POLICY_NO,'000000')) || '/' ||";
                    Query =Query+" to_char(um.ISSUE_DATE,'mm')||'/'||um.ISSUE_YR)   AS Policy_No, ";
                  Query =Query+" TRIM( um.ISSUE_DATE) AS Date_Of_Issuance,  ct.CITY_NM AS Issued_At,   TRIM(um.RISK_START_DATE) AS From_DT, ";
                  Query =Query+"   TRIM(um.RISK_END_DATE) AS To_DT,(STD.INSURED_NM)  AS Insured,STD.POSTAL_ADDRESS_1||' '||STD.POSTAL_ADDRESS_2||' '";
                  Query =Query+"  ||STD.POSTAL_ADDRESS_3 AS Postal_Address, nvl(STD.POSTAL_ADDRESS_3,'-') City_Name, br.BRANCH_NM as Branch_Name, ";
                 Query =Query+" STD.PASSPORT_NO AS Passport_No ,STD.CONTACT_NO AS Contact,STD.CNIC AS CNIC, STD.EMAIL AS EMAIL,STD.TICKET_NO AS Policy_Ticket_No, ";
                 Query =Query+"  TP.TRAVEL_PACKAGE_DESC AS Package_Type, TPT.TRAVEL_PRODUCT_DESC AS Product_Plan, TRIM(STD.INSURED_DOB) AS Date_Of_Birth,";
                                                                                                                                         
                  Query =Query+"   TRIM(ct1.Full_NM) AS Destinations,  TC.TRAVEL_COVERAGE_DESC AS Type_Of_Coverage,";
                  Query =Query+"      TRIM(TTN.TRAVEL_TENURE_DESC)  AS Travel_Tenure, ROUND(MONTHS_BETWEEN(SYSDATE,STD.INSURED_DOB)/12) AS Age, um.UW_DOC_ID ";
                  Query =Query+"   from STUDENT_TRAVEL STD  left join UW_MAIN um on um.UW_DOC_ID=STD.UW_DOC_ID ";
                   Query =Query+" LEFT JOIN EFU_MAST.TRAVEL_PACKAGE TP ON STD.TRAVEL_PACKAGE_ID= TP.TRAVEL_PACKAGE_ID";
                   Query =Query+"     LEFT JOIN  EFU_MAST.TRAVEL_COVERAGE TC ON STD.TRAVEL_COVERAGE_ID=TC.TRAVEL_COVERAGE_ID";
                   Query =Query+"    LEFT JOIN  EFU_MAST.TRAVEL_PRODUCT TPT ON STD.TRAVEL_PRODUCT_ID=TPT.TRAVEL_PRODUCT_ID ";
                   Query =Query+"    LEFT JOIN EFU_MAST.ST_TRAVEL_TENURE TTN ON STD.TRAVEL_TENURE_ID=TTN.TRAVEL_TENURE_ID";
                   Query =Query+"   and um.comp_id=1 right join EFU_MAST.BRANCHES br on um.BRANCH_ID=br.BRANCH_ID AND BR.COMP_ID=1 full join";
                  Query =Query+"  efu_mast.city ct on br.CITY_ID=ct.CITY_ID full join  efu_mast.Country ct1 on STD.COUNTRY_1=ct1.COUNTRY_ID ";
                  Query =Query+" where um.RISK_END_DATE>sysdate  and ";}
                
                
                 if(Integer.parseInt(doctype)==1) {
                   Query =Query+" TRIM(um.POLICY_NO) ="+policyint+"   and um.POLICY_TYPE in ('P') and";
                                   Query =Query+" TRIM(um.BRANCH_ID)=TRIM('"+branchid+"') and um.CLASS_ID=5   ";   }
                 
                 if(Integer.parseInt(doctype)==4) {
                      Query =Query+" um.ENDT_NO ="+policyint+ " and um.POLICY_TYPE in ('E') and";
                                       Query =Query+" um.BRANCH_ID="+branchid+" and um.CLASS_ID=5   ";    }
                 
                  if (Integer.parseInt(doctype)==2){Query=Query+" TRIM(UM.CERTIFICATE_NO)="+policyno+" and um.POLICY_TYPE in ('P','C','E') and";}
           if (Integer.parseInt(doctype)==3){Query=Query+" TRIM(UM.COVER_NOTE_NO) ="+policyno+" and um.POLICY_TYPE='P' and";}
                  // if(passno!=null) {Query=Query+" TRIM(etravel.PASSPORT_NO)='"+passno+"' and"; }
                 //  if(CNIC!=null){ Query=Query+" TRIM(etravel.CNIC)='"+CNIC+"' and"; }
                 if(!jon.containsKey("doc_from_student")){
           if(passno!=null) {Query=Query+" and TRIM(etravel.PASSPORT_NO)='"+passno+"' "; passcheck=passno; }
           if(CNIC!=null){ Query=Query+" and TRIM(etravel.CNIC)="+CNIC+" ";niccheck=CNIC;  }
           // Query =Query+"  order by um.issue_date DESC  ";
                 }
                  
                 else {           if(passno!=null) {Query=Query+" and TRIM(STD.PASSPORT_NO)= '"+passno+ "' "; passcheck=passno; }
           if(CNIC!=null) { Query=Query+" and TRIM(STD.CNIC)= '"+CNIC+"' ";niccheck=CNIC;  }
           
           // Query =Query+"  order by um.issue_date DESC  ";
                 }
                 
                
                 
          // System.out.print(Query+"  ");
       
             PreparedStatement ps = con.prepareStatement(Query);  
//               if (Integer.parseInt(doctype)==1||Integer.parseInt(doctype)==4)   
//               { int s=1;
//                   ps.setString(s++, policyno);
//               ps.setString(s++, branchid); 
//               
//               ps.setString(s++, classid);
//               if(passno!=null){
//               ps.setString(s++, passcheck.trim());}
//               if(CNIC!=null){
//               ps.setString(s++, niccheck.trim());}
////               for (int i=1; i <= iter; i++){
////                   if(passno!=null){
////           ps.setString(iter+3, passcheck.trim());}
////               if(CNIC!=null){
////               ps.setString(iter+3, niccheck.trim());}
////           }
//           }
//                 else if (Integer.parseInt(doctype)==2||Integer.parseInt(doctype)==3)   
//                   {int s=1;
//                   ps.setString(s++, policyno);  
//                      if(passno!=null){
//                      ps.setString(s++, passcheck.trim());}
//                      if(CNIC!=null){
//                      ps.setString(s++, niccheck.trim());}
//                  } 
                       try{  ps.executeQuery();
           rs= ps.getResultSet();
                   jo = new JSONObject(); 
                   while (rs.next()) 
                              {                
                                        
                   int total_rows = rs.getMetaData().getColumnCount();
                                for (int i = 0; i < total_rows; i++) {                                            
                 Object value = rs.getObject(i + 1);
                        if (value != null) 
                        {
                            jom.put(rs.getMetaData().getColumnLabel(i + 1)
                            .toLowerCase(), rs.getObject(i + 1).toString());}
                 if (value == null) 
                 {          jom.put(rs.getMetaData().getColumnLabel(i + 1)
                            .toLowerCase(), " ");  }
                                }         
                       
//                                jo.put("Policy_No",rs.getString("Policy_No"));
//                                  jo.put("Date_Of_Issuance",rs.getString("Date_Of_Issuance"));
//                                  jo.put("Issued_At",rs.getString("Issued_At"));
//                           jo.put("Insured",rs.getString("Insured"));
//                           jo.put("Postal_Address",rs.getString("Postal_Address"));                    
//                           jo.put("Passport_No",rs.getString("Passport_No"));
//                           jo.put("Product_Plan",rs.getString("Product_Plan"));
//                           jo.put("CNIC",rs.getString("CNIC"));
//                       String emailvalue=rs.getString("EMAIL");
//                       String city_nm=rs.getString("City_Name");
//                       if (emailvalue==null||emailvalue.isEmpty())
//                       {emailvalue="'No value provided in database'";}
//                           if (city_nm==null||city_nm.isEmpty())
//                           {city_nm="'No value provided in database'";}
//                           jo.put("EMAIL",emailvalue);
//                           jo.put("Policy_Ticket_No",rs.getString("Policy_Ticket_No"));                   
//                           jo.put("Package_Type",rs.getString("Package_Type"));
//                           jo.put("Product_Plan",rs.getString("Product_Plan"));
//                           jo.put("Date_Of_Birth",rs.getString("Date_Of_Birth"));
//                           jo.put("Destinations",rs.getString("Destinations"));
//                           jo.put("Type_Of_Coverage",rs.getString("Type_Of_Coverage"));         
//                           jo.put("Travel_Tenure",rs.getString("Travel_Tenure"));
//                           jo.put("Age",rs.getString("Age")); 
//                         jo.put("From", rs.getString("From_DT"));
//                       jo.put("To", rs.getString("To_DT"));
//                           jo.put("Contact", rs.getString("Contact"));
//                           jo.put("City", city_nm);
//                             jo.put("Branch_Name",rs.getString("Branch_Name"));    
                                  ja.put(jom);
                              
                       }
                           
                   o.put("Policy Schedule", ja);
           con.close();                    
                 } 
        catch (Exception e) {             
        
             return Response.ok("{\"Error\": \""+e.getCause() +e.getLocalizedMessage()+e.getMessage()+"the policy no "+policyno  + "\"}",
                                MediaType.APPLICATION_JSON).build();     
        }  
                   
           
           return Response.status(ok).entity(""+o+"").build();   } 
          
                       
       }
}
