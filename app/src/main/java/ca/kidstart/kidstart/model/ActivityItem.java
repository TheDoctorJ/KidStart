package ca.kidstart.kidstart.model;

import java.util.Objects;

public class ActivityItem {
    private final int imageResId;
    private final String category;
    private final String title;
    private final String location;
    private final String ageRange;
    private final String price;
    private final String rating;
    private final String distance;

    public ActivityItem(int imageResId, String category, String title, String location,
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

    public String getCategory() {
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

    public String getAgeGroup() {
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

    public double getNumericPrice() {
        String cleaned = price.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getNumericDistance() {
        String cleaned = distance.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityItem)) return false;
        ActivityItem that = (ActivityItem) o;
        return imageResId == that.imageResId
                && Objects.equals(category, that.category)
                && Objects.equals(title, that.title)
                && Objects.equals(location, that.location)
                && Objects.equals(ageRange, that.ageRange)
                && Objects.equals(price, that.price)
                && Objects.equals(rating, that.rating)
                && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageResId, category, title, location, ageRange, price, rating, distance);
    }
}