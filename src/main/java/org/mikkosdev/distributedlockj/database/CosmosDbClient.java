package org.mikkosdev.distributedlockj.database;

import org.mikkosdev.distributedlockj.DistributedLock;

import java.util.List;

public class CosmosDbClient implements IDatabaseClient {
    @Override
    public void add(DistributedLock distributedLock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroyMigrationsTable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DistributedLock> getAllDistributedLocks() {
        throw new UnsupportedOperationException();
    }
}
