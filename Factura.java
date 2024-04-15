import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Factura {

    private String denumireClient;
    private LocalDate dataEmitere;
    private List<Linie> linii = new ArrayList<>();

    public String getDenumireClient() {
        return denumireClient;
    }

    public LocalDate getDataEmitere() {
        return dataEmitere;
    }

    public List<Linie> getLinii() {
        return linii;
    }

    public Factura(String denumireClient, LocalDate dataEmitere, List<Linie> linii) {
        this.denumireClient = denumireClient;
        this.dataEmitere = dataEmitere;
        this.linii = linii;
    }
    public Factura(){

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Factura{");
        sb.append("denumireClient='").append(denumireClient).append('\'');
        sb.append(", dataEmitere=").append(dataEmitere);
        sb.append(", linii=").append(linii);
        sb.append('}');
        return sb.toString();
    }

    public void adaugaLinie(Linie linie){
        this.linii.add(linie);
    }

    //1) Să se definească o funcție generareListaFacturi care să primească dată minimă și un număr n de facturi și care:
//    să genereze o lista de n obiecte factură cu 1-10 linii
//    datele (clienți, produse, preturi) din liste fixe


    static class Linie{
        private String produs;
        private double pret;
        private int cantitate;

        public String getProdus() {
            return produs;
        }

        public double getPret() {
            return pret;
        }

        public int getCantitate() {
            return cantitate;
        }

        public Linie(String produs, double pret, int cantitate) {
            this.produs = produs;
            this.pret = pret;
            this.cantitate = cantitate;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Linie{");
            sb.append("produs='").append(produs).append('\'');
            sb.append(", pret=").append(pret);
            sb.append(", cantitate=").append(cantitate);
            sb.append('}');
            return sb.toString();
        }
    }





}
