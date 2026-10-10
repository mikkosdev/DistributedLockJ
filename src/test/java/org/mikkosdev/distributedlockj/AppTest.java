package org.mikkosdev.distributedlockj;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private DistributedLockCollection lockCollection;

    @BeforeEach
    public void setup() {
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
        // Assert

        assertDoesNotThrow(() -> {
            DistributedLock dlock = lockCollection.tryAcquire("Hello");
        });
    }
}
