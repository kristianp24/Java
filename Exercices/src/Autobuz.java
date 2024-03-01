public class Autobuz implements Cloneable {
    private int anul;
    private String marca;
    private int nrlocuri;
    private final int id;

    int getAnul(){
        return this.anul;
    }

    void setAnul(int an){
        if(an>0)
            this.anul=an;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getNrlocuri() {
        return nrlocuri;
    }

    public void setNrlocuri(int nrlocuri) {
        this.nrlocuri = nrlocuri;
    }

    public Autobuz(){
        anul=2009;
        marca="Benz";
        nrlocuri=50;
        id=0;
    }
    public Autobuz(int a,String m,int loc,int i){
        id=i;
        anul=a;
        marca=m;
        nrlocuri=loc;
    }

    //Constructor copiere
    public Autobuz(Autobuz a){
        this.marca=a.marca;
        this.anul=a.anul;
        this.nrlocuri=a.nrlocuri;
        this.id=a.id;
    }

    @Override
    public String toString() {
        return "Autobuz{" +
                "anul=" + anul +
                ", marca='" + marca + '\'' +
                ", nrlocuri=" + nrlocuri +
                ", id=" + id +
                '}';
    }

    //Clonarea
    @Override
    public Autobuz clone() throws CloneNotSupportedException {
        Autobuz copy= (Autobuz)super.clone();
        copy.nrlocuri=nrlocuri;
        copy.anul=anul;
        //copy.id=id;
        copy.marca=marca;
        return copy;
    }


}
