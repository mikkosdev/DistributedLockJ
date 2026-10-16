package org.mikkosdev.distributedlockj.database;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.PartitionKey;
import org.mikkosdev.distributedlockj.DatabaseConfiguration;
import org.mikkosdev.distributedlockj.DistributedLock;

import java.util.List;

public class AzureCosmosDbClient implements IDatabaseClient {

    protected DatabaseConfiguration dbConfig;
    private CosmosClient cosmosDbClient;

    private String cosmosDbAccount = "https://localhost:8081/";
    private String cosmosDbKey = "C2y6yDjf5/R+ob0N8A7Cgv30VRDJIWEHLM+4QDU5DE2nQ9nDuVTqobD4b8mGGyPMbIZnqyMsEcaGQy67XIw/Jw==";
    private CosmosDatabase cosmosDb;
    private CosmosContainer container;

    public AzureCosmosDbClient() {
        dbConfig = new DatabaseConfiguration();
        cosmosDbAccount = dbConfig.getCosmosDbAccount();
        cosmosDbKey = dbConfig.getCosmosDbKey();

        // Create database client
        cosmosDbClient = new CosmosClientBuilder()
                .endpoint(cosmosDbAccount)
                .key(cosmosDbKey)
                .buildClient();

        initialize();
    }

    private void initialize() {
        // Create database
        cosmosDbClient.createDatabaseIfNotExists(dbConfig.getDatabaseName());
        cosmosDb = cosmosDbClient.getDatabase(dbConfig.getDatabaseName());

        // Create container
        cosmosDb.createContainerIfNotExists(dbConfig.getTableName(), dbConfig.getPartitionKey());
        container = cosmosDb.getContainer(dbConfig.getTableName());
    }

    @Override
    public void addDistributedLock(DistributedLock distributedLock) {
        container.createItem(distributedLock);
    }

    @Override
    public void deleteDistributedLock(DistributedLock distributedLock) {
        container.deleteItem(distributedLock.getName(), null);
    }

    @Override
    public void destroyDistributedLockTable() {
        container.delete();
    }

    @Override
    public void deleteAllDistributedLocks() {
        var partitionKey = new PartitionKey(dbConfig.getPartitionKey());
        container.deleteAllItemsByPartitionKey(partitionKey, null);
    }

    @Override
    public List<DistributedLock> getAllDistributedLocks() {
        var partitionKey = new PartitionKey(dbConfig.getPartitionKey());
        List<DistributedLock> distributedLocks = container.readAllItems(partitionKey, DistributedLock.class)
                .stream()
                .toList();
        return distributedLocks;
    }
}
