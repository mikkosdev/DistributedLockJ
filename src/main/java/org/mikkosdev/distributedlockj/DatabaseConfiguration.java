package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.database.AzureTableStorageClient;
import org.mikkosdev.distributedlockj.database.AzureCosmosDbClient;
import org.mikkosdev.distributedlockj.database.IDatabaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DatabaseConfiguration {

    protected final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private String databaseName = "distributedlockdb";
    private String tableName = "distributedlock";
    private String partitionKey = "/ABC";

    // This is needed for operations where something is done to all locks
    private String superLockName = "superlock-" + UUID.randomUUID();

    // Table Storage
    private String tableStorageConnStr;
    private String cosmosDbAccount;
    private String cosmosDbKey;

    public enum DatabaseClientEnum {
        AZURE_COSMOS_DB, AZURE_TABLE_STORAGE
    }

    // Constructor
    public DatabaseConfiguration() {
        setFromEnvironmentVariables();
    }

    // ------------------------------ Public methods ------------------------------

    public String getDatabaseName() {
        return databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public String getSuperLockName() {
        return superLockName;
    }

    public String getTableStorageConnStr() {
        return tableStorageConnStr;
    }

    public void setTableStorageConnStr(String tableStorageConnStr) {
        this.tableStorageConnStr = tableStorageConnStr;
    }

    public String getCosmosDbAccount() {
        return cosmosDbAccount;
    }

    public void setCosmosDbAccount(String cosmosDbAccount) {
        this.cosmosDbAccount = cosmosDbAccount;
    }

    public String getCosmosDbKey() {
        return cosmosDbKey;
    }

    public void setCosmosDbKey(String cosmosDbKey) {
        this.cosmosDbKey = cosmosDbKey;
    }

    public IDatabaseClient getDatabaseClient(DatabaseClientEnum client) {
        switch (client) {
            case AZURE_COSMOS_DB:
                return new AzureCosmosDbClient();
            case AZURE_TABLE_STORAGE:
                return new AzureTableStorageClient();
        }
        throw new UnsupportedOperationException("No client found!");
    }

    // ------------------------------ Private methods ------------------------------

    /**
     * Initialize configuration from environment variables or use the default value if not set.
     */
    private void setFromEnvironmentVariables() {
        // General configuration parameters
        var envDbName = System.getenv("DISTRIBUTEDLOCKJ_DATABASE_NAME");
        if (envDbName != null) databaseName = envDbName;

        var envTableName = System.getenv("DISTRIBUTEDLOCKJ_TABLE_NAME");
        if (envTableName != null) tableName = envTableName;

        var envPartitionKey = System.getenv("DISTRIBUTEDLOCKJ_PARTITION_KEY");
        if (envPartitionKey != null) partitionKey = envPartitionKey;

        var envSuperLockName = System.getenv("DISTRIBUTEDLOCKJ_SUPER_LOCK_NAME");
        if (envSuperLockName != null) superLockName = envSuperLockName;

        // Azure Table Storage related
        var envTableStorageConnStr = System.getenv("DISTRIBUTEDLOCKJ_AZURETABLESTORAGE_CONNECTION_STRING");
        if (envTableStorageConnStr != null) tableStorageConnStr = envTableStorageConnStr;

        // Azure Cosmos DB related
        var envCosmosDbAccount = System.getenv("DISTRIBUTEDLOCKJ_AZURECOSMOSDB_ACCOUNT");
        if (envCosmosDbAccount != null) cosmosDbAccount = envCosmosDbAccount;

        var envCosmosDbKey = System.getenv("DISTRIBUTEDLOCKJ_AZURECOSMOSDB_KEY");
        if (envCosmosDbKey != null) cosmosDbKey = envCosmosDbKey;
    }
}
