package org.mikkosdev.distributedlockj.exception;

public class DistributedLockNotFoundException extends RuntimeException {

    public DistributedLockNotFoundException() {
        super();
    }

    public DistributedLockNotFoundException(String message) {
        super(message);
    }
}
