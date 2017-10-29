package edu.usc.csci310.focus.focus.dataobjects;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by bdeng on 10/28/17.
 */
public class ProfileTest {
    private Profile testProfile;
    private App testApp;
    private String appName = "Test";
    private String appId = "Test-id";
    @Before
    public void setUp() {
        testProfile = new Profile("Test-Profile");
        testApp = new App(appName, appId);

    }

    @Test
    public void setAppsTest() throws Exception {
        ArrayList<App> expectedAppList = new ArrayList<>();
        expectedAppList.add(testApp);
        testProfile.setApps(expectedAppList);

        ArrayList<App> resultAppList = testProfile.getApps();
        assertEquals(expectedAppList, resultAppList);
     }

    @Test
    public void addAppTest() throws Exception {
        //Add an app to a profile
        testProfile.addApp(testApp);

        boolean containApp = testProfile.getApps().contains(testApp);
        assertEquals(true, containApp);

    }

    @Test
    public void removeAppWithName() throws Exception {
        testProfile.addApp(testApp);

        testProfile.removeAppWithName(appName);
        boolean containApp = testProfile.getApps().contains(testApp);
        assertEquals(false, containApp);
    }

    @Test
    public void removeAppNonExistentName() throws Exception {
        ArrayList<App> expectedApps = new ArrayList<>();
        expectedApps.add(testApp);
        testProfile.setApps(expectedApps);

        testProfile.removeAppWithName("dummy-name");
        //check testProfile still has all its apps
        ArrayList<App> resultApps = testProfile.getApps();
        assertEquals(resultApps, expectedApps);
    }

    @Test
    public void removeAppWithIdentifier() throws Exception {
        testProfile.addApp(testApp);
        testProfile.removeAppWithIdentifier(appId);

        boolean success = testProfile.getApps().contains(testApp);
        assertEquals(false, success);
    }
    @Test
    public void getBigApps() throws Exception {
        ArrayList<App> expectedList = new ArrayList<>();
        for(int i = 0; i < 50; i++)
        {
            String name = i + "app";
            String id = i + "id";
            App a = new App(name, id);
            expectedList.add(a);
        }
        testProfile.setApps(expectedList);

        ArrayList<App> resultList = testProfile.getApps();

        assertEquals(resultList, expectedList);
    }


}