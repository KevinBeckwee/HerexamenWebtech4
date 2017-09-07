package vraag1WT4;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import redis.clients.jedis.Jedis;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.bson.Document;
@Path("/movies")
public class MovieResource {
    public MovieResource() {
    }

    @GET
    @Path("{title}")
    @Produces({"text/html"})
    public String searchMovie(@PathParam("title") String title) {
            Jedis jedis = new Jedis("localhost");
            List<String> moviesList = jedis.lrange("movies", 0 , jedis.llen("movies"));
            

            Boolean notInDB = true;
            String returnJSON = "";
            Iterator var10 = moviesList.iterator();

            JsonObject object;
            while(var10.hasNext()) {
                Document doc = (Document)var10.next();
                if (doc.get("title").toString().equals(title)) {
                    JsonObjectBuilder builder = Json.createObjectBuilder();
                    builder.add("title", doc.get("title").toString());
                    builder.add("year", doc.get("year").toString());
                    System.out.println("gevonden in db!");
                    object = builder.build();
                    notInDB = false;
                    returnJSON = object.toString();
                    break;
                }
            }

            if (notInDB.booleanValue()) {
                Response response = ClientBuilder.newClient().target("http://echo.jsontest.com/movie/" + title + "/year/2016").request(new String[]{"application/json"}).get();
                String jsonString = response.readEntity(String.class);
                JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
                object = jsonReader.readObject();
                jsonReader.close();
                Document movie = new Document();
                movie.append("title", object.getString("movie"));
                movie.append("year", object.getString("year"));
                jedis.append("movies",movie.toString());
                jedis.close();
                returnJSON = object.toString();
                System.out.println("opgehaald en opgeslagen");
            }

            return returnJSON;
        }
}
