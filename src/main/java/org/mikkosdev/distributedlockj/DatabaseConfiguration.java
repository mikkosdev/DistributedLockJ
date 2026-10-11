package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.database.AzureTableStorageClient;
import org.mikkosdev.distributedlockj.database.IDatabaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfiguration {

    protected final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    public static final String TABLE_NAME = "distributedlock";
    public static final String PARTITION_KEY = "ABC";

    public enum DatabaseClientEnum {
        AZURE_COSMOS_DB, AZURE_TABLE_STORAGE
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
}
