public class Carte {
    private String cota;
    private String titlu;
    private String autor;
    private int anul;

    public String getCota() {
        return cota;
    }

    public String getTitlu() {
        return titlu;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnul() {
        return anul;
    }

    public Carte(String cota, String titlu, String autor, int anul) {
        this.cota = cota;
        this.titlu = titlu;
        this.autor = autor;
        this.anul = anul;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Carte{");
        sb.append("cota='").append(cota).append('\'');
        sb.append(", titlu='").append(titlu).append('\'');
        sb.append(", autor='").append(autor).append('\'');
        sb.append(", anul=").append(anul);
        sb.append('}');
        return sb.toString();
    }
}
