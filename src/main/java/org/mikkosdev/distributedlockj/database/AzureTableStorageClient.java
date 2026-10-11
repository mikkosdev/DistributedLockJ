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

    private static String localDevelopmentInstance =
            """
                    DefaultEndpointsProtocol=http;
                    AccountName=devstoreaccount1;
                    AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;
                    BlobEndpoint=http://127.0.0.1:10000/devstoreaccount1;
                    QueueEndpoint=http://127.0.0.1:10001/devstoreaccount1;
                    TableEndpoint=http://127.0.0.1:10002/devstoreaccount1;""";

    public AzureTableStorageClient() {
        tableServiceClient = new TableServiceClientBuilder()
                .connectionString(localDevelopmentInstance)    // TODO: magic number
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
                .addProperty("name", distributedLock.getName());
//                .addProperty("Price", 5.00)
//                .addProperty("Quantity", 21);

        tableClient.createEntity(entity);
    }

    @Override
    public void destroyDistributedLockTable() {
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
