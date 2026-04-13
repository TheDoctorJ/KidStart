package ca.kidstart.kidstart.model;

public class FreeFilter extends ActivityFilter {
    public FreeFilter() {
        this.name = "Free";
    }

    @Override
    public boolean isIncluded(ActivityItem activity) {
        return activity.getPrice().equals(ActivityItem.FREE_PRICE);
    }
}
