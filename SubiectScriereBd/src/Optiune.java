public class Optiune {
    private int codLiceu;
    private  int codSpecializare;

    public Optiune(int codLiceu, int codSpecializare) {
        this.codLiceu = codLiceu;
        this.codSpecializare = codSpecializare;
    }

    public int getCodLiceu() {
        return codLiceu;
    }

    public int getCodSpecializare() {
        return codSpecializare;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Optiune{");
        sb.append("codLiceu=").append(codLiceu);
        sb.append(", codSpecializare=").append(codSpecializare);
        sb.append('}');
        return sb.toString();
    }
}
