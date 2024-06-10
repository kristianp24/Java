public class Intretinere {
    private int nrApartament;
    private String numePropietar;
    private double valoarePlata;

    public Intretinere(int nrApartament, String numePropietar, double valoarePlata) {
        this.nrApartament = nrApartament;
        this.numePropietar = numePropietar;
        this.valoarePlata = valoarePlata;
    }

    public int getNrApartament() {
        return nrApartament;
    }

    public String getNumePropietar() {
        return numePropietar;
    }

    public double getValoarePlata() {
        return valoarePlata;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Intretinere{");
        sb.append("nrApartament=").append(nrApartament);
        sb.append(", numePropietar='").append(numePropietar).append('\'');
        sb.append(", valoarePlata=").append(valoarePlata);
        sb.append('}');
        return sb.toString();
    }
}
