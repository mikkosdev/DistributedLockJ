package org.mikkosdev.distributedlockj.database;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.ListEntitiesOptions;
import com.azure.data.tables.models.TableEntity;
import com.azure.data.tables.models.TableErrorCode;
import com.azure.data.tables.models.TableServiceException;
import org.mikkosdev.distributedlockj.DatabaseConfiguration;
import org.mikkosdev.distributedlockj.DistributedLock;
import org.mikkosdev.distributedlockj.exception.DistributedLockInUseException;
import org.mikkosdev.distributedlockj.exception.DistributedLockNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Code adapted from: https://learn.microsoft.com/en-us/java/api/overview/azure/data-tables-readme?view=azure-java-stable
 */
public class AzureTableStorageClient implements IDatabaseClient {

    protected final Logger logger = LoggerFactory.getLogger(AzureTableStorageClient.class);

    protected DatabaseConfiguration dbConfig;
    protected TableServiceClient tableServiceClient;
    protected TableClient tableClient;

    private static String tableStorageConnStr;

    public AzureTableStorageClient() {
        dbConfig = new DatabaseConfiguration();
        tableStorageConnStr = dbConfig.getTableStorageConnStr();

        // Create database client
        tableServiceClient = new TableServiceClientBuilder()
                .connectionString(tableStorageConnStr)    // TODO: magic number
                .buildClient();

        initialize();
    }

    private void initialize() {
        tableClient = tableServiceClient.createTableIfNotExists(dbConfig.getTableName());
    }

    public void addDistributedLock(DistributedLock distributedLock) {
        TableEntity entity = new TableEntity(dbConfig.getPartitionKey(), distributedLock.getName())
                .addProperty("name", distributedLock.getName());
        try {
            tableClient.createEntity(entity);
        } catch (TableServiceException e) {
            TableErrorCode errCode = e.getValue().getErrorCode();
            if (errCode == TableErrorCode.ENTITY_ALREADY_EXISTS) {
                throw new DistributedLockInUseException();
            }
        }
    }

    public void deleteDistributedLock(DistributedLock distributedLock) {
        try {
            tableClient.deleteEntity(dbConfig.getPartitionKey(), distributedLock.getName());
        } catch (TableServiceException e) {
            TableErrorCode errCode = e.getValue().getErrorCode();
            if (errCode == TableErrorCode.ENTITY_NOT_FOUND) {
                throw new DistributedLockNotFoundException();
            }
        } catch (IllegalArgumentException e) {
            logger.debug("Exception " + e.getMessage());
        }
        finally {
            logger.debug("deleteDistributedLock finally");
        }
    }

    @Override
    public void destroyDistributedLockTable() {
        tableServiceClient.deleteTable(dbConfig.getTableName());
    }

    public void deleteAllDistributedLocks() {
        var tableEntities = tableClient.listEntities();
        for (TableEntity tableEntity : tableEntities) {
            try {
                tableClient.deleteEntity(tableEntity);
            } catch (TableServiceException e) {
                logger.error("Unable to delete distribute lock entity: " + e);
                throw new RuntimeException(e);
            }
        }
    }

    public List<DistributedLock> getAllDistributedLocks() {
        List<String> propertiesToSelect = new ArrayList<>();
        propertiesToSelect.add("name");

        ListEntitiesOptions options = new ListEntitiesOptions()
                .setFilter(String.format("PartitionKey eq '%s'", dbConfig.getPartitionKey()))
                .setSelect(propertiesToSelect);

        List<DistributedLock> distributedLocks = new ArrayList<>();
        for (TableEntity entity : tableClient.listEntities(options, null, null)) {
            var name = (String) entity.getProperty("name");
            distributedLocks.add(new DistributedLock(name));
        }
        return distributedLocks;
    }

    // Pending impl.
    private DistributedLock mapTableEntityToMigration(TableEntity entity) {
        return null;
    }
}
