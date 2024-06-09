public class Capitol {
    private int codCapitol;
    private int codSantier;
    private String denumireCheltuiala;
    private String unitateMasura;
    private float cantitate;
    private float pretUnitar;

    public int getCodCapitol() {
        return codCapitol;
    }

    public int getCodSantier() {
        return codSantier;
    }

    public String getDenumireCheltuiala() {
        return denumireCheltuiala;
    }

    public String getUnitateMasura() {
        return unitateMasura;
    }

    public float getCantitate() {
        return cantitate;
    }

    public float getPretUnitar() {
        return pretUnitar;
    }

    public Capitol(int codCapitol, int codSantier, String denumireCheltuiala, String unitateMasura, float cantitate, float pretUnitar) {
        this.codCapitol = codCapitol;
        this.codSantier = codSantier;
        this.denumireCheltuiala = denumireCheltuiala;
        this.unitateMasura = unitateMasura;
        this.cantitate = cantitate;
        this.pretUnitar = pretUnitar;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Capitol{");
        sb.append("codCapitol=").append(codCapitol);
        sb.append(", codSantier=").append(codSantier);
        sb.append(", denumireCheltuiala='").append(denumireCheltuiala).append('\'');
        sb.append(", unitateMasura='").append(unitateMasura).append('\'');
        sb.append(", cantitate=").append(cantitate);
        sb.append(", pretUnitar=").append(pretUnitar);
        sb.append('}');
        return sb.toString();
    }
}
