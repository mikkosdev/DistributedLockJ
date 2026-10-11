package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.exception.DistributedLockInUseException;

public class DistributedLock {

    private String name;

    public DistributedLock(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    /**
     * Try to acquire this lock
     * @return
     */
    public void tryToAcquire() throws DistributedLockInUseException {

    }

    /**
     * Release the distributed lock
     */
    public void release() throws DistributedLockInUseException {

    }
}
