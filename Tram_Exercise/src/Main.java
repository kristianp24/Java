import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Tram t1= new Tram();
        System.out.println(t1);
        Tram t2 = new Tram();
        try {
            t2=(Tram)t1.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        t1.setName("Linia11");
        System.out.println(t2);

        HybridTram h1=new HybridTram(145,"Linia25",80,5);
        HybridTram h2= new HybridTram();
        try {
            h2=(HybridTram) h1.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(h1);
        System.out.println(h2);
        //h2.setTimeCharging(19);
        //h2.setName("Sth else");

        if(h1.equals(h2))
            System.out.println("Egale");
        else
            System.out.println("No egale");

        ArrayList<Tram> trams = new ArrayList<>();
        trams.add(t1);
        trams.add(t2);
        trams.add(h1);
        trams.add(h2);
        for(Tram t: trams){
            System.out.println(t);
        }

    }


}
