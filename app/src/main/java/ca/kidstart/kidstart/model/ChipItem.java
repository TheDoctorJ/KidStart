package ca.kidstart.kidstart.model;

public class ChipItem {
    private final String label;
    private boolean selected;

    public ChipItem(String label, boolean selected) {
        this.label = label;
        this.selected = selected;
    }

    public String getLabel() {
        return label;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}