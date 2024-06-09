import org.json.JSONArray;
import org.json.JSONTokener;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Main {
    public  static List<Produs> listaProduse = new ArrayList<>();
    public static List<Tranzactie> listaTranzacti = new ArrayList<>();
    public static void main(String[] args) throws Exception {
//        1) Să se afișeze la consolă numărul de produse
        System.out.println("--------CERINTA 1------------");
        citireXML();
       long nrProduse =  listaProduse.stream().count();
       System.out.println("Numarul de produse = "+nrProduse);

//        2) Să se afișeze la consolă lista de produse ordonate alfabetic.
        System.out.println("---------CERINTA 2------------");
        listaProduse.stream().sorted(Comparator.comparing(Produs::getDenumire)).forEach(System.out::println);

//        3) Să se scrie în fișierul text date\subiect1\lista.txt un raport de forma:
//        Denumire Produs, Numar tranzactii
//
//        Produsele trebuie să fie ordonate în ordinea descrescătoare
//        a numărului de tranzacții.
        System.out.println("----------CERINTA 3-------------");
        citireJSON();

        try(FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\HP\\IdeaProjects\\SubiectProduse\\DateIN\\lista.txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);){

            listaProduse.stream().collect(Collectors.groupingBy(Produs::getCod)).forEach((cod,listaProduse)->{
                Long nrTranzacti = listaTranzacti.stream().filter(tranzactie -> tranzactie.getCod() == cod).count();
                //System.out.println("Denumire:"+listaProduse.get(0).getDenumire()+". Nr tranzacti:"+nrTranzacti);
                try {
                    writer.write(listaProduse.get(0).getDenumire()+","+nrTranzacti.toString());
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Fisierul creat!!");
        }

       // 4) Să se afișeze la consolă valoarea totală a stocurilor.
        System.out.println("-----------CERINTA 4-------------");
        Float[] stoc = {0.0f};
       listaProduse.stream().forEach(produs -> {

           listaTranzacti.stream().filter(tranzactie -> tranzactie.getCod() == produs.getCod()).forEach(tranzactie->{
               if(tranzactie.getTip().equals("intrare"))
                   stoc[0] = stoc[0] +(tranzactie.getCantitate()*produs.getPret());
               else
                   stoc[0] = stoc[0] - (tranzactie.getCantitate()*produs.getPret());
           });
       });
       System.out.println("Valoarea stocurilor = "+stoc[0]);

//        5) Să se implementeze funcționalitățile de server și client TCP/IP și să se
//        execute următorul scenariu: componenta client trimite serverului un cod de produs
//        iar componenta server va întoarce clientului valoarea stocului corespunzător.
        System.out.println("-----------CERINTA 5-------------");
        final int port = 5555;
        Thread serverThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try(ServerSocket serverSocket = new ServerSocket(port);){
                    System.out.println("[SERVER] Server pornit in portul "+port);
                    Socket socket =serverSocket.accept();
                    //Preluare mesaj de la client
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String messageRecieved = dataInputStream.readUTF();

                    //Procesare date
                    int codProdus = Integer.parseInt(messageRecieved);
                    Float[] stoculReturnat = {0.0f};
                    listaProduse.stream().filter(produs -> produs.getCod() == codProdus).forEach(produs -> {
                        listaTranzacti.stream().filter(tranzactie -> tranzactie.getCod()==codProdus).forEach(tranzactie->{
                            if(tranzactie.getTip().equals("intrare"))
                                stoculReturnat[0] = stoculReturnat[0] + (tranzactie.getCantitate()*produs.getPret());
                            else
                                stoculReturnat[0] = stoculReturnat[0] - (tranzactie.getCantitate()*produs.getPret());

                        });

                    });

                    //Trimitere raspuns catre client
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(stoculReturnat[0].toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                String messageToBeSent =null;
                try(Socket socket = new Socket("localhost",port);){
                    System.out.print("Codul produsului:");
                   messageToBeSent= scanner.nextLine();
                   OutputStream outputStream = socket.getOutputStream();
                   DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                   dataOutputStream.writeUTF(messageToBeSent);

                   //Preluare raspuns de la server
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String raspuns = dataInputStream.readUTF();
                    System.out.println("Valoare totala a stocului:"+raspuns);

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        serverThread.start();
        sleep(1500);
        clientThread.start();
        serverThread.join();
        clientThread.join();

    }

    public static void citireJSON(){
        try(var fisier =new FileReader("C:\\Users\\HP\\IdeaProjects\\SubiectProduse\\DateIN\\tranzactii.json");){
            var tokener = new JSONTokener(fisier);
            var jsonArray = new JSONArray(tokener);

            for(int i=0;i<jsonArray.length();i++){
                var object = jsonArray.getJSONObject(i);
                int cod = object.getInt("codProdus");
                int cantitate = object.getInt("cantitate");
                String denumire = object.getString("tip");
                listaTranzacti.add(new Tranzactie(cod,cantitate,denumire));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public  static  void citireXML() throws Exception {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var document = builder.parse("C:\\Users\\HP\\IdeaProjects\\SubiectProduse\\DateIN\\produse.xml");
        var root = document.getDocumentElement();

        NodeList noduriProduse = root.getElementsByTagName("produs");
        for(int i=0;i< noduriProduse.getLength();i++){
            var nodProdus = (Element)noduriProduse.item(i);
            int cod = Integer.parseInt(nodProdus.getElementsByTagName("cod").item(0).getTextContent());
            String denumire = nodProdus.getElementsByTagName("denumire").item(0).getTextContent();
            float pret = Float.parseFloat(nodProdus.getElementsByTagName("pret").item(0).getTextContent());
            listaProduse.add(new Produs(cod,denumire,pret));
        }
    }
}
