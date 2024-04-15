import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Achizitie implements Comparable,Serializable {

    private String codul;
    private int anul;
    private int luna;
    private int ziua;
    private int cantitate;
    private float pret;

    public String getCodul() {
        return codul;
    }

    public int getAnul() {
        return anul;
    }

    public int getLuna() {
        return luna;
    }

    public int getZiua() {
        return ziua;
    }

    public int getCantitate() {
        return cantitate;
    }

    public float getPret() {
        return pret;
    }

    public Achizitie(String codul, int anul, int luna, int ziua, int cantitate, float pret) {
        this.codul=codul;
        this.anul = anul;
        this.luna = luna;
        this.ziua = ziua;
        this.cantitate = cantitate;
        this.pret = pret;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Achizitie{");
        sb.append("codul=").append(codul);
        sb.append(", anul=").append(anul);
        sb.append(", luna=").append(luna);
        sb.append(", ziua=").append(ziua);
        sb.append(", cantitate=").append(cantitate);
        sb.append(", pret=").append(pret);
        sb.append('}');
        return sb.toString();
    }

    public float valoare(){
        return pret*cantitate;
    }


    @Override
    public int compareTo(Object o) {
        Achizitie a = (Achizitie) o;
        if (this.valoare() > a.valoare())
            return -1;
        else if(this.valoare()<a.valoare())
            return 1;
        else
           return 0;
    }



    public static void main(String[] args)
    {
        List<Achizitie> achizitieList = new ArrayList<>();

        try(FileInputStream f = new FileInputStream("achizitii.txt")){
            InputStreamReader streamReader = new InputStreamReader(f);
            BufferedReader reader = new BufferedReader(streamReader);

            while(reader.ready()){
                String linie = reader.readLine();
                String codul = linie.split(",")[0];
                int anul = Integer.parseInt(linie.split(",")[1]);
                int luna = Integer.parseInt(linie.split(",")[2]);
                int ziua = Integer.parseInt(linie.split(",")[3]);
                int cantitate = Integer.parseInt(linie.split(",")[4]);
                float pret = (float) Double.parseDouble(linie.split(",")[5]);
                achizitieList.add(new Achizitie(codul,anul,luna,ziua,cantitate,pret));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        for(var a :achizitieList)
              System.out.println(a.toString());

        System.out.printf("%-40s\t%-40s\t%-40s\n","Produsul","Numar achizitii","Valoarea");
        achizitieList.stream().filter(luna -> luna.getLuna() == 2).collect(Collectors.groupingBy(produsId -> produsId.getCodul())).forEach((prod,lista)->{
            int nrApr = (int)lista.size();
            float valoare = 0;
            for(int i=0;i<lista.size();i++){
                valoare += lista.get(i).valoare();
            }
            System.out.printf("%-40s\t%-40d\t%-405.2f\n",prod,nrApr,valoare);
        });

        try(FileOutputStream f = new FileOutputStream("achizitiFrecv.bin");
            DataOutputStream writer = new DataOutputStream(f);){


            achizitieList.stream().filter(luna -> luna.getLuna() == 2).collect(Collectors.groupingBy(produsId -> produsId.getCodul())).forEach((prod,lista)->{
                int nrAchizitii = (int)lista.size();

                if(nrAchizitii > 1){
                    try {
                        writer.writeInt(nrAchizitii);
                        for(int i=0;i<nrAchizitii;i++){
                            writer.writeUTF(lista.get(i).codul);
                            writer.writeInt(lista.get(i).anul);
                            writer.writeInt(lista.get(i).luna);
                            writer.writeInt(lista.get(i).ziua);
                            writer.writeInt(lista.get(i).cantitate);
                            writer.writeFloat(lista.get(i).pret);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }



            });

        }catch(IOException e){

        }


        List<Achizitie> achitiFrecv = new ArrayList<>();
        try (FileInputStream fI = new FileInputStream("achizitiFrecv.bin");
             DataInputStream reader = new DataInputStream(fI);){
           int nrObjecte =  reader.readInt();
           for(int i=0;i<nrObjecte;i++){
               String cod = reader.readUTF();
               int an = reader.readInt();
               int luna = reader.readInt();
               int ziua = reader.readInt();
               int cant = reader.readInt();
               float pret = reader.readFloat();
               achitiFrecv.add(new Achizitie(cod,an,luna,ziua,cant,pret));
           }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

       for(var a :achitiFrecv)
           System.out.println(a.toString());
    }

}
