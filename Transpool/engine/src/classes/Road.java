package classes;

public class Road {
    private String origin;
    private String destination;
    private boolean isOneWay;
    private int roadLength;
    private int gasConsumption;
    private int maxSpeed;

    public Road(){}

    public Road(String origin, String destination, boolean isOneWay, int roadLength, int gasConsumption, int maxSpeed) {
        this.origin = origin;
        this.destination = destination;
        this.isOneWay = isOneWay;
        this.roadLength = roadLength;
        this.gasConsumption = gasConsumption;
        this.maxSpeed=maxSpeed;
    }



    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public double getRoadLength() {
        return roadLength;
    }

    public void setRoadLength(int roadLength) {
        this.roadLength = roadLength;
    }

    public double getGasConsumption() {
        return gasConsumption;
    }

    public void setGasConsumption(int gasConsumption) {
        this.gasConsumption = gasConsumption;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    //maybe we need to do calculate road length and gas consumption
}
