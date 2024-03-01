import java.util.ArrayList;
import java.util.List;

public class Autobaza implements Cloneable {
    private int nrAutobuze;
  private  List <Autobuz> buzes;
    public Autobaza(){
        nrAutobuze=1;
        buzes=new ArrayList<>(nrAutobuze);
        buzes.add(new Autobuz());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Autobaza copy = (Autobaza) super.clone();
        copy.nrAutobuze=nrAutobuze;
        copy.buzes=new ArrayList<>(copy.nrAutobuze);
        for(int i=0;i< copy.nrAutobuze;i++)
            copy.buzes.add(buzes.get(i).clone());
        //get ia elementul din poz respectiva

        return copy;
    }
}
