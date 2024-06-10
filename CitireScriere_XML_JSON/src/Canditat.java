public class Canditat {
    private int cod;
    private String nume;
    private float media;

    public int getCod() {
        return cod;
    }

    public String getNume() {
        return nume;
    }

    public float getMedia() {
        return media;
    }

    public Canditat(int cod, String nume, float media) {
        this.cod = cod;
        this.nume = nume;
        this.media = media;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Canditat{");
        sb.append("cod=").append(cod);
        sb.append(", nume='").append(nume).append('\'');
        sb.append(", media=").append(media);
        sb.append('}');
        return sb.toString();
    }
}
