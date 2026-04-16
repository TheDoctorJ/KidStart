package ca.kidstart.kidstart.model;


import android.content.Context;
import android.graphics.drawable.Drawable;

import ca.kidstart.kidstart.R;

public class InterestCategory {
    private Drawable icon;
    private String name;
    private String description;
    private Categories category;
    public enum Categories {
        Science,
        Art,
        Computers,
        Music,
        Sports,
        Reading,
        Daycare
    }

    public InterestCategory(Categories category, Context context) {
        this.icon = context.getDrawable(
                context.getResources().obtainTypedArray(R.array.interest_category_drawables).getResourceId(category.ordinal(), 0));
        this.name = context.getResources().getStringArray(R.array.interest_category_names)[category.ordinal()];
        this.description = context.getResources().getStringArray(R.array.interest_category_descriptions)[category.ordinal()];
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
    public Drawable getIcon() {
        return icon;
    }
    public Categories getCategory() { return category; }
}
