public class Aventuri {
    private int cod;
    private String denumire;
    private float tarif;
    private int locuriDisp;

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

    public float getTarif() {
        return tarif;
    }

    public void setTarif(float tarif) {
        this.tarif = tarif;
    }

    public int getLocuriDisp() {
        return locuriDisp;
    }

    public void setLocuriDisp(int locuriDisp) {
        this.locuriDisp = locuriDisp;
    }

    public Aventuri(int cod, String denumire, float tarif, int locuriDisp) {
        this.cod = cod;
        this.denumire = denumire;
        this.tarif = tarif;
        this.locuriDisp = locuriDisp;
    }

    public Aventuri() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Aventuri{");
        sb.append("cod=").append(cod);
        sb.append(", denumire='").append(denumire).append('\'');
        sb.append(", tarif=").append(tarif);
        sb.append(", locuriDisp=").append(locuriDisp);
        sb.append('}');
        return sb.toString();
    }
}
