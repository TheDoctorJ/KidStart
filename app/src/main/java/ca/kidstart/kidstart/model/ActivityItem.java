package ca.kidstart.kidstart.model;

public class ActivityItem {

    private final int imageResId;
    private final String category;
    private final String title;
    private final String location;
    private final String ageGroup;
    private final String price;
    private final String rating;
    private final String distance;

    public ActivityItem(int imageResId,
                        String category,
                        String title,
                        String location,
                        String ageGroup,
                        String price,
                        String rating,
                        String distance) {
        this.imageResId = imageResId;
        this.category = category;
        this.title = title;
        this.location = location;
        this.ageGroup = ageGroup;
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

    public String getAgeGroup() {
        return ageGroup;
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
        if (price == null) return Double.MAX_VALUE;

        String lower = price.toLowerCase().trim();
        if (lower.equals("free")) return 0;

        String numeric = lower.replace("$", "")
                .replace("/wk", "")
                .replace("/mo", "")
                .replace(",", "")
                .trim();

        try {
            return Double.parseDouble(numeric);
        } catch (NumberFormatException e) {
            return Double.MAX_VALUE;
        }
    }

    public double getNumericDistance() {
        if (distance == null) return Double.MAX_VALUE;

        String numeric = distance.toLowerCase()
                .replace("mi", "")
                .trim();

        try {
            return Double.parseDouble(numeric);
        } catch (NumberFormatException e) {
            return Double.MAX_VALUE;
        }
    }
}