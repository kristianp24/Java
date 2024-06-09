public class Tranzactie {
    private int cod;
    private  int cantitate;
    private  String tip;

    public int getCod() {
        return cod;
    }

    public int getCantitate() {
        return cantitate;
    }

    public String getTip() {
        return tip;
    }

    public Tranzactie(int cod, int cantitate, String tip) {
        this.cod = cod;
        this.cantitate = cantitate;
        this.tip = tip;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tranzactie{");
        sb.append("cod=").append(cod);
        sb.append(", cantitate=").append(cantitate);
        sb.append(", tip='").append(tip).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
