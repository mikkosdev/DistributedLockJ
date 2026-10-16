package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.database.IDatabaseClient;

/**
 * This super-class has utility methods that help to test database functionality.
 */
public abstract class AbstractDatabaseTest {

    protected IDatabaseClient dbClient;

    protected AbstractDatabaseTest() {
        var dbConfig = new DatabaseConfiguration();
    }

    // Add row to database

    // Drop table
    protected void destroyDistributedLockTable() {
        dbClient.destroyDistributedLockTable();
    }

    // Count rows in table
    // TODO: Create actual count() method for better performance
    protected int getRowCount(String tableName) {
        var distributedLocks = dbClient.getAllDistributedLocks();
        return distributedLocks.size();
    }

    // Get row in table
}
