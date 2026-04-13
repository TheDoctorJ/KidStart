package ca.kidstart.kidstart.model;

public class ChipItem {
    private final String label;
    private boolean selected;
    private ActivityFilter filter;

    public ChipItem(String label, boolean selected) {
        this.label = label;
        this.selected = selected;
        this.filter = null;
    }

    public ChipItem(String label, boolean selected, ActivityFilter filter) {
        this.label = label;
        this.selected = selected;
        this.filter = filter;
    }

    public String getLabel() {
        return label;
    }

    public boolean isSelected() {
        return selected;
    }
    public ActivityFilter getFilter() { return filter; }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}