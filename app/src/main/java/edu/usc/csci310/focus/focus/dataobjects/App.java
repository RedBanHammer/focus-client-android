package edu.usc.csci310.focus.focus.dataobjects;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

/**
 * An application with a name and bundle identifier.
 */

public class App extends NamedObject {
    public App(String name, String identifier) {
        super(name, identifier);
    }

}
