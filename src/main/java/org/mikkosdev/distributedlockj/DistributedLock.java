package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.exception.LockInUseException;

public class DistributedLock {

    private String name;

    public DistributedLock(String name) {
        this.name = name;
    }

    /**
     * Try to acquire this lock
     * @return
     */
    public void tryToAcquire() throws LockInUseException {

    }

    /**
     * Release the distributed lock
     */
    public void release() throws LockInUseException {

    }
}
