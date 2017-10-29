package edu.usc.csci310.focus.focus.managers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import edu.usc.csci310.focus.focus.storage.StorageManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by bdeng on 10/29/17.
 */
public class ProfileManagerTest {
    private ProfileManager mockedProfileManager = mock(ProfileManager.class);
    private StorageManager mockedStorageManager = mock(StorageManager.class);
    public TemporaryFolder tmpFileDir = new TemporaryFolder();


    @Before
    public void setUp() throws Exception {
        File baseDir = this.tmpFileDir.newFolder("storage-test");


    }

    @Test
    public void getDefaultManager() throws Exception {

    }

    @Test
    public void setProfile() throws Exception {

    }

    @Test
    public void removeProfile() throws Exception {

    }

    @Test
    public void removeProfileWithIdentifier() throws Exception {

    }

    @Test
    public void getProfileWithName() throws Exception {

    }

    @Test
    public void getProfileWithIdentifier() throws Exception {

    }

    @Test
    public void getProfilesWithNames() throws Exception {

    }

    @Test
    public void getProfileWithIdentifiers() throws Exception {

    }

    @Test
    public void getAllProfiles() throws Exception {

    }

}