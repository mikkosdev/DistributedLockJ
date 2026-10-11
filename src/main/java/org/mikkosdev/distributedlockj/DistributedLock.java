package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.database.AzureTableStorageClient;
import org.mikkosdev.distributedlockj.exception.DistributedLockInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

public class DistributedLock {

    protected final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    private String name;
    private DistributedLockCollection parentCollection;

    public DistributedLock(String name) {
        this.name = name;
    }

    void setParent(DistributedLockCollection parentCollection) {
        this.parentCollection = parentCollection;
    }

    public String getName() {
        return name;
    }

    // The implementation for Closeable interface
    // Needed for try-with-resources syntax
    public void release() {
        logger.debug("In release()");
        parentCollection.release(this);
    }
}
