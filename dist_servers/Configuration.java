
public class Configuration {
    private int faultToleranceLevel;

    public Configuration(int faultToleranceLevel) {
        this.faultToleranceLevel = faultToleranceLevel;
    }

    public int getFaultToleranceLevel() {
        return faultToleranceLevel;
    }

    public void setFaultToleranceLevel(int faultToleranceLevel) {
        this.faultToleranceLevel = faultToleranceLevel;
    }
}
