package edu.usc.csci310.focus.focus.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.test.mock.MockContext;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.Calendar;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.RecurringTime;
import edu.usc.csci310.focus.focus.dataobjects.Schedule;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.storage.StorageManager;

import static org.junit.Assert.*;

/**
 * Created by Ashley Walker on 10/28/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class AppBlockedPopupTest {

    Context context;

    @Rule
    public TemporaryFolder tmpFileDir = new TemporaryFolder();

    @Test
    public void onCreateDialog() throws Exception {
        context = InstrumentationRegistry.getInstrumentation().getContext();
        StorageManager testedManager = StorageManager.getDefaultManagerWithContext(context);
        File baseDir = this.tmpFileDir.newFolder("storage-test");

        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putString("profile", "profileName");
        boolean containsKey = savedInstanceState.containsKey("profile");
        assertTrue(containsKey);

        Profile p = new Profile("profileName");
        App a = new App("appName", "appIdentifier");
        p.addApp(a);
        StorageManager.getDefaultManagerWithContext(context).setObject(p, "Profiles", p.getIdentifier());

        Schedule s = new Schedule("scheduleName");
        RecurringTime rt = new RecurringTime();
        Calendar c = Calendar.getInstance();
        rt.addTime(c.get(Calendar.DAY_OF_WEEK)-1,
                (long)(c.get(Calendar.HOUR)*60 + c.get(Calendar.MINUTE)),
                (long) 10);
        s.addProfile(p.getIdentifier(), rt);
        s.setIsActive(true);
        StorageManager.getDefaultManagerWithContext(context).setObject(s, "Schedules", s.getIdentifier());

        AppBlockedPopup abp = new AppBlockedPopup();
        abp.onCreateDialog(savedInstanceState);

        String message = abp.getDialog().toString();
        String expected = "appName is being blocked by the following schedule:\n\n• scheduleName\n";
        assertEquals(expected, message);
    }

}