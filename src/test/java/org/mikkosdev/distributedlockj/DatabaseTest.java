package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.database.IDatabaseClient;

/**
 * This super-class has utility methods that help to test database functionality.
 */
public class DatabaseTest {

    protected IDatabaseClient dbClient;

    protected DatabaseTest() {
        var dbConfig = new DatabaseConfiguration();
        dbClient = dbConfig.getDatabaseClient(DatabaseConfiguration.DatabaseClientEnum.AZURE_TABLE_STORAGE);
    }

    // Add row to database

    // Drop table

    // Count rows in table
    // TODO: Create actual count() method for better performance
    protected int getRowCount(String tableName) {
        var distributedLocks = dbClient.getAllDistributedLocks();
        return distributedLocks.size();
    }

    // Get row in table
}
