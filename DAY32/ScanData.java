package DAY32;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class ScanData {
    public static void main(String[] args) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("5yvpgt", "1fhljf");

        DynamoDbClient client = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        String tableName = "Employees02"; // change if different

        try {

            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(tableName)
                    .build();


            ScanResponse response = client.scan(scanRequest);

            List<Map<String, AttributeValue>> items = response.items();
            if (items.isEmpty()) {
                System.out.println("No items found in table: " + tableName);
            } else {
                System.out.println("Items in table " + tableName + ":");
                for (Map<String, AttributeValue> item : items) {
                    item.forEach((key, value) -> System.out.print(key + ": " + value.toString() + " | "));
                    System.out.println();
                }
            }

        } catch (DynamoDbException e) {
            System.err.println("Error scanning table: " + e.getMessage());
        } finally {
            client.close();
        }

    }

}
