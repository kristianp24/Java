import java.util.Objects;

public class HybridTram extends Tram{
    public void setTimeCharging(int timeCharging) {
        this.timeCharging = timeCharging;
    }

    private int timeCharging;
    private int speed;

    public HybridTram(){
        super();
        this.speed=80;
        this.timeCharging=2;
    }

    public HybridTram(int nr,String name,int time,int speed){
        super(nr,name);
        timeCharging=time;
        this.speed=speed;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        HybridTram copy = (HybridTram) super.clone();
        return copy;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("HybridTram{");
        sb.append("timeCharging=").append(timeCharging);
        sb.append(", speed=").append(speed);
        sb.append(" Line:").append(this.getNr());
        sb.append(" Name:").append(this.getName());
        sb.append(" No stations:").append(this.getNo_stations());
        sb.append('}');

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HybridTram that = (HybridTram) o;
        return timeCharging == that.timeCharging && speed == that.speed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timeCharging, speed);
    }
}
