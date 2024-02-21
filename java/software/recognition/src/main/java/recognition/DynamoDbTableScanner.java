package recognition;

import java.util.List;
import java.util.stream.Collectors;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public final class DynamoDbTableScanner {

    // 1.) Function to list all items from a DynamoDB table
    public static List<List<String>> listItems(String tableName) {
        // <<Amazon CodeWhisperer generated code goes here>>
        DynamoDbClient dynamoDbClient = DynamoDbClient.create();
        List<List<String>> items = dynamoDbClient.scanPaginator(scanRequest -> scanRequest.tableName(tableName))
                .items()
                .stream()
                .map(item -> item.values().stream().map(Object::toString).collect(Collectors.toList()))
                .collect(Collectors.toList());

        return items; // <<Amazon CodeWhisperer generated code goes here>>
    }

    
}
