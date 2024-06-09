public class Santier {
    private int codSantier;
    private String localitate;
    private String strada;
    private String obiectiv;
    private double valoare;

    public int getCodSantier() {
        return codSantier;
    }

    public String getLocalitate() {
        return localitate;
    }

    public String getStrada() {
        return strada;
    }

    public String getObiectiv() {
        return obiectiv;
    }

    public double getValoare() {
        return valoare;
    }

    public Santier(int codSantier, String localitate, String strada, String obiectiv, double valoare) {
        this.codSantier = codSantier;
        this.localitate = localitate;
        this.strada = strada;
        this.obiectiv = obiectiv;
        this.valoare = valoare;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Santier{");
        sb.append("codSantier=").append(codSantier);
        sb.append(", localitate='").append(localitate).append('\'');
        sb.append(", strada='").append(strada).append('\'');
        sb.append(", obiectiv='").append(obiectiv).append('\'');
        sb.append(", valoare=").append(valoare);
        sb.append('}');
        return sb.toString();
    }
}
