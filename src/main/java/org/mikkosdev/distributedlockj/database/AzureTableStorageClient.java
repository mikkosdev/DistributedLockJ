package org.mikkosdev.distributedlockj.database;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.ListEntitiesOptions;
import com.azure.data.tables.models.TableEntity;
import org.mikkosdev.distributedlockj.Migration;
import org.mikkosdev.distributedlockj.StaticValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Code adapted from: https://learn.microsoft.com/en-us/java/api/overview/azure/data-tables-readme?view=azure-java-stable
 */
public class AzureTableStorageClient implements IDatabaseClient {

    TableServiceClient tableServiceClient;
    TableClient tableClient;

    public AzureTableStorageClient() {
        TableServiceClient tableServiceClient = new TableServiceClientBuilder()
                .connectionString("localhost:10002")    // TODO: magic number
                .buildClient();

        initialize();
    }

    private void initialize() {
        tableClient = tableServiceClient.createTableIfNotExists(StaticValues.TABLE_NAME);
    }

    private void getTableContents() {

    }

    public void addTableEntity(Migration migration) {
        TableEntity entity = new TableEntity(StaticValues.PARTITION_KEY, migration.getName())
                .addProperty("Product", "Marker Set")
                .addProperty("Price", 5.00)
                .addProperty("Quantity", 21);

        tableClient.createEntity(entity);
    }

    public void destroy() {
        tableServiceClient.deleteTable(StaticValues.TABLE_NAME);
    }

    public void getAllTableEntities() {
        List<String> propertiesToSelect = new ArrayList<>();
        propertiesToSelect.add("Product");
        propertiesToSelect.add("Price");

        ListEntitiesOptions options = new ListEntitiesOptions()
                .setFilter(String.format("PartitionKey eq '%s'", StaticValues.PARTITION_KEY))
                .setSelect(propertiesToSelect);

        for (TableEntity entity : tableClient.listEntities(options, null, null)) {
            Map<String, Object> properties = entity.getProperties();
            System.out.printf("%s: %.2f%n", properties.get("Product"), properties.get("Price"));
        }
    }
}
