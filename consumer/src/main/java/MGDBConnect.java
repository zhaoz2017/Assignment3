import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import model.LiftRideEvent;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class MGDBConnect {
  private MongoDatabase database;
  private MongoCollection<Document> collection;

  public MGDBConnect() {
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
  }

  public void addLiftRides(LiftRideEvent liftRide) {
    MongoCollection<LiftRideEvent> collection = database.getCollection("liftRides", LiftRideEvent.class);
    collection.insertOne(liftRide);
    System.out.println("Lift ride record is added to the collection.");
  }
}
