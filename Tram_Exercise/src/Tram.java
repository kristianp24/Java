import java.util.Objects;

public class Tram implements Cloneable{
    private final int nr;
    private String name;

    public int getNr() {
        return nr;
    }
    private int no_stations;
    private int min_station;

    public String getName() {
        return name;
    }

    public int getNo_stations() {
        return no_stations;
    }

    public int getMin_station() {
        return min_station;
    }


    public Tram(int nr,String name) {
        this.nr = nr;
        if(name!=null)
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tram(){
        this.nr=123;
        this.name="Anonim";
        this.no_stations=10;
        this.min_station=1;
    }

    public Tram(Tram t){
        this.nr=t.nr;
        this.name=name;
        this.min_station= t.min_station;;
        this.no_stations=t.min_station;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("Tram:");
        s.append(this.name);
        s.append(" Id:");
        s.append(this.nr);
        s.append(" Num. stations:");
        s.append(this.no_stations);
        s.append(" Min per station:");
        s.append(this.min_station);

        return s.toString();

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Tram copy = (Tram)super.clone();
        String aux = this.name;
        copy.name=aux;
        return copy;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tram tram = (Tram) o;
        return nr == tram.nr && no_stations == tram.no_stations && min_station == tram.min_station && Objects.equals(name, tram.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nr, name, no_stations, min_station);
    }
}
