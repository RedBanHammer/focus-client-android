package edu.usc.csci310.focus.focus.storage;
import android.content.Context;
import android.os.Environment;

import edu.usc.csci310.focus.focus.dataobjects.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Manages disk storage operations for the business logic layer.
 */

public class StorageManager {
    private static StorageManager defaultManager = new StorageManager();

    private StorageManager() {

    }

    public static StorageManager getDefaultManager() {
        return defaultManager;
    }

    /**
     * Public API methods.
     */

    /**
     * Save an object to disk storage.
     * @param object The object to save.
     * @param group The name of the group to store the object in.
     * @param identifier A unique identifier for the object to store.
     */
    public <T extends Serializable> void setObject(T object, String group, String identifier) {
        try {
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + group;

            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(fullPath, identifier);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            FileOutputStream fileOutputStream = context.openFileOutput(identifier, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write
            objectOutputStream.writeObject(object);

            // Clean up
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get an object from disk storage based on its namespaced identifier.
     * @source https://stackoverflow.com/a/33896724
     * @param group The name of the group to retrieve the object from.
     * @param identifier A unique identifier for the object to retrieve.
     * @return An in the group `group` and the identifier `identifier` if it exists.
     */
    public <T extends Serializable> T getObject(String group, String identifier) {
        T object = null;

        try {
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + group;

            File dir = new File(fullPath);
            if (!dir.exists()) {
                return object;
            }

            File file = new File(fullPath, identifier);
            if (!file.exists()) {
                return object;
            }

            FileInputStream fileInputStream = new FileInputStream(file);
//            FileInputStream fileInputStream = context.openFileInput(identifier);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Read
            object = (T) objectInputStream.readObject();

            // Clean up
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * Get all objects from disk storage inside a particular group.
     * @param group The name of the group to retrieve objects from.
     * @return An ArrayList of all objects inside the grouop
     */
    public ArrayList<Serializable> getObjectsWithPrefix(String group) {
        ArrayList<Serializable> objects = new ArrayList<Serializable>();

        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + group;

        File dir = new File(fullPath);
        if (!dir.exists()) {
            return objects;
        }

        for (final File fileEntry : dir.listFiles()) {
            Serializable object = this.getObject(group, fileEntry.getName());
            objects.add(object);
        }

        return objects;
    }

    /**
     * Remove the object matching the storage identifier.
     * @param group The name of the group to delete the object from.
     * @param identifier A unique identifier for the object to delete.
     */
    public void removeObject(String group, String identifier) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + group;

        File dir = new File(fullPath);
        if (!dir.exists()) {
            return;
        }

        File file = new File(fullPath, identifier);
        if (!file.exists()) {
            return;
        }

        file.delete();
    }

    /**
     * Remove all objects inside a group.
     * @param group The name of the group to delete the objects from.
     */
    public void removeObjectsWithPrefix(String group) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + group;

        File dir = new File(fullPath);
        if (!dir.exists()) {
            return;
        }

        dir.delete();
    }
}
