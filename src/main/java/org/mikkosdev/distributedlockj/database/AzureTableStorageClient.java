package org.mikkosdev.distributedlockj.database;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.ListEntitiesOptions;
import com.azure.data.tables.models.TableEntity;
import org.mikkosdev.distributedlockj.DatabaseConfiguration;
import org.mikkosdev.distributedlockj.DistributedLock;

import java.util.ArrayList;
import java.util.List;

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
        tableClient = tableServiceClient.createTableIfNotExists(DatabaseConfiguration.TABLE_NAME);
    }

    private void getTableContents() {

    }

    public void addDistributedLock(DistributedLock distributedLock) {
        TableEntity entity = new TableEntity(DatabaseConfiguration.PARTITION_KEY, distributedLock.getName())
                .addProperty("Product", "Marker Set")
                .addProperty("Price", 5.00)
                .addProperty("Quantity", 21);

        tableClient.createEntity(entity);
    }

    @Override
    public void add(DistributedLock distributedLock) {
        throw new UnsupportedOperationException();
    }

    public void destroyMigrationsTable() {
        tableServiceClient.deleteTable(DatabaseConfiguration.TABLE_NAME);
    }

    public List<DistributedLock> getAllDistributedLocks() {
        List<String> propertiesToSelect = new ArrayList<>();
        propertiesToSelect.add("name");
//        propertiesToSelect.add("Price");

        ListEntitiesOptions options = new ListEntitiesOptions()
                .setFilter(String.format("PartitionKey eq '%s'", DatabaseConfiguration.PARTITION_KEY))
                .setSelect(propertiesToSelect);

        List<DistributedLock> migrations = new ArrayList<>();
        for (TableEntity entity : tableClient.listEntities(options, null, null)) {
//            Map<String, Object> properties = entity.getProperties();
            var name = (String) entity.getProperty("name");
            migrations.add(new DistributedLock(name));
//            System.out.printf("%s: %.2f%n", properties.get("Product"), properties.get("Price"));
        }
        return migrations;
    }

    // Pending impl.
    private DistributedLock mapTableEntityToMigration(TableEntity entity) {
        return null;
    }
}
