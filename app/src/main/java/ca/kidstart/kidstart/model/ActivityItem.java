package ca.kidstart.kidstart.model;

public class ActivityItem {
    private final int imageResId;
    private final InterestCategory category;
    private final String title;
    private final String location;
    private final String ageRange;
    private final String price;
    private final String rating;
    private final String distance;

    public static final String FREE_PRICE = "Free";

    public ActivityItem(int imageResId, InterestCategory category, String title, String location,
                        String ageRange, String price, String rating, String distance) {
        this.imageResId = imageResId;
        this.category = category;
        this.title = title;
        this.location = location;
        this.ageRange = ageRange;
        this.price = price;
        this.rating = rating;
        this.distance = distance;
    }

    public int getImageResId() {
        return imageResId;
    }

    public InterestCategory getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public String getDistance() {
        return distance;
    }
}