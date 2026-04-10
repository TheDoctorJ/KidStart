package ca.kidstart.kidstart.model;

public interface ActivityFilter {
    public boolean isIncluded(ActivityItem activity);

    /**
     *
     */
    public class InterestCategoryFilter implements ActivityFilter {
        private InterestCategory toFilter;
        public InterestCategoryFilter(InterestCategory newToFilter) {
            this.toFilter = newToFilter;
        }

        @Override
        public boolean isIncluded(ActivityItem activity) {
            return false;
        }
    }
}
