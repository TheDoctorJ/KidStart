package ca.kidstart.kidstart.model;

public class InterestCategoryFilter extends ActivityFilter {
    private InterestCategory targetCategory;
    public InterestCategoryFilter(InterestCategory newTargetCategory) {
        this.targetCategory = newTargetCategory;
        this.name = newTargetCategory.getName();
    }

    @Override
    public boolean isIncluded(ActivityItem activity) {
        return activity.getCategory() == targetCategory;
    }
}
