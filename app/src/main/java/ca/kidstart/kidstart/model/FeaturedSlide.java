package ca.kidstart.kidstart.model;

public class FeaturedSlide {
    private final int imageResId;
    private final String tag;
    private final String title;

    public FeaturedSlide(int imageResId, String tag, String title) {
        this.imageResId = imageResId;
        this.tag = tag;
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }
}