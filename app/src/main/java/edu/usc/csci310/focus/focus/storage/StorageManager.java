package edu.usc.csci310.focus.focus.storage;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

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
    private Context context;

    private StorageManager() {

    }

    public static StorageManager getDefaultManager() {
        return defaultManager;
    }
    public static StorageManager getDefaultManagerWithContext(@NonNull Context context) {
        defaultManager.setContext(context);
        return defaultManager;
    }

    public void setContext(@NonNull Context context) {
        this.context = context;
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
    public <T extends Serializable> void setObject(@NonNull  T object, @NonNull String group, @NonNull String identifier) {
        try {
            String fullPath = this.context.getFilesDir().getAbsolutePath() + "/" + group;

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
    public <T extends Serializable> T getObject(@NonNull String group, @NonNull String identifier) {
        T object = null;

        try {
            String fullPath = this.context.getFilesDir().getAbsolutePath() + "/" + group;

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
    public @NonNull ArrayList<Serializable> getObjectsWithPrefix(@NonNull String group) {
        ArrayList<Serializable> objects = new ArrayList<Serializable>();

        if (this.context == null) {
            System.out.println("wtf");
        }
        String fullPath = this.context.getFilesDir().getAbsolutePath() + "/" + group;

        File dir = new File(fullPath);
        if (!dir.exists()) {
            return objects;
        }

        for (final File fileEntry : dir.listFiles()) {
            String identifier = fileEntry.getName();
            Serializable object = this.getObject(group, identifier);
            if (object != null) {
                objects.add(object);
            } else {
                // Unable to deserialize the object, delete it
                this.removeObject(group, identifier);
            }
        }

        return objects;
    }

    /**
     * Remove the object matching the storage identifier.
     * @param group The name of the group to delete the object from.
     * @param identifier A unique identifier for the object to delete.
     */
    public void removeObject(@NonNull String group, @NonNull String identifier) {
        String fullPath = this.context.getFilesDir().getAbsolutePath() + "/" + group;

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
    public void removeObjectsWithPrefix(@NonNull String group) {
        String fullPath = this.context.getFilesDir().getAbsolutePath() + "/" + group;

        File dir = new File(fullPath);
        if (!dir.exists()) {
            return;
        }

        dir.delete();

    }
}
