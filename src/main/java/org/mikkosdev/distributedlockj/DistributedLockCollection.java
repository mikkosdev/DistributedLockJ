package org.mikkosdev.distributedlockj;

import org.mikkosdev.distributedlockj.database.IDatabaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DistributedLockCollection {

    protected final Logger logger = LoggerFactory.getLogger(DistributedLockCollection.class);

    private DatabaseConfiguration dbConfig;
    private IDatabaseClient dbClient;
    private List<DistributedLock> locks = new ArrayList<>();
    private DistributedLock localSuperLock;

    public DistributedLockCollection() {
        dbConfig = new DatabaseConfiguration();
        dbClient = dbConfig.getDatabaseClient(DatabaseConfiguration.DatabaseClientEnum.AZURE_COSMOS_DB);

        // SSL certificate
        var env = System.getenv("ENVIRONMENT");
        if (env == null || env.equalsIgnoreCase("development")) {
            logger.debug("Development environment");
            System.setProperty("javax.net.ssl.trustStore", "NUL");
            System.setProperty("javax.net.ssl.trustStoreType", "Windows-ROOT");
        }
    }

    public DistributedLock tryAcquire(String name) {
        var dLock = new DistributedLock(name);
        dLock.setParent(this);

        dbClient.addDistributedLock(dLock);

        return dLock;
    }

    public void release(DistributedLock distributedLock) {
        logger.debug("DistributedLockCollection.release()");

        dbClient.deleteDistributedLock(distributedLock);
    }

    public void releaseAll() {
        logger.debug("Deleting all distributed locks");

        var lock = tryAcquire(dbConfig.getSuperLockName());
        dbClient.deleteAllDistributedLocks();
        release(lock);
    }

    public List<DistributedLock> getAll() {
        logger.debug("Getting all distributed locks");

        var locks = dbClient.getAllDistributedLocks();
        return locks;
    }

    // ------------------------------ Convenience methods ------------------------------

    public DistributedLock tryAcquireSuperLock() {
        localSuperLock = tryAcquire(dbConfig.getSuperLockName());
        return localSuperLock;
    }

    public void releaseSuperLock() {
        release(localSuperLock);
    }
}
