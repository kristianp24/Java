import java.util.List;

public class Canditat {
    private int codCanditat;
    private String numeCanditat;
    private float media;
    private List<Optiune> listaOptiuni;

    public int getCodCanditat() {
        return codCanditat;
    }

    public String getNumeCanditat() {
        return numeCanditat;
    }

    public float getMedia() {
        return media;
    }

    public List<Optiune> getListaOptiuni() {
        return listaOptiuni;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Canditat{");
        sb.append("codCanditat=").append(codCanditat);
        sb.append(", numeCanditat='").append(numeCanditat).append('\'');
        sb.append(", media=").append(media);
        sb.append(", listaOptiuni=").append(listaOptiuni);
        sb.append('}');
        return sb.toString();
    }

    public Canditat(int codCanditat, String numeCanditat, float media, List<Optiune> listaOptiuni) {
        this.codCanditat = codCanditat;
        this.numeCanditat = numeCanditat;
        this.media = media;
        this.listaOptiuni = listaOptiuni;
    }
}
