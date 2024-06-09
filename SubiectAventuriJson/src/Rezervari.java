public class Rezervari {
    private String id_rezervare;
    private int cod;
    private int locuriRezervate;

    public String getId_rezervare() {
        return id_rezervare;
    }

    public void setId_rezervare(String id_rezervare) {
        this.id_rezervare = id_rezervare;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getLocuriRezervate() {
        return locuriRezervate;
    }

    public void setLocuriRezervate(int locuriRezervate) {
        this.locuriRezervate = locuriRezervate;
    }

    public Rezervari(String id_rezervare, int cod, int locuriRezervate) {
        this.id_rezervare = id_rezervare;
        this.cod = cod;
        this.locuriRezervate = locuriRezervate;
    }

    public Rezervari() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rezervari{");
        sb.append("id_rezervare='").append(id_rezervare).append('\'');
        sb.append(", cod=").append(cod);
        sb.append(", locuriRezervate=").append(locuriRezervate);
        sb.append('}');
        return sb.toString();
    }
}
