package policy_verify;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;



@Path("test_class")
public class test_class {
/* Json Format Return*/

    @GET
    @Path("/test")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData(  @QueryParam("login" )
        String var_uw_doc_id) throws Exception {
        int ok = 200;
                String Base_Query = "";
                //Base_Query ="select  CITY_ID,CITY_NM,PROVINCE_STATE_ID    from efu_mast.city where status='A'";                 
                Base_Query ="select  CITY_ID,CITY_NM,PROVINCE_STATE_ID    from efu_mast.city where status='A'and city_id in (1,16,19,20,49,63 )  and rownum=1";
                /*Base_Query=Base_Query+"union all ";
                Base_Query=Base_Query+"select  CITY_ID,CITY_NM,PROVINCE_STATE_ID    from (";
                Base_Query=Base_Query+"select  CITY_ID,CITY_NM,PROVINCE_STATE_ID    from efu_mast.city where status='A'and city_id in (select  CITY_ID  from efu_mast.workshops  where status='A' group by CITY_ID  )";
                Base_Query=Base_Query+"and not city_id in (1,16,19,20,49,63 )  order by CITY_NM";
                Base_Query=Base_Query+")";
                */                

        
                
        System.out.println(Base_Query);
  //      System.out.println(temp_city_id);

        JSONObject o = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        
       
       try {
            boolean status = false;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con =
                DriverManager.getConnection("jdbc:oracle:thin:@test.efuinsurance.com:1521:test",
                                            "efu_gis", "test");
            PreparedStatement ps = con.prepareStatement(Base_Query);
            ResultSet rs = ps.executeQuery();
            System.out.println("sucessful");
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("CITY_ID", rs.getInt("CITY_ID"));
                obj.put("CITY_NM", rs.getString("CITY_NM"));
                obj.put("PROVINCE_STATE_ID", rs.getString("PROVINCE_STATE_ID"));
                jsonArray.put(obj);
            }

        } catch (Exception e) {
            return Response.ok("{\"Response\": \"Zero Value   " + "\"}",MediaType.APPLICATION_JSON).build();
            //e.printStackTrace();
        }
        o.put("list", jsonArray);
        //return Response.status(ok).entity(jsonArray.toString()).build();
//        return Response.status(ok).entity(o.toString()).build();  //Return Object
        //return Response.status(ok).entity(jsonArray.toString()).build();
        //return Response.ok(o.toString(),MediaType.APPLICATION_JSON).build();  //Return Object      
        return Response.ok("{\"id\":9999,\"content\":\"sarfaraz!\"}",MediaType.APPLICATION_JSON).build();
      
    }


}


