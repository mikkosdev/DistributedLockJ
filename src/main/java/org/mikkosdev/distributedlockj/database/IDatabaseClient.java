package org.mikkosdev.distributedlockj.database;

import org.mikkosdev.distributedlockj.DistributedLock;

import java.util.List;

public interface IDatabaseClient {

    public void addDistributedLock(DistributedLock distributedLock);
    public void deleteDistributedLock(DistributedLock distributedLock);
    public void destroyDistributedLockTable();
    public void deleteAllDistributedLocks();
    public List<DistributedLock> getAllDistributedLocks();
}
