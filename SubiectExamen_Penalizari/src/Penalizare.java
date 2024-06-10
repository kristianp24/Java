public class Penalizare {
    private int nrApartament;
    private float penalizare;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Penalizare{");
        sb.append("nrApartament=").append(nrApartament);
        sb.append(", penalizare=").append(penalizare);
        sb.append('}');
        return sb.toString();
    }

    public int getNrApartament() {
        return nrApartament;
    }

    public float getPenalizare() {
        return penalizare;
    }

    public Penalizare(int nrApartament, float penalizare) {
        this.nrApartament = nrApartament;
        this.penalizare = penalizare;
    }
}
