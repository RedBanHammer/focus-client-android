package edu.usc.csci310.focus.focus.dataobjects;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

/**
 * An application with a name and bundle identifier.
 */

public class App extends NamedObject {
    private Drawable icon;
    public App(String name, String identifier) {
        super(name, identifier);
    }

    public void setIcon(Drawable icon)
    {
        this.icon = icon;
    }

    public Drawable getIcon()
    {
        return this.icon;
    }
}
