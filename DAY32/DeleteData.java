package DAY32;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DeleteData{
    public static void main(String[] args) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("5yvpgt", "1fhljf");

        DynamoDbClient client = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        String tableName = "Employees02";

        // Key for the item to delete (ID = 1003)
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("ID", AttributeValue.builder().n("1003").build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        client.deleteItem(request);
        System.out.println("Item with ID=1003 deleted successfully.");

        client.close();
    }
}
