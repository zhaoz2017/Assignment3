import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MGDBQuery {
  private static MongoDatabase database;
  private static MongoCollection<Document> collection;
  public static void main(String[] args) {
    String uri = "mongodb+srv://zilongz0904:OcfQsCIflfUnujb1@cluster0.nor4qgp.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

    // Construct a ServerApi instance using the ServerApi.builder() method
    ServerApi serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build();

    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString(uri))
        .serverApi(serverApi)
        .build();

    // Create a new client and connect to the server
    try (MongoClient mongoClient = MongoClients.create(settings)) {
      database = mongoClient.getDatabase("skiers");
      collection = database.getCollection("liftRides");
      try {
        // Send a ping to confirm a successful connection
        Bson command = new BsonDocument("ping", new BsonInt64(1));
        Document commandResult = database.runCommand(command);
        System.out.println("Successfully connected to Atlas");
      } catch (MongoException e) {
        System.err.println(e);
      }
    }

    getDays("20505", "2024");
    getVertical("20505", "2024", "1");
    getLifts("20505", "2024", "1");
    getNumberOfSkiers("10", "2024", "1");
  }

  private static List<String> getDays(String skierId, String seasonId) {

    List<Document> docs = collection
        .find(and(eq("skierId", Integer.parseInt(skierId)), eq("seasonId", seasonId)))
        .into(new ArrayList<>());
    List<String> days = new ArrayList<>();
    for(Document d : docs) {
      days.add(d.getString("dayId"));
    }
    return days;
  }

  private static int getVertical(String skierId, String seasonId, String dayId) {
    List<Document> docs = collection
        .find(and(eq("skierId", Integer.parseInt(skierId)), eq("seasonId", seasonId), eq("dayId", dayId)))
        .into(new ArrayList<>());

    int vertical = 0;
    for(Document d : docs) {
      vertical += d.getInteger("liftId") * 10;
    }
    return vertical;
  }

  private static List<Integer> getLifts(String skierId, String seasonId, String dayId) {
    List<Document> docs = collection
        .find(and(eq("skierId", Integer.parseInt(skierId)), eq("seasonId", seasonId), eq("dayId", dayId)))
        .into(new ArrayList<>());

    List<Integer> lifts = new ArrayList<>();

    for(Document d : docs) {
      lifts.add(d.getInteger("liftId"));
    }

    return lifts;
  }

  private static List<Integer> getNumberOfSkiers(String resortId, String seasonId, String dayId) {
    List<Document> docs = collection
        .find(and(eq("resortId", Integer.parseInt(resortId)), eq("seasonId", seasonId), eq("dayId", dayId)))
        .into(new ArrayList<>());
    List<Integer> skiers = new ArrayList<>();

    for (Document d : docs) {
      skiers.add(d.getInteger("skierId"));
    }
    return skiers;
  }
}
