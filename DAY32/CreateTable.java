package DAY32;


import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

// Create table in DynamoDB
public class CreateTable {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, create table in DynamoDB");

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("5yvpgt", "1fhljf");

        DynamoDbClient client = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        String tableName = "Employees02";

        try {
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(KeySchemaElement.builder()
                            .attributeName("ID")
                            .keyType(KeyType.HASH)
                            .build())
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName("ID")
                            .attributeType(ScalarAttributeType.N)
                            .build())
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build();

            client.createTable(request);
            System.out.println(tableName + " table is created.");
        } catch (Exception ex) {
            // ResourceInUseException if table already exists
            System.out.println("Please choose a different table name as it already exists.");
        }

        client.close();
    }
}
