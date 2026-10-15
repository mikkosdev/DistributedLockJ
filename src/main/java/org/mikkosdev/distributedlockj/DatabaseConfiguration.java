package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.database.AzureTableStorageClient;
import org.mikkosdev.distributedlockj.database.IDatabaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DatabaseConfiguration {

    protected final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private String tableName = "distributedlock";
    private String partitionKey = "ABC";

    // This is needed for operations where something is done to all locks
    private String masterLockName = "masterlock-" + UUID.randomUUID();

    public enum DatabaseClientEnum {
        AZURE_COSMOS_DB, AZURE_TABLE_STORAGE
    }

    public DatabaseConfiguration() {
        setFromEnvironmentVariables();
    }

    // ------------------------------ Public methods ------------------------------

    public String getTableName() {
        return tableName;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public String getMasterLockName() {
        return masterLockName;
    }

    public IDatabaseClient getDatabaseClient(DatabaseClientEnum client) {
        switch (client) {
            case AZURE_COSMOS_DB:
                throw new UnsupportedOperationException("Azure Cosmos DB support not implemented yet!");
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
        var envTableName = System.getenv("DISTRIBUTEDLOCKJ_TABLE_NAME");
        if (envTableName != null) tableName = envTableName;

        var envPartitionKey = System.getenv("DISTRIBUTEDLOCKJ_PARTITION_KEY");
        if (envPartitionKey != null) partitionKey = envPartitionKey;

        var envMasterLockName = System.getenv("DISTRIBUTEDLOCKJ_MASTER_LOCK_NAME");
        if (envMasterLockName != null) masterLockName = envMasterLockName;
    }
}
