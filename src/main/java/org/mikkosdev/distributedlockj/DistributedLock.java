package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.database.AzureTableStorageClient;
import org.mikkosdev.distributedlockj.exception.DistributedLockInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

public class DistributedLock {

    protected final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    private String id;
    private String partitionKey;    // Required by Cosmos DB
    private String name;
    private DistributedLockCollection parentCollection;

    public DistributedLock(String name) {
        this.name = name;
        this.partitionKey = "/" + new DatabaseConfiguration().getPartitionKey();    // FIXME: This is an awful hack
        this.id = UUID.randomUUID().toString();
    }

    void setParent(DistributedLockCollection parentCollection) {
        this.parentCollection = parentCollection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public String getName() {
        return name;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    // The implementation for Closeable interface
    // Needed for try-with-resources syntax
    public void release() {
        logger.debug("In release()");
        parentCollection.release(this);
    }
}
