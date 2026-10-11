package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.database.IDatabaseClient;

import java.util.ArrayList;
import java.util.List;

public class DistributedLockCollection {

    private List<DistributedLock> locks = new ArrayList<>();
    private IDatabaseClient dbClient;

    public DistributedLockCollection() {
        var dbConfig = new DatabaseConfiguration();
        dbClient = dbConfig.getDatabaseClient(DatabaseConfiguration.DatabaseClientEnum.AZURE_TABLE_STORAGE);
    }

    public DistributedLock tryAcquire(String name) {
        var dLock = new DistributedLock(name);
        dbClient.addDistributedLock(dLock);

        return dLock;
    }
}
