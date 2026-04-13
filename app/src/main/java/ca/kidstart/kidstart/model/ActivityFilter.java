package ca.kidstart.kidstart.model;

public abstract class ActivityFilter {
    protected String name;

    public String getName() { return name; }
    public abstract boolean isIncluded(ActivityItem activity);
}

