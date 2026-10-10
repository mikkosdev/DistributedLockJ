package org.mikkosdev.distributedlockj.database;

import org.mikkosdev.distributedlockj.Migration;

public interface IDatabaseClient {

    public void addTableEntity(Migration migration);
    public void destroy();
    public void getAllTableEntities();
}
