package policy_verify;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.ResultSetMetaData;

import java.util.Date;
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

@Path("marine")
public class marinepolicy {
    public marinepolicy() {
    }

    @GET
    @Path("marine")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData(
    @QueryParam ("doctype") String doctype, 
                                 @QueryParam("policystr") String policystr,
                                 @QueryParam("clientcode")String clientcode
        ) throws Exception {
        LinkedHashMap<String, String> jom = new LinkedHashMap<String, String>();
        String Query = null;
        int ok = 200; String q1="";
        String name="";
        String policyno=null;
        String branchid=null;
        String classid=null;
        String secondcheck=null;
       float  newval=0;
       
       
       int type_id=0; String opencheck="";String truemaker="false";
        ResultSet rs5=null;
        String can="";
        String dq=""; String Querex=""; int addendum=0; String addtext="";
    /*the Deployment Descriptor (or DD) of this web application which
     * is actually a webservice is a web.xml file 
     * */
        ResultSet rs=null; ResultSet rs2=null; ResultSet rs3=null;
    JSONObject o=new JSONObject();
        JSONObject jo=new JSONObject();
        JSONArray data = new JSONArray();             
    JSONArray ja=new JSONArray();
     
        
    if(( Integer.parseInt(doctype)==1)||( Integer.parseInt(doctype)==2)||( Integer.parseInt(doctype)==3)||( Integer.parseInt(doctype)==4)){ 
        if (policystr.indexOf('/', 10)!=-1){
              policyno = policystr.substring(4, 10); 
            branchid=policystr.substring(0,3);
            classid=policystr.substring(3,4);
        }}
             //||Integer.parseInt(doctype)==3
             Class.forName("oracle.jdbc.driver.OracleDriver");
            // Connection con =DriverManager.getConnection("jdbc:oracle:thin:@test.efuinsurance.com:1521:test","efu_gis", "test");
            Connection con =
             
     //   DriverManager.getConnection("jdbc:oracle:thin:@oda01-scan.efuinsurance.com:1521/efuprd.efuinsurance.com", "efu_gis", "PRODgis");
      
      
     DriverManager.getConnection("jdbc:oracle:thin:@adg.efuinsurance.com:1521/adg", "efu_gis", "PRODgis");
            
           try {   if( Integer.parseInt(doctype)==1){
        dq=" select TRIM(um.REFUND_CANCEL) rc from UW_MAIN um " ;
        dq=dq+" where TRIM(UM.POLICY_NO) ="+policyno+" and TRIM(um.BRANCH_ID)="+branchid+" and TRIM(um.REFUND_CANCEL) IN ('R','C')  and um.POLICY_TYPE in";
           dq=dq+" ('E') and um.CLASS_ID="+ classid+" and TRIM(um.CLIENT_ID)="+ clientcode;
             }
        if( Integer.parseInt(doctype)==2){
        dq=" select TRIM(um.REFUND_CANCEL) rc from UW_MAIN um " ;
        dq=dq+" where TRIM(UM.CERTIFICATE_NO) ="+policyno+" and TRIM(um.BRANCH_ID)="+branchid+" and TRIM(um.REFUND_CANCEL) IN ('R','C')  and um.POLICY_TYPE in";
        dq=dq+" ('E') and um.CLASS_ID="+ classid+" and TRIM(um.CLIENT_ID)="+ clientcode;
        }
        if( Integer.parseInt(doctype)==3){
        dq=" select TRIM(um.REFUND_CANCEL) rc  from covernote cn LEFT join uw_main um on cn.COVER_NOTE_NO=um.COVER_NOTE_NO " ;
        dq=dq+" where TRIM(UM.COVER_NOTE_NO) ="+policyno+" and TRIM(um.BRANCH_ID)="+branchid+" and cn.HOW_PRINT in ('C')  ";
        dq=dq+" and um.CLASS_ID=1 and TRIM(um.CLIENT_ID)="+ clientcode;
        }
               PreparedStatement ps4 = con.prepareStatement(dq);  
           ps4.executeQuery();
           rs5= ps4.getResultSet();
           while (rs5.next()){can=rs5.getString("rc").toString();
           }}
        catch(Exception ex){          return Response.ok("{\"Refund/Cancel Error\": \""+ex.getCause()+ex.getClass()+ex.getLocalizedMessage()+q1+" this is the refund cancel query : "+dq +" P : "+policyno+" The second check : "+secondcheck+ "\"}",
                                     MediaType.APPLICATION_JSON).build();    }
            
           if (can.equals("C")||can.equals("R") ){
        return Response.ok("{\"Canceled\": \""+can + "\"}",
                        MediaType.APPLICATION_JSON).build();        }
              else{
                try{    
            if (Integer.parseInt(doctype)==3){
                    Query ="Select Distinct (cn.BRANCH_ID || 1|| TRIM(to_char(cn.COVER_NOTE_NO,'000000')) || '/' ||";
                    Query =Query+" to_char(cn.ISSUE_DATE,'mm')||'/' ||cn.COVER_NOTE_YR) AS Policy_No,";
                    Query =Query+" TRIM(cn.ISSUE_DATE) AS Date_Of_Issuance";
                    Query =Query+", TRIM(cn.ISSUE_DATE) AS From_, TRIM(cn.EXPIRY_DATE) AS To_, br.BRANCH_NM as Branch_Name, ";
                    Query=Query+"  cn.CLIENT_NM as Insured_,ct.CITY_NM AS Issued_At, ";
                    Query =Query+"(TRIM(cn.ADDRESS_1)||' '||TRIM(cn.ADDRESS_2)||' '||TRIM(cn.ADDRESS_3)) AS Postal_Address, cn.CNIC  as CNIC_,";
                    Query =Query+"   TRIM(TRIM(cn.INTEREST_1)||TRIM(cn.INTEREST_2)||TRIM(cn.INTEREST_3)";
                    Query =Query+"||TRIM(cn.INTEREST_4)||TRIM(cn.INTEREST_5)) as Commodity_,CN.SHIPED_FROM as Voyage_From";
                    Query =Query+", CN.SHIPED_TO as ship_to, cn.SHIP_NM as Carrier_Name, DECODE (cn.CARRIER,'S',  'Any Approved Vessel'";
                    Query =Query+",'A','Any Approved Aircraft','T', 'By Truck/Trailer','R', 'By Rail') as Carrier_Mode";
                    Query =Query+", mc.UW_DOC_ID from covernote cn ";
                   // Query=Query+" LEFT join uw_main um on cn.COVER_NOTE_NO=um.COVER_NOTE_NO ";
                    Query =Query+ "  LEFT   join MARINE_CARGO mc on cn.COVER_NOTE_ID=mc.COVER_NOTE_ID ";
                    Query=Query+" LEFT  join EFU_MAST.BRANCHES br on  cn.BRANCH_ID = br.BRANCH_ID  left join"; 
                    Query=Query+" efu_mast.city ct on br.CITY_ID=ct.CITY_ID ";
                    Query =Query+"where ";
                    Query =Query+"cn.COVER_NOTE_NO=?  and cn.BRANCH_ID=? and TRIM(cn.CLIENT_ID)=? ";
            //requirement changedas on 21st of march 2018 
            //documents should be verfied even if they are expired
            // Query =Query+"  and cn.EXPIRY_DATE>sysdate"; 
                    Query =Query+" and rownum=1";
                   // Query =Query+" and um.POLICY_TYPE in ('P','C','E') and not (nvl(um.refund_cancel,' ')) in ('R','C') and rownum=1";
                    secondcheck=clientcode;
                    q1="select Unique  nvl(prem.NET_AMT,'0'), ((prem.NET_AMT+nvl(ANA.DEB,0))-nvl(ANA.cred,0)) AS PREMIUM_PAID,(select TRIM(nvl(mc.OPEN_NONOPEN,' '))  " ;
                       q1=q1+ "from UW_MAIN um left join MARINE_CARGO mc on um.UW_DOC_ID=mc.uw_doc_id where um.POLICY_NO="+policyno+" and um.CLASS_ID=1 and um.POLICY_TYPE='P' and um.BRANCH_ID="+ branchid+") open_pol,"; 
                    q1=q1+"  nvl(umi.SUB_CLASS_TYPE_ID,0), nvl(ANA.DEB,0), nvl(ANA.cred,0), nvl((ANA.DEB-ANA.cred),0) D_C";
                    q1=q1+" FROM (Select Unique pr.NET_AMT,";
                    q1=q1+" um.uw_doc_id , sum (ABS(DECODE(vd.DRCR,2,vd.VCH_AMT) )) cred,";
                    q1=q1+" sum(ABS(DECODE(vd.DRCR,1,vd.VCH_AMT) )) deb from" ;
                       q1=q1+ " UW_MAIN um ";
                               q1=q1+"  LEFT  JOIN VIEW_RECOVERY_4_WEB";
                              q1=q1+"   ON VIEW_RECOVERY_4_WEB.class_id       =um.class_id";
                             q1=q1+"    AND VIEW_RECOVERY_4_WEB.policy_no     =um.policy_no";
                             q1=q1+"    AND VIEW_RECOVERY_4_WEB.CERTIFICATE_NO=um.CERTIFICATE_NO";
                              q1=q1+"   AND VIEW_RECOVERY_4_WEB.ENDT_NO       =um.ENDT_NO";
                             q1=q1+"    AND VIEW_RECOVERY_4_WEB.policy_type   =um.policy_type";
                             q1=q1+"    AND VIEW_RECOVERY_4_WEB.branch_id     =um.branch_id";
                                 q1=q1+"  LEFT  join VOUCHER_DETAIL vd  on um.UW_DOC_ID=vd.UW_DOC_ID  ";
                               
                    q1=q1+" LEFT join PREM_SHARES pr on um.uw_doc_id=pr.UW_DOC_ID left join VOUCHER_MASTER vm on  vd.VCH_MAST_ID=vm.VCH_MAST_ID "; 
                    q1=q1+" left join covernote cn on um.COVER_NOTE_NO= cn.COVER_NOTE_NO";
                    
        q1=q1+" where cn.COVER_NOTE_NO="+policyno+" and um.CLASS_ID="+classid+ " and ";   
 q1=q1+" um.POLICY_TYPE='P' and vm.VCH_TYPE NOT IN ('A') and  pr.COINS_BRANCH_ID='110000' AND SUBSTR(vd.gl_id,0,4) IN (3491,4271,3251)" ;
      q1=q1+" and um.BRANCH_ID="+branchid+" and pr.NET_AMT>0" ;
         q1=q1+  " group by um.uw_doc_id,  pr.NET_AMT) ana";
                   q1=q1+  "  full  join  UW_MAIN UMI ON ana.UW_DOC_ID=UMI.UW_DOC_ID ";
                //  q1=q1+  "   left join EFU_MAST.SUB_CLASS_TYPE sct on umi.SUB_CLASS_TYPE_ID=sct.SUB_CLASS_TYPE_ID";
            //   q1=q1+  "      and umi.COMP_ID=sct.SUB_CLASS_ID ";
                 q1=q1+  "    LEFT  join covernote cni on UMI.COVER_NOTE_NO = cni.COVER_NOTE_NO    ";
                  q1=q1+  "           left  join PREM_SHARES prem on umi.UW_DOC_ID=prem.UW_DOC_ID  left  join VOUCHER_DETAIL vd";
                 q1=q1+  "    on umi.UW_DOC_ID=vd.UW_DOC_ID";
                  q1=q1+  "   left  JOIN VIEW_RECOVERY_4_WEB   ON VIEW_RECOVERY_4_WEB.class_id       =umi.class_id   AND VIEW_RECOVERY_4_WEB.policy_no";
                  q1=q1+  "   =umi.policy_no   AND VIEW_RECOVERY_4_WEB.CERTIFICATE_NO=umi.CERTIFICATE_NO  AND VIEW_RECOVERY_4_WEB.ENDT_NO       =umi.ENDT_NO  AND VIEW_RECOVERY_4_WEB.policy_type";
                 q1=q1+  "    =umi.policy_type  AND VIEW_RECOVERY_4_WEB.branch_id     =umi.branch_id";


                    q1=q1+" WHERE prem.COINS_BRANCH_ID='110000' AND SUBSTR(vd.gl_id,0,4) IN (3491,4271,3251) and"; 
q1=q1+"  cni.COVER_NOTE_NO="+policyno+" and UMI.CLASS_ID="+classid+" and UMI.POLICY_TYPE='P'";
q1=q1+" and UMI.BRANCH_ID="+branchid+"   and rownum=1 ";  
//expiry clause removed
//q1=q1+" and cni.EXPIRY_DATE>sysdate";                     
         //Querex="select Count(ad.ADDENDUM_ID) add_cn from COVERNOTE cn left join ADDENDUM ad on cn.COVER_NOTE_ID=ad.COVER_NOTE_ID where cn.COVER_NOTE_NO=031839  and cn.BRANCH_ID=219 and TRIM(cn.CLIENT_ID)=223129 "; 
    Querex=" select TRIM(to_char(ad.ISSUE_DATE, 'dd-MON-yy')),(Select count(ad.ADDENDUM_ID) "; 
    Querex=Querex+"from COVERNOTE cn left join ADDENDUM ad on cn.COVER_NOTE_ID=ad.COVER_NOTE_ID where cn.COVER_NOTE_NO=060828 and  ";
               Querex=Querex+" cn.BRANCH_ID="+branchid+" and TRIM(cn.CLIENT_ID)="+clientcode+") COUNT_ADD ";
               Querex=Querex+" from COVERNOTE cn left join ADDENDUM ad on cn.COVER_NOTE_ID=ad.COVER_NOTE_ID where cn.COVER_NOTE_NO="+policyno+" and ";
               Querex=Querex+" cn.BRANCH_ID="+branchid+" and TRIM(cn.CLIENT_ID)="+clientcode+" order by ad.ISSUE_DATE"; }
               
             if((Integer.parseInt(doctype)==1)||(Integer.parseInt(doctype)==2)) {
                 
                 
               Query ="Select Unique (um.BRANCH_ID || um.CLASS_ID|| TRIM(to_char(um.POLICY_NO,'000000')) || '/' || ";
            Query =Query+"to_char(um.ISSUE_DATE,'mm')||'/' ||um.ISSUE_YR) AS Policy_No, TRIM(um.ISSUE_DATE) AS Date_Of_Issuance"; 
             Query =Query+", TRIM(um.RISK_START_DATE) AS From_, TRIM(um.RISK_END_DATE) AS To_, br.BRANCH_NM as Branch_Name";
            Query =Query+", um.CLIENT_NM as Insured_,  ct.CITY_NM AS Issued_At, (um.ADDRESS_1||' '||um.ADDRESS_2||' '||";
             Query =Query+"um.ADDRESS_3) AS Postal_Address, um.INSURED_CNIC as CNIC_, um.INSURED_GST_NO AS GST_"; 
                Query =Query+", um.INSURED_NTN as NTN_, TRIM(cm.CMODT_DESC) as Commodity_,TRIM(mc.SHIP_FROM) as Voyage_From";
            Query =Query+", TRIM(mc.SHIP_TO) as ship_to, TRIM(mc.CONVEYANCE) as Carrier_Name, DECODE (mc.CARRIER_BY,'S', 'Any Approved Vessel'";
             Query =Query+" ,'A','Any Approved Aircraft','T', 'By Truck/Trailer','R', 'By Rail') as Carrier_Mode"; 
             Query=Query+", um.UW_DOC_ID";
        Query=Query+" from UW_MAIN um left  join MARINE_CARGO mc on um.UW_DOC_ID=mc.UW_DOC_ID ";
             Query=Query+"  LEFT  join EFU_MAST.BRANCHES br on um.BRANCH_ID = br.BRANCH_ID ";
                Query=Query+"  LEFT  join efu_mast.city ct on br.CITY_ID=ct.CITY_ID";
           Query =Query+ "  LEFT  join EFU_MAST.COMMODITIES cm on um.COMMODITY_ID=cm.COMMODITY_ID";
          Query =Query+  " where ";  
    q1="select Unique  nvl(prem.NET_AMT,'0'), ((prem.NET_AMT+nvl(ANA.DEB,0))-nvl(ANA.cred,0)) AS PREMIUM_PAID, (select TRIM(nvl(mc.OPEN_NONOPEN,' '))  ";
                 if((Integer.parseInt(doctype)==1)) {
    q1=q1+ " from UW_MAIN um left join MARINE_CARGO mc on um.UW_DOC_ID=mc.uw_doc_id where um.POLICY_NO="+policyno+" and um.CLASS_ID=1 and um.POLICY_TYPE='P'";
                 }else if((Integer.parseInt(doctype)==2)) {q1=q1+ " from UW_MAIN um left join MARINE_CARGO mc on um.UW_DOC_ID=mc.uw_doc_id where  UM.CERTIFICATE_NO="+policyno+" and um.CLASS_ID=1 and um.POLICY_TYPE='C'";}
   q1=q1+  " and um.BRANCH_ID="+ branchid+") open_pol, nvl(umi.SUB_CLASS_TYPE_ID,0), nvl(ANA.DEB,0),nvl(ANA.cred,0), nvl((ANA.DEB-ANA.cred),0) D_C";
    q1=q1+" FROM (Select Unique pr.NET_AMT, um.uw_doc_id , sum (ABS(DECODE(vd.DRCR,2,vd.VCH_AMT) )) cred,";
    q1=q1+"sum(ABS(DECODE(vd.DRCR,1,vd.VCH_AMT) )) deb from UW_MAIN um ";
               q1=q1+"  LEFT  JOIN VIEW_RECOVERY_4_WEB";
              q1=q1+"   ON VIEW_RECOVERY_4_WEB.class_id       =um.class_id";
             q1=q1+"    AND VIEW_RECOVERY_4_WEB.policy_no     =um.policy_no";
             q1=q1+"    AND VIEW_RECOVERY_4_WEB.CERTIFICATE_NO=um.CERTIFICATE_NO";
              q1=q1+"   AND VIEW_RECOVERY_4_WEB.ENDT_NO       =um.ENDT_NO";
             q1=q1+"    AND VIEW_RECOVERY_4_WEB.policy_type   =um.policy_type";
             q1=q1+"    AND VIEW_RECOVERY_4_WEB.branch_id     =um.branch_id";
             q1=q1+"  LEFT  join VOUCHER_DETAIL vd  on um.UW_DOC_ID=vd.UW_DOC_ID ";
                 q1=q1+" LEFT  join PREM_SHARES pr on um.uw_doc_id=pr.UW_DOC_ID left join VOUCHER_MASTER vm on  vd.VCH_MAST_ID=vm.VCH_MAST_ID";
               //  q1=q1+" LEFT   JOIN marine_cargo mc ON um.uw_doc_id=mc.uw_doc_id";
                
    
              if(Integer.parseInt(doctype)==1) {
                Query =Query+" UM.POLICY_NO =?  and um.POLICY_TYPE in ('P') and";
                    Query =Query+" um.BRANCH_ID=? and um.CLASS_ID=? and ";
        q1=q1+" where um.POLICY_NO="+policyno+" and um.CLASS_ID="+classid+ " and ";
     q1=q1+" um.POLICY_TYPE='P'  and vm.VCH_TYPE NOT IN ('A') and  pr.COINS_BRANCH_ID='110000' AND SUBSTR(vd.gl_id,0,3)";
     q1=q1+" IN (349,427) and um.BRANCH_ID="+branchid+" ";
     //q1=q1+"and um.RISK_END_DATE>sysdate";
     q1=q1+"  group by um.uw_doc_id, pr.NET_AMT) ana ";
                        q1=q1+  "  full  join  UW_MAIN UMI ON ana.UW_DOC_ID=UMI.UW_DOC_ID ";
                     //   q1=q1+  "   left join EFU_MAST.SUB_CLASS_TYPE sct on umi.SUB_CLASS_TYPE_ID=sct.SUB_CLASS_TYPE_ID";
                      //wrong code  q1=q1+  "      and umi.COMP_ID=sct.SUB_CLASS_ID ";
                       
                        q1=q1+  "           left  join PREM_SHARES prem on umi.UW_DOC_ID=prem.UW_DOC_ID  left  join VOUCHER_DETAIL vd";
                        q1=q1+  "    on umi.UW_DOC_ID=vd.UW_DOC_ID";
                        q1=q1+  "   left  JOIN VIEW_RECOVERY_4_WEB   ON VIEW_RECOVERY_4_WEB.class_id       =umi.class_id   AND VIEW_RECOVERY_4_WEB.policy_no";
                        q1=q1+  "   =umi.policy_no   AND VIEW_RECOVERY_4_WEB.CERTIFICATE_NO=umi.CERTIFICATE_NO  AND VIEW_RECOVERY_4_WEB.ENDT_NO       =umi.ENDT_NO  AND VIEW_RECOVERY_4_WEB.policy_type";
                        q1=q1+  "    =umi.policy_type  AND VIEW_RECOVERY_4_WEB.branch_id     =umi.branch_id";
 
    q1=q1+" WHERE prem.COINS_BRANCH_ID='110000' AND SUBSTR(vd.gl_id,0,3) IN (349,427) and"; 
    q1=q1+" UMI.POLICY_NO="+policyno+" and UMI.CLASS_ID="+classid+" and UMI.POLICY_TYPE='P' and UMI.BRANCH_ID="+branchid+" and rownum=1";                
 //
 // q1=q1+"   AND umi.RISK_END_DATE>sysdate ";   
  
  }
           if (Integer.parseInt(doctype)==2)  {   Query=Query+" UM.CERTIFICATE_NO=? and um.POLICY_TYPE in ('C')   and";
          Query =Query+" um.BRANCH_ID=? and um.CLASS_ID=? and ";    
 q1=q1+" where UM.CERTIFICATE_NO="+policyno+" and um.CLASS_ID="+classid+ " and ";   
q1=q1+" um.POLICY_TYPE='C' and vm.VCH_TYPE NOT IN ('A') and  pr.COINS_BRANCH_ID='110000' AND SUBSTR(vd.gl_id,0,3) IN (349,427) and um.BRANCH_ID="+branchid+" and pr.NET_AMT>0  group by UM.uw_doc_id, pr.NET_AMT) ana ";
              q1=q1+  "  full  join  UW_MAIN UMI ON ana.UW_DOC_ID=UMI.UW_DOC_ID ";
         //     q1=q1+  "   left join EFU_MAST.SUB_CLASS_TYPE sct on umi.SUB_CLASS_TYPE_ID=sct.SUB_CLASS_TYPE_ID";
         //     q1=q1+  "      and umi.COMP_ID=sct.SUB_CLASS_ID ";
              q1=q1+  "           left  join PREM_SHARES prem on umi.UW_DOC_ID=prem.UW_DOC_ID  left  join VOUCHER_DETAIL vd";
              q1=q1+  "    on umi.UW_DOC_ID=vd.UW_DOC_ID";
              q1=q1+  "   left  JOIN VIEW_RECOVERY_4_WEB   ON VIEW_RECOVERY_4_WEB.class_id       =umi.class_id   AND VIEW_RECOVERY_4_WEB.policy_no";
              q1=q1+  "   =umi.policy_no   AND VIEW_RECOVERY_4_WEB.CERTIFICATE_NO=umi.CERTIFICATE_NO  AND VIEW_RECOVERY_4_WEB.ENDT_NO       =umi.ENDT_NO  AND VIEW_RECOVERY_4_WEB.policy_type";
              q1=q1+  "    =umi.policy_type  AND VIEW_RECOVERY_4_WEB.branch_id     =umi.branch_id";
              q1=q1+" WHERE prem.COINS_BRANCH_ID='110000' AND SUBSTR(vd.gl_id,0,3) IN (349,427) and"; 
 q1=q1+"  UMI.CERTIFICATE_NO="+policyno+" and UMI.CLASS_ID="+classid+" and UMI.POLICY_TYPE='C' and UMI.BRANCH_ID="+branchid+"and rownum=1";          
     //  q1=q1+" and  umi.RISK_END_DATE>sysdate  ";
       }
                if (Integer.parseInt(doctype)==4){ Query=Query+" um.ENDT_NO =? and um.POLICY_TYPE='E' and";}
        Query=Query+" TRIM(UM.CLIENT_ID)=?   and  rownum = 1 "; 
                 //  q1=q1+" and  um.RISK_END_DATE>sysdate  ";
        secondcheck=clientcode;
         
    
             } 
               // System.out.print(Query); 
                System.out.print(q1); 
           
                PreparedStatement ps1 = con.prepareStatement(q1);  
          PreparedStatement ps = con.prepareStatement(Query);  
               
            if (clientcode!=null&&clientcode.isEmpty()==false) 
            {
                if (Integer.parseInt(doctype)==1||Integer.parseInt(doctype)==2)   
            { ps.setString(1, policyno);
            ps.setString(2, branchid);ps.setString(3, classid);
        ps.setString(4, secondcheck);}
              
              else if (Integer.parseInt(doctype)==3)   
                { ps.setString(1, policyno);
            ps.setString(2, branchid);
        ps.setString(3, secondcheck);}
            } 
               // System.out.print(Query);     
       ps.executeQuery();
        rs= ps.getResultSet();
                jo = new JSONObject(); 
             try{  while (rs.next())  {                
                
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
//                      jo.put("Policy_No",rs.getString("Policy_No"));
//                               jo.put("Date_Of_Issuance",rs.getString("Date_Of_Issuance"));
//                        jo.put("From", rs.getString("From_"));
//                        jo.put("To", rs.getString("To_"));
//                        jo.put("Branch_Name",rs.getString("Branch_Name"));    
//                        jo.put("Insured",(rs.getString("Insured_")));
//                        jo.put("Postal_Address",rs.getString("Postal_Address"));                    
//                        jo.put("CC", rs.getString("CLIENT_CODE"));
//                        jo.put("city", rs.getString("CITY_NM"));
//                        jo.put("nic", rs.getString("CNIC_"));
//                        jo.put("gst", rs.getString("GST_"));
//                        jo.put("ntn", rs.getString("NTN_"));
//                        jo.put("commodity", rs.getString("Commodity_")); 
//                        jo.put("voyage", rs.getString("Voyage_From"));
//                        jo.put("shipto", rs.getString("ship_to")); 
//                        jo.put("carrier_mode", rs.getString("Carrier_Mode"));
//                        jo.put("Premium", rs.getString("Premium_"));                                                        
                       }}catch(Exception ex){          return Response.ok("{\" Main Query Error\": \""+ex.getCause()+ex.getClass()+ex.getLocalizedMessage()+"this is main resultset the policy no is : "+policyno+" The second check : "+secondcheck+ "\"}",
                             MediaType.APPLICATION_JSON).build();    }
              ps1.executeQuery();rs2= ps1.getResultSet();
               
            try{  while (rs2.next())  {
                  
                   //System.out.print("the rs dot next is running"); 
                   
                    
                    if (doctype.equals("1")){
                        newval= Float.parseFloat(rs2.getString(2));
                      type_id=Integer.parseInt(rs2.getString(4).toString());               
                   opencheck=rs2.getString(3).toString(); 
                        if (newval!=0){
            if (opencheck.equals("O"))
              
            {      
                //if (!rs2.getString(4).toString().equals(null)) {  
                if ((type_id==1012)||(type_id==1032))
             { truemaker="true";
                   // jom.put( rs2.getMetaData().getColumnName(2),"This is an Open Policy and premium would be charged on individual certificates issued at the time of each shipment."); 
                   jom.put( rs2.getMetaData().getColumnName(2),"As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");                      
                          
                                 }//end of class type 1012
                             
                     else  if (type_id==1013||type_id==1033||type_id==1011)
                  {
                    
           //         jom.put( rs2.getMetaData().getColumnName(2),"Premium is guaranteed and would be paid as per the agreed schedule.");
              jom.put( rs2.getMetaData().getColumnName(2),"As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");                                              
                                                 }// end of class type 1033
  
         else       { 
                    //jom.put( rs2.getMetaData().getColumnName(2),"no sub class type defined for this record.");
                    jom.put( rs2.getMetaData().getColumnName(2),"As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");   
                    }// end of everything else class type
                   }//open policy
            else  { 
              //  jom.put( rs2.getMetaData().getColumnName(2),"Premium is guaranteed and would be paid as per the agreed schedule.");
              jom.put( rs2.getMetaData().getColumnName(2),"As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk."); 
                } //non open po9licy                                              
                                                                                             }//not equal to zero
                    if (newval==0||newval<0)
                   {  
                        if (opencheck.equals("O"))
            {      if (type_id==1012||type_id==1032)
                             { jom.put( rs2.getMetaData().getColumnName(2),"Paid");        
                                 }  
                 else   if (type_id==1013||type_id==1033)
                                             { jom.put( rs2.getMetaData().getColumnName(2),"Paid");
                                                 }
                    else 
                              { jom.put( rs2.getMetaData().getColumnName(2),"Paid");
                                                             }
                }
          else  { jom.put( rs2.getMetaData().getColumnName(2),"Paid");} 
                     
                      }//equal to zero
                    }//end of policy no search
                  if (doctype.equals("2"))
                  {
                          newval= Integer.parseInt(rs2.getString(2));
                          type_id=Integer.parseInt(rs2.getString(4).toString());
                          opencheck=rs2.getString(3).toString();
                          if (newval!=0){
                                      if (opencheck.equals("O"))
                                        
                                      {                if (type_id==1012||type_id==1032)
                                      {               
                                                           //jom.put( rs2.getMetaData().getColumnName(2),"Premium is guaranteed and would be paid as per the agreed schedule.");
                                                               jom.put( rs2.getMetaData().getColumnName(2),"As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");
                                                          // 
                                                           }
//            if (type_id==1013||type_id==1033)
//                {
//                //jom.put( rs2.getMetaData().getColumnName(2),"Premium is guaranteed and would be paid as per the agreed schedule.");
//                   jom.put( rs2.getMetaData().getColumnName(2),"As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");                                                                              }
                                 
                                 else 
                                { 
               // jom.put( rs2.getMetaData().getColumnName(2),"Premium is guaranteed and would be paid as per the agreed schedule."); 
                    jom.put( rs2.getMetaData().getColumnName(2),"As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");
                } 
                                                       
//                                          if (rs2.getString(4).toString().equals("1013")||rs2.getString(4).toString().equals("1033"))
//                                                                       { jom.put( rs2.getMetaData().getColumnName(2),"This is an Open Policy and premium would be charged on individual certificates issued at the time of each shipment.");
//                                                                           
//                                                                           }
                                                                                                                       }//end of open policy
                                      else  { 
                                         // jom.put( rs2.getMetaData().getColumnName(2),"Premium is guaranteed and would be paid as per the agreed schedule.");
                                         jom.put( rs2.getMetaData().getColumnName(2),"As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");        
                                                                                                                       }//end of if not open
                                                                                                                       }//end of unpaid or not equal to zero
                                              if (newval==0||newval<0)
                                             {  
                                                  if (opencheck.equals("O"))
                                      {   if (rs2.getString(4).toString().equals("1012")||rs2.getString(4).toString().equals("1032"))
                                                       { jom.put( rs2.getMetaData().getColumnName(2),"Paid");
                                                           
                                                           }
                                                                          else 
                                                                                            { jom.put( rs2.getMetaData().getColumnName(2),"Paid");
                                                                                                                           }
                                                       
                                        //  if (rs2.getString(4).toString().equals("1013")||rs2.getString(4).toString().equals("1033"))
                                        //                               { jom.put( rs2.getMetaData().getColumnName(2),"Paid");}
                                                                           
                                                                              }
                                    else  { jom.put( rs2.getMetaData().getColumnName(2),"Paid");} 
                                               
                                                }
                      }
                    if (doctype.equals("3")){
                            if (newval!=0){
               // jom.put( rs2.getMetaData().getColumnName(2),"Premium is guaranteed and would be paid as per the agreed schedule");
               jom.put( rs2.getMetaData().getColumnName(2),"As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");
                                               //jom.put( rs2.getMetaData().getColumnName(1),rs2.getString(1).toString());
                                               }
                                                if (newval==0||newval<0)
                                               {  jom.put( rs2.getMetaData().getColumnName(2),"Paid");
                                                    //  jom.put( rs2.getMetaData().getColumnName(1),rs2.getString(1).toString());
                                                  } 
                        }
               } }
                catch(Exception ex){          return Response.ok("{\"premium resultset Error\": \""+ex.getCause()+ex.getClass()+q1 +Query+" this is premium resultset the policy no is : "+policyno+" The second check : "+secondcheck+ "\"}",
                                             MediaType.APPLICATION_JSON).build();    }
                   if (doctype.equals("3")){  PreparedStatement prex2 = con.prepareStatement(Querex);  prex2.executeQuery();rs3= prex2.getResultSet(); 
                       addtext=" Addendum(s) issued on this Covernote |";
                       int r=0; String voltar="";String count_add="";
                    try{while (rs3.next())  {
                            
                           voltar=rs3.getString(1);
                            count_add=rs3.getString(2);
                            r++;
                          //  addendum=Integer.parseInt(rs3.getString(1));
                            if(voltar!=null)
                            {
                                addtext=addtext+" Addendum No "+r+" issued on "+rs3.getString(1).toString()+"|";
                            }
                            
                            }
                        if (voltar==null){ addtext=addtext+" 0";}
//                        if(addendum!=0){
//                            if(addendum>1){
//                       // addtext=addendum +" times addendum issued on this covernote.";
//                       addtext=addendum +" Addendum(s) issued on this Covernote |";
//                        }
//                            else if (addendum==1){ addtext=addendum +"addendum issued on this covernote.";}
//                            
//                            
//                            }
//                            else { addtext="No addendum issued on this covernote.";}
                        
                        }
                catch(Exception ex){          return Response.ok("{\"addendum Error\": \""+ex.getCause()+ex.getClass()+ex.getLocalizedMessage()+Querex+" this is addendum resultset the policy no is : "+policyno+" The second check : "+secondcheck+ "\"}",
                                             MediaType.APPLICATION_JSON).build();    }}
                    
             if(!jom.containsKey("PREMIUM_PAID")){
                
                    if (doctype.equals("1")){ 
                        jom.put( "PREMIUM_PAID","As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");
                   
                    }
                    if (doctype.equals("2"))
                    {
                          jom.put( "PREMIUM_PAID","As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");
                                           
                      }
                    if (doctype.equals("3")){
                           
                    jom.put( "PREMIUM_PAID","As per rule 58 of insurance rules 2017, the premium must be paid within 30 days of the attachment of the risk.");
                                        
                        }
            
                } if (type_id==1011||type_id==1021||type_id==1031)
                  { jom.put( "opencheck","SV");}
                    else if 
                (type_id==1034)
                                  { jom.put( "opencheck","SL");}
                    else{ jom.put( "opencheck",opencheck);}
                if (doctype.equals("3")){
                                     jom.put( "addendum",addtext);
                    }  
             ja.put(jom);  o.put("Policy Schedule", ja);  
             
        con.close();                    
            } 
     catch (Exception e) {             
     
          return Response.ok("{\"encapsul Error\": \""+e.getCause()+e.getClass()+e.getLocalizedMessage()+Querex+" the policy no is : "+policyno+" The second check : "+secondcheck+ "\"}",
                             MediaType.APPLICATION_JSON).build();     
     }             
        return Response.status(ok).entity(""+o+"").build();  
           // return Response.status(ok).entity(""+o+"premium paid value: "+newval+"").build();

        }          
    }
}
