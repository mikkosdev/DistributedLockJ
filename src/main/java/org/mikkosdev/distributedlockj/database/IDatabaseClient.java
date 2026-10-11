package org.mikkosdev.distributedlockj.database;

import org.mikkosdev.distributedlockj.DistributedLock;

import java.util.List;

public interface IDatabaseClient {

    public void add(DistributedLock distributedLock);
    public void destroyMigrationsTable();
    public List<DistributedLock> getAllDistributedLocks();
}
