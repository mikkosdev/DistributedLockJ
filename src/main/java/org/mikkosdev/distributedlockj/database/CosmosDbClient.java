package org.mikkosdev.distributedlockj.database;

import org.mikkosdev.distributedlockj.DistributedLock;

import java.util.List;

public class CosmosDbClient implements IDatabaseClient {
    @Override
    public void addDistributedLock(DistributedLock distributedLock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteDistributedLock(DistributedLock distributedLock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroyDistributedLockTable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllDistributedLocks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DistributedLock> getAllDistributedLocks() {
        throw new UnsupportedOperationException();
    }
}
