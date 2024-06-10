import java.util.Arrays;

public class Liceu {
    private int codLiceu;
    private String nume;
    private int nrSpecializari;

    private int[] coduriSpecializari;
    private int[] nrlocuriSpecializare;

    public int getCodLiceu() {
        return codLiceu;
    }

    public String getNume() {
        return nume;
    }

    public int getNrSpecializari() {
        return nrSpecializari;
    }

    public int[] getCoduriSpecializari() {
        return coduriSpecializari;
    }

    public int[] getNrlocuriSpecializare() {
        return nrlocuriSpecializare;
    }

    public Liceu(int codLiceu, String nume, int nrSpecializari, int[] coduriSpecializari, int[] nrlocuriSpecializare) {
        this.codLiceu = codLiceu;
        this.nume = nume;
        this.nrSpecializari = nrSpecializari;
        this.coduriSpecializari = coduriSpecializari;
        this.nrlocuriSpecializare = nrlocuriSpecializare;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Liceu{");
        sb.append("codLiceu=").append(codLiceu);
        sb.append(", nume='").append(nume).append('\'');
        sb.append(", nrSpecializari=").append(nrSpecializari);
        sb.append(", coduriSpecializari=").append(Arrays.toString(coduriSpecializari));
        sb.append(", nrlocuriSpecializare=").append(Arrays.toString(nrlocuriSpecializare));
        sb.append('}');
        return sb.toString();
    }
}
