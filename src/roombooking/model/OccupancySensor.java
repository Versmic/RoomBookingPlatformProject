package roombooking.model;

public class OccupancySensor {

    private boolean occupied;

    public boolean detectOccupancy() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
