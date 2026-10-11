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

    protected TableServiceClient tableServiceClient;
    protected TableClient tableClient;

    private static String localDevelopmentInstance =
            """
                    DefaultEndpointsProtocol=http;
                    AccountName=devstoreaccount1;
                    AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;
                    BlobEndpoint=http://127.0.0.1:10000/devstoreaccount1;
                    QueueEndpoint=http://127.0.0.1:10001/devstoreaccount1;
                    TableEndpoint=http://127.0.0.1:10002/devstoreaccount1;""";

    public AzureTableStorageClient() {
        tableServiceClient = new TableServiceClientBuilder()
                .connectionString(localDevelopmentInstance)    // TODO: magic number
                .buildClient();

        initialize();
    }

    private void initialize() {
        tableClient = tableServiceClient.createTableIfNotExists(DatabaseConfiguration.TABLE_NAME);
    }

    private void getTableContents() {

    }

    public void addDistributedLock(DistributedLock distributedLock) {
        TableEntity entity = new TableEntity(DatabaseConfiguration.PARTITION_KEY, distributedLock.getName())
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
            tableClient.deleteEntity(DatabaseConfiguration.PARTITION_KEY, distributedLock.getName());
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
        tableServiceClient.deleteTable(DatabaseConfiguration.TABLE_NAME);
    }

    public List<DistributedLock> getAllDistributedLocks() {
        List<String> propertiesToSelect = new ArrayList<>();
        propertiesToSelect.add("name");

        ListEntitiesOptions options = new ListEntitiesOptions()
                .setFilter(String.format("PartitionKey eq '%s'", DatabaseConfiguration.PARTITION_KEY))
                .setSelect(propertiesToSelect);

        List<DistributedLock> migrations = new ArrayList<>();
        for (TableEntity entity : tableClient.listEntities(options, null, null)) {
            var name = (String) entity.getProperty("name");
            migrations.add(new DistributedLock(name));
        }
        return migrations;
    }

    // Pending impl.
    private DistributedLock mapTableEntityToMigration(TableEntity entity) {
        return null;
    }
}
