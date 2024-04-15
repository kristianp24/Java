import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainFactura {

        public static List<Factura> generareListaFacturi(LocalDate dataMinima, int nrFacturi){
            Random rand = new Random();
            List<Factura> facturi = new ArrayList<>();
            List<String> clienti = new ArrayList<>();
            clienti.add("Client 1"); clienti.add("Client 2"); clienti.add("Client 3");
            List<Factura.Linie> liniFactura = new ArrayList<>();
            liniFactura.add(new Factura.Linie("Avion",20000.33,2));
            liniFactura.add(new Factura.Linie("Masina",5200.55,20));
            liniFactura.add(new Factura.Linie("AC",4200,30));
            liniFactura.add(new Factura.Linie("Bicicleta",700,22));

            for(int i=0;i<nrFacturi;i++){
                int pozitie = rand.nextInt(3);
                //List<Linie> linii = new ArrayList<>();
                String nume = clienti.get(pozitie);
                facturi.add(new Factura(nume,dataMinima,liniFactura));
            }

            return facturi;
        }

//        2) Să se definească o funcție salvareFacturi pentru salvarea unei liste de facturi într-un fișier binar.
//    Fișierul va fi compus din înregistrări de forma:
//
//    denumire client - string
//    an / luna / zi - întregi
//    număr linii - întreg
//            (produs - string, preț - double, cantitate - întreg) x număr linii
//    EOF
    public static void salvareFacturi(List<Factura> facturi, FileOutputStream f){
            try(DataOutputStream write = new DataOutputStream(f)){
                for(int i=0;i<facturi.size();i++)
                {
                    write.writeUTF(facturi.get(i).getDenumireClient());
                    write.write('\n');
                    write.writeInt(facturi.get(i).getDataEmitere().getYear());
                    write.writeInt(facturi.get(i).getDataEmitere().getMonthValue());
                    write.writeInt(facturi.get(i).getDataEmitere().getDayOfMonth());
                    write.write('\n');
                    write.writeInt(facturi.get(i).getLinii().size());
                    write.write('\n');
                    for(int j=0;j<facturi.get(i).getLinii().size();j++){
                        write.writeUTF(facturi.get(i).getLinii().get(j).getProdus());
                        write.writeDouble(facturi.get(i).getLinii().get(j).getPret());
                        write.writeInt(facturi.get(i).getLinii().get(j).getCantitate());
                    }
                    write.write('\n');

                }
                //write.close();
                System.out.println("Datele au fost salvate!");
            }
            catch(IOException e){
                System.out.print("Ceva gresit");
            }

    }

   // 3) Să se scrie o funcție incarcareFacturi care să citească o listă de facturi dintr-un fișier binar în formatul de mai sus.
  public static List<Factura> incarcareFacturi(FileInputStream f){
            List<Factura> facture = new ArrayList<>();
            try(DataInputStream read = new DataInputStream(f))
            {
               while(true)
               {
                   List<Factura.Linie> linii = new ArrayList<>();

                   String nume = null;
                   nume = read.readUTF();
                   read.read();
                   int an = read.readInt();
                   int luna = read.readInt();
                   int ziua = read.readInt();
                   LocalDate data = LocalDate.of(an,luna,ziua);
                   read.read();
                   int nrLinii=read.readInt();
                   read.read();
                   for(int i=0;i<nrLinii;i++){
                       String produs = null;
                       produs= read.readUTF();
                       double pret = read.readDouble();
                       int cantitate = read.readInt();
                       linii.add(new Factura.Linie(produs,pret,cantitate));
                   }
                   read.read();
                   facture.add(new Factura(nume,data,linii));

               }

            }
            catch(IOException e){
               // System.out.println(e.getMessage());
            }
            return facture;
  }




    public static void main (String args[]) {
            List<Factura> facture = new ArrayList<>();
            facture = generareListaFacturi(LocalDate.now(),5);

            for (var f:facture)
                System.out.println(f.toString());

            List<Factura> factureNoua = new ArrayList<>();

        try {
            FileOutputStream fisier = new FileOutputStream("date.bin");
            salvareFacturi(facture,fisier);
            fisier.close();
            FileInputStream f = new FileInputStream("date.bin");
            factureNoua = incarcareFacturi(f);
            f.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (var f:factureNoua)
            System.out.println(f.toString());


    }



}
