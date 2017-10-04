package sg.edu.nus.cs3205.subsystem3.api.session;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

@Produces(MediaType.APPLICATION_JSON)
public class Upload implements Session {
    @GET
    @Override
    public String get() {
        return "\"You sent a GET request\"";
    }

    @Path("/upload/{type}/{timestamp}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response upload(@PathParam("type")String type, @PathParam("timestamp")long timestamp, final InputStream is){
      // Verify jWTToken
      // Obtain userID from the token

      Response response null;
      // just for a test
      int userID = 1;
      InputStream stream = null;
      if(type.equalsIgnoreCase("heart")){
        response = uploadToHeart();
      }else if(type.equalsIgnoreCase("image")){
        response = uploadToImage(userID, is, timestamp);
      }else if(type.equalsIgnoreCase("video")){
        response = uploadToImage(userID, is, timestamp);
      }else if(type.equalsIgnoreCase("step")){
        response = uploadToImage(userID, is, timestamp);
      }else{
        response = Response.status(401).entity("unknown request made.").build();
      }

      return response;
    }

    private Response uploadToStep(int userID, InputStream stream, long timestamp){
      Client client = ClientBuilder.newClient();
      Invocation.Builder builder = client.target("http://cs3205-4-i.comp.nus.edu.sg/api/team3/steps/"+userID+"/upload/"+timestamp).request();
      // @TODO: Add in the headers for server 4 verification in the future
      Response response = builder.post(Entity.entity(stream, "application/json"));
      return response;
    }

    private Response uploadToImage(int userID, InputStream stream, long timestamp){
      Client client = ClientBuilder.newClient();
      Invocation.Builder builder = client.target("http://cs3205-4-i.comp.nus.edu.sg/api/team3/image/"+userID+"/upload/"+timestamp).request();
      // @TODO: Add in the headers for server 4 verification in the future
      Response response = builder.post(Entity.entity(stream, "image/jpeg"));
      return response;
    }

    private Response uploadToVideo(int userID, InputStream stream, long timestamp){
      Client client = ClientBuilder.newClient();
      Invocation.Builder builder = client.target("http://cs3205-4-i.comp.nus.edu.sg/api/team3/video/"+userID+"/upload/"+timestamp).request();
      // @TODO: Add in the headers for server 4 verification in the future
      Response response = builder.post(Entity.entity(stream, "video/mpeg"));
      return response;
    }

    private Response uploadToHeart(int userID, int heartrate, long timestamp){
      Client client = ClientBuilder.newClient();
      Invocation.Builder builder = client.target("http://cs3205-4-i.comp.nus.edu.sg/api/team3/heartservice/"+userID+"/"+heartrate+"/"+timestamp).request();
      // @TODO: Add in the headers for server 4 verification in the future
      Response response = builder.post();
      return response;
    }

    private boolean verifyJWTToken(String jWTToken){

    }
}
