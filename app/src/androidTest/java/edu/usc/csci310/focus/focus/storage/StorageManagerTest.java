package edu.usc.csci310.focus.focus.storage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresPermission;
import android.support.test.InstrumentationRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.concurrent.ThreadSafe;

import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Test Storage Manager public API methods.
 */
public class StorageManagerTest {
    @Test
    public void testGetDefaultManager() throws Exception {
        StorageManager expectedManager = StorageManager.getDefaultManager();
        assertEquals(expectedManager, StorageManager.getDefaultManager());
    }

    @Test
    public void testGetDefaultManagerWithContext() throws Exception {
        Context context = mock(Context.class);

        StorageManager expectedManager = StorageManager.getDefaultManager();
        StorageManager testedManager = StorageManager.getDefaultManagerWithContext(context);

        assertEquals(expectedManager, testedManager);
        assertEquals(context, testedManager.getContext());
    }

    @Test
    public void testSetContext() throws Exception {
        Context context = mock(Context.class);

        StorageManager testedManager = StorageManager.getDefaultManager();
        testedManager.setContext(context);

        assertEquals(context, testedManager.getContext());
    }

    @Rule
    public TemporaryFolder tmpFileDir = new TemporaryFolder();

    @Test
    public void testSetGetRemoveSingleObject() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        StorageManager testedManager = StorageManager.getDefaultManagerWithContext(context);
        File baseDir = this.tmpFileDir.newFolder("storage-test");
        String testGroup = "profiles-test";

        Profile expectedProfile = new Profile("A Test Profile");

        ArrayList<App> expectedApps = new ArrayList<App>();
        expectedApps.add(new App("A Test App", "com.example.app.test"));
        expectedProfile.setApps(expectedApps);

        // Set
        testedManager.setObject(expectedProfile, testGroup, expectedProfile.getIdentifier(), baseDir.getAbsolutePath());

        // Get
        Profile testedProfile = (Profile)testedManager.getObject(testGroup, expectedProfile.getIdentifier(), baseDir.getAbsolutePath());
        assertEquals(expectedProfile, testedProfile);
        assertEquals(expectedProfile.getName(), testedProfile.getName());
        assertEquals(expectedProfile.getApps(), testedProfile.getApps());

        // Remove
        boolean success = testedManager.removeObject(testGroup, expectedProfile.getIdentifier(), baseDir.getAbsolutePath());
        assertEquals(true, success);
        assertEquals(null, testedManager.getObject(testGroup, expectedProfile.getIdentifier(), baseDir.getAbsolutePath()));
    }

    @Test
    public void testSetGetRemoveObjectsWithPrefix() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        StorageManager testedManager = StorageManager.getDefaultManagerWithContext(context);
        File baseDir = this.tmpFileDir.newFolder("storage-test");

        ArrayList<Profile> expectedProfiles = new ArrayList<Profile>();
        String testGroup = "profiles-test";

        // Set and get multiple test serializable objects with the same test group.
        for (int i = 0; i < 5; i++) {
            Profile expectedProfile = new Profile("A Test Profile " + i);

            ArrayList<App> expectedApps = new ArrayList<App>();
            expectedApps.add(new App("A Test App", "com.example.app.test"));
            expectedProfile.setApps(expectedApps);

            expectedProfiles.add(expectedProfile);

            testedManager.setObject(expectedProfile, testGroup, expectedProfile.getIdentifier(), baseDir.getAbsolutePath());

            Profile testedProfile = (Profile)testedManager.getObject(testGroup, expectedProfile.getIdentifier(), baseDir.getAbsolutePath());
            assertEquals(expectedProfile, testedProfile);
            assertEquals(expectedProfile.getName(), testedProfile.getName());
            assertEquals(expectedProfile.getApps(), testedProfile.getApps());
        }

        // Get all expected serializable objects
        ArrayList<Serializable> rawTestedProfiles = testedManager.getObjectsWithPrefix(testGroup, baseDir.getAbsolutePath());
        ArrayList<Profile> testedProfiles = new ArrayList<Profile>();
        for (Serializable rawProfile : rawTestedProfiles) {
            if (rawProfile == null) {
                continue;
            }
            testedProfiles.add((Profile)rawProfile);
        }

        assertEquals(expectedProfiles, testedProfiles);

        // Remove all serializable objects
        boolean success = testedManager.removeObjectsWithPrefix(testGroup, baseDir.getAbsolutePath());
        assertEquals(true, success);
        assertEquals(new ArrayList<Serializable>(), testedManager.getObjectsWithPrefix(testGroup, baseDir.getAbsolutePath()));
    }

    @Test
    public void testGetNonExistentObject() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        StorageManager testedManager = StorageManager.getDefaultManagerWithContext(context);
        File baseDir = this.tmpFileDir.newFolder("storage-test");
        String testGroup = "profiles-test";

        Serializable testObject = testedManager.getObject(testGroup, "a-non-existent-identifier", baseDir.getAbsolutePath());
        assertEquals(null, testObject);
    }

    @Test
    public void testGetNonExistentObjectsWithPrefix() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        StorageManager testedManager = StorageManager.getDefaultManagerWithContext(context);
        File baseDir = this.tmpFileDir.newFolder("storage-test");
        String testGroup = "profiles-test";

        ArrayList<Serializable> testObjects = testedManager.getObjectsWithPrefix(testGroup, baseDir.getAbsolutePath());
        assertEquals(new ArrayList<Serializable>(), testObjects);
    }

    @Test
    public void testRemoveNonExistentObject() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        StorageManager testedManager = StorageManager.getDefaultManagerWithContext(context);
        File baseDir = this.tmpFileDir.newFolder("storage-test");
        String testGroup = "a-non-existent-group";

        boolean success = testedManager.removeObject(testGroup, "a-non-existent-identifier", baseDir.getAbsolutePath());
        assertEquals(false, success);
    }

    @Test
    public void testRemoveNonExistentGroup() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        StorageManager testedManager = StorageManager.getDefaultManagerWithContext(context);
        File baseDir = this.tmpFileDir.newFolder("storage-test");
        String testGroup = "a-non-existent-group";

        boolean success = testedManager.removeObjectsWithPrefix(testGroup, baseDir.getAbsolutePath());
        assertEquals(false, success);
    }
}