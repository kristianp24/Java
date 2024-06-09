public class Sectie {
    private  int cod;
    private String denumire;
    private  int nr_locuri;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getNr_locuri() {
        return nr_locuri;
    }

    public void setNr_locuri(int nr_locuri) {
        this.nr_locuri = nr_locuri;
    }

    public Sectie(int cod, String denumire, int nr_locuri) {
        this.cod = cod;
        this.denumire = denumire;
        this.nr_locuri = nr_locuri;
    }

    public Sectie() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sectie{");
        sb.append("cod=").append(cod);
        sb.append(", denumire='").append(denumire).append('\'');
        sb.append(", nr_locuri=").append(nr_locuri);
        sb.append('}');
        return sb.toString();
    }
}
