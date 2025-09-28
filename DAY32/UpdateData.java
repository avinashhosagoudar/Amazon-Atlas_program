package DAY32;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class UpdateData {
    public static void main(String[] args) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("5yvpgt", "1fhljf");


        DynamoDbClient client = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000")) // DynamoDB Local
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        String tableName = "Employees02";


        Map<String, AttributeValue> key = new HashMap<>();
        key.put("ID", AttributeValue.builder().n("1002").build());


        String updateExpr = "SET Address = :newAddress";


        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":newAddress", AttributeValue.builder().s("Singapore").build());


        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .updateExpression(updateExpr)
                .expressionAttributeValues(expressionValues)
                .returnValues("ALL_NEW") // returns updated item
                .build();


        UpdateItemResponse response = client.updateItem(request);
        System.out.println("Item updated successfully. New values:");
        System.out.println(response.attributes());

        client.close();
    }
}

