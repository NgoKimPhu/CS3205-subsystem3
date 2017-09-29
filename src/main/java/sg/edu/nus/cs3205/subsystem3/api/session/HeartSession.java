package sg.edu.nus.cs3205.subsystem3.api.session;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import sg.edu.nus.cs3205.subsystem3.dao.DBQueryParser;

import java.sql.ResultSet;

@Produces(MediaType.APPLICATION_JSON)
public class HeartSession implements Session {
    @GET
    @Override
    public String get() {
      try{
        ResultSet rs = DBQueryParser.query("user",
        null,
        new String[]{"NFCID LIKE ?"},
        new Object[]{"%3%"});
        // ResultSet rs = DBQueryParser.query("USER", new String[]{"NFCID"}, new String[]{"NFCID LIKE ?"}, new Object[]{"%3%"});
        String username = "";
        while(rs.next()){
          System.out.println(rs.getString("NFCID"));
          System.out.println(rs.getString("username"));
          username = rs.getString("username");
        }
        return username;
      }catch(Exception e){
        System.out.println(e);
        e.printStackTrace();
      }
        return "\"You sent a GET request\"";
    }

    @POST
    @Override
    public String upload(String body) {
        return "\"You sent " + body + '"';
    }
}
