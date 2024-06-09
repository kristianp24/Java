public class Imprumuturi {
    private String cota;
    private String numeCititor;
    private  int nrZileImprumut;

    public String getCota() {
        return cota;
    }

    public String getNumeCititor() {
        return numeCititor;
    }

    public int getNrZileImprumut() {
        return nrZileImprumut;
    }

    public Imprumuturi(String cota, String numeCititor, int nrZileImprumut) {
        this.cota = cota;
        this.numeCititor = numeCititor;
        this.nrZileImprumut = nrZileImprumut;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Imprumuturi{");
        sb.append("cota='").append(cota).append('\'');
        sb.append(", numeCititor='").append(numeCititor).append('\'');
        sb.append(", nrZileImprumut=").append(nrZileImprumut);
        sb.append('}');
        return sb.toString();
    }
}
