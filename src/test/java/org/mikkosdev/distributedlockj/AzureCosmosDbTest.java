package org.mikkosdev.distributedlockj;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mikkosdev.distributedlockj.exception.DistributedLockInUseException;
import org.mikkosdev.distributedlockj.exception.DistributedLockNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class AzureCosmosDbTest extends AbstractDatabaseTest {

    private DatabaseConfiguration dbConfig;
    private DistributedLockCollection lockCollection;

    public AzureCosmosDbTest() {
        dbConfig = new DatabaseConfiguration();
        dbClient = dbConfig.getDatabaseClient(DatabaseConfiguration.DatabaseClientEnum.AZURE_COSMOS_DB);
    }

    @BeforeEach
    public void setup() {
        destroyDistributedLockTable();
        dbConfig = new DatabaseConfiguration();
        lockCollection = new DistributedLockCollection();
    }

    @AfterEach
    public void tearDown() {
        lockCollection = null;
    }

    @Test
    public void tryAcquiringLockSucceeds() {
        // Arrange
        // Act
        assertDoesNotThrow(() -> {
            DistributedLock dlock = lockCollection.tryAcquire("Hello");
        });

        // Assert
        var rowCount = getRowCount(dbConfig.getTableName());
        assertEquals(1, rowCount);
    }

    @Test
    public void tryAcquiringLockWithoutReleasingFails() {
        // Arrange
        // Act
        // Assert

        // Acquire lock once
        assertDoesNotThrow(() -> {
            DistributedLock dlock = lockCollection.tryAcquire("Hello");
        });
        // Acquire lock second time
        assertThrows(DistributedLockInUseException.class, () -> {
            DistributedLock dlock = lockCollection.tryAcquire("Hello");
        });

        var rowCount = getRowCount(dbConfig.getTableName());
        assertEquals(1, rowCount);
    }

    @Test
    public void testReleasingLockSucceeds() {
        DistributedLock dlock = lockCollection.tryAcquire("Hello");
        dlock.release();
    }

    // This test is disabled for now because it seems Azurite does not return error when you're deleting something that exists
    @Test
    @Disabled
    public void testReleasingLockTwiceThrows() {
        DistributedLock dlock = lockCollection.tryAcquire("Hello");

        // Release lock once
        dlock.release();

        // Release lock second time
        assertThrows(DistributedLockNotFoundException.class, () -> {
            dlock.release();
        });
    }

    @Test
    public void testDeletingAllLocks() {
        // Create a lock
        DistributedLock dlock = lockCollection.tryAcquire("Hello");
        var locks = lockCollection.getAll();
        assertEquals(1, locks.size());

        // Release (delete) all locks
        lockCollection.releaseAll();

        // Verify if any locks exist
        locks = lockCollection.getAll();
        assertEquals(0, locks.size());
    }
}
