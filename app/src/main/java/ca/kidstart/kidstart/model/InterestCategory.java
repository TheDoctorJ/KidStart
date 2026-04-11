package ca.kidstart.kidstart.model;

import android.graphics.drawable.Drawable;

public class InterestCategory {
    private Drawable icon;
    private String name;
    private String description;

    public InterestCategory(Drawable newIcon, String newName, String newDescription) {
        this.icon = newIcon;
        this.name = newName;
        this.description = newDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
