package org.mikkosdev.distributedlockj;

/**
 * This is the super-class for all migrations.
 * The migration classes should be immutable.
 */
public abstract class Migration {

    private String name;

    public Migration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
