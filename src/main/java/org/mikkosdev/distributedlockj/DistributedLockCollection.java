package org.mikkosdev.distributedlockj;

import java.util.ArrayList;
import java.util.List;

public class DistributedLockCollection {

    private List<DistributedLock> locks = new ArrayList<>();

    public DistributedLock tryAcquire(String hello) {
        return null;
    }
}
