package edu.usc.csci310.focus.focus.storage;
import edu.usc.csci310.focus.focus.dataobjects.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Manages disk storage operations for the business logic layer.
 */

public class StorageManager {
    private static StorageManager defaultManager = new StorageManager();

    private StorageManager() {}

    public static StorageManager getDefaultManager() {
        return defaultManager;
    }

    /**
     * Public API methods.
     */

    /**
     * Save an object to disk storage.
     * @param object The object to save.
     * @param identifier A namespaced string identifier associated with the saved object.
     */
    public void setObject(Serializable object, String identifier) {

    }

    /**
     * Get an object from disk storage based on its namespaced identifier.
     * @source https://stackoverflow.com/a/33896724
     * @param identifier A namespaced string identifier used to retrieve the object.
     */
    public <T extends Serializable> T getObject(String identifier) {
        return null;
    }

    /**
     * Get all objects from disk storage based on the identifier prefix.
     * @param identifierPrefix A namespace prefix to match against object identifiers.
     * @return An ArrayList of all objects matching the identifier prefix.
     */
    public ArrayList<Serializable> getObjectsWithPrefix(String identifierPrefix) {
        return new ArrayList<Serializable>();
    }

    /**
     * Remove the object matching the storage identifier.
     * @param identifier A namespaced string identifier.
     */
    public void removeObject(String identifier) {

    }

    /**
     * Remove all objects matching the storage identifier prefix.
     * @param identifierPrefix A namespace prefix to match against object identifiers.
     */
    public void removeObjectsWithPrefix(String identifierPrefix) {

    }

}
