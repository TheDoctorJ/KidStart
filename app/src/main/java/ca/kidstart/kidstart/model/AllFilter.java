package ca.kidstart.kidstart.model;

public class AllFilter extends ActivityFilter {

    public AllFilter() {
        this.name = "All";
    }

    @Override
    public boolean isIncluded(ActivityItem activity) {
        return true;
    }
}
