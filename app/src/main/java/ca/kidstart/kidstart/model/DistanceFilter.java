package ca.kidstart.kidstart.model;

public class DistanceFilter extends ActivityFilter {
    private int maxDistance;
    public DistanceFilter(int newMaxDistance) {
        this.maxDistance = newMaxDistance;
        this.name = "<" + newMaxDistance + "km";
    }

    @Override
    public boolean isIncluded(ActivityItem activity) {
        String distanceString = activity.getDistance();
        char current;
        String result = "";
        for (int i = 0; i < distanceString.length(); i++) {
            current = distanceString.charAt(i);
            if (Character.isDigit(current))
                result += current;
        }
        return Integer.parseInt(result) <= maxDistance;
    }
}
