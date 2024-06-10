import org.json.JSONArray;
import org.json.JSONTokener;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Main {
    public static List<Intretinere> intretinereList = new ArrayList<>();
    public  static List<Penalizare> penalizareList = new ArrayList<>();
    public static List<Intretinere> apartamente = new ArrayList<>();
    public static void main(String[] args){
//        1) Să se afișeze la consolă valoarea întreținerii (fără penalizări) pentru întreg imobilul în formatul:
//        CERINTA 1: total intretinere 1999.12 lei
        citireFisier();
        System.out.println("--------CERINTA 1----------");
        var sum = intretinereList.stream().mapToDouble(Intretinere::getValoarePlata).sum();
        System.out.println("Valoare totala:"+sum);

//        2) Să se afișeze la consolă lista de apartamente ordonate descrescător în funcție de suma de plată.
////        Pentru fiecare apartament se va afișa suma de plată, numărul și numele în formatul:
////
////        CERINTA 2:
////        364.5  1 Popescu
////        200.2  3 Testescu
////        164.5  2 Enescu
        System.out.println("----------CERINTA 2-----------");
        intretinereList.stream().sorted(Comparator.comparing(Intretinere::getValoarePlata).reversed()).forEachOrdered(intretinere -> {
            System.out.println(intretinere.getValoarePlata()+" "+intretinere.getNrApartament()+" "+intretinere.getNumePropietar());
        });

        //        3) Să se afișeze la consolă lista apartementelor care au penalizări.
//        Pentru fiecare apartament se va afișa suma finala (suma inițială + penalizare), numărul și numele în formatul:
//
//        CERINTA 3:
//        369.50  1 Popescu
//        205.52  3 Testescu
        System.out.println("----------CERINTA  3-----------");
        citireJSON();
        penalizareList.stream().forEach(penalizare -> {
            intretinereList.stream().filter(intretinere -> intretinere.getNrApartament() == penalizare.getNrApartament()).forEach(intretinere -> {
                System.out.println(intretinere.getValoarePlata()+" "+intretinere.getNrApartament()+" "+intretinere.getNumePropietar());
            });
        });

        try {
            scriereXML();

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server TCP/IP");
        try {
            citireXML();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final int port = 5555;
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try(ServerSocket serverSocket = new ServerSocket(port);){
                    System.out.println("[SERVER] Server pornit in portul "+port);
                    Socket socket = serverSocket.accept();
                    //Preluare mesaj de la client
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String messageRecieved = dataInputStream.readUTF();;

                    //Procesare date
                    int codApartament = Integer.parseInt(messageRecieved);
                    String messageToBeSent = "Nu exista apartament";
                    for(Intretinere i:apartamente){
                        if(i.getNrApartament() == codApartament){
                            messageToBeSent = i.getNumePropietar();
                            break;
                        }

                    }

                    //Trimitere raspuns
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(messageToBeSent);


                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        Scanner scanner = new Scanner(System.in);

        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                try(Socket socket = new Socket("localhost",port);){
                    //Trimitere mesaj
                    System.out.print("Id apartament:");
                    message = scanner.nextLine();
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(message);

                    //Preluare raspuns
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String messageRecieved = dataInputStream.readUTF();
                    System.out.println("Numele propietarului:"+messageRecieved);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        serverThread.start();
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        clientThread.start();
        try {
            serverThread.join();
            clientThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public static void citireXML() throws Exception {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var document = builder.parse("C:\\Users\\HP\\IdeaProjects\\SubiectExamen_Penalizari\\DateIN\\apartamente.xml");
        var root = document.getDocumentElement();

        var noduri = root.getElementsByTagName("Apartament");
        for(int i=0;i< noduri.getLength();i++){
            var nodAp = (Element)noduri.item(i);
            int cod = Integer.parseInt(nodAp.getElementsByTagName("Cod").item(0).getTextContent());
            String propr = nodAp.getElementsByTagName("Propietar").item(0).getTextContent();
            apartamente.add(new Intretinere(cod,propr,0));
        }


    }

    public static void citireJSON(){
        try(var fisier = new FileReader("C:\\Users\\HP\\IdeaProjects\\SubiectExamen_Penalizari\\DateIN\\penalizari.json");){
            var tokener = new JSONTokener(fisier);
            var jsonArray = new JSONArray(tokener);

            for(int i=0;i< jsonArray.length();i++){
                var object = jsonArray.getJSONObject(i);
                penalizareList.add(new Penalizare(object.getInt("numar_apartament"), object.getFloat("penalizare") ));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void citireFisier(){
        try(FileInputStream fisier = new FileInputStream("C:\\Users\\HP\\IdeaProjects\\SubiectExamen_Penalizari\\DateIN\\intretinere_apartamente.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fisier);
            BufferedReader reader = new BufferedReader(inputStreamReader);){

            while (reader.ready()){
                String linie = reader.readLine();
                int cod = Integer.parseInt(linie.split(",")[0]);
                String nume = linie.split(",")[1];
                double val = Double.parseDouble(linie.split(",")[2]);
                intretinereList.add(new Intretinere(cod,nume,val));

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Cerinta +
    public static void scriereXML() throws ParserConfigurationException, TransformerException, FileNotFoundException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var document = builder.newDocument();

        var root = document.createElement("Apartamente");
        document.appendChild(root);

        for(Intretinere i:intretinereList){
            var house = document.createElement("Apartament");
            root.appendChild(house);

            var cod = document.createElement("Cod");
            cod.setTextContent(String.valueOf(i.getNrApartament()));
            house.appendChild(cod);

            var propietar = document.createElement("Propietar");
            propietar.setTextContent(i.getNumePropietar());
            house.appendChild(propietar);
        }

        var transformerFactory = TransformerFactory.newInstance();
        var transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT,"yes");
        var fisier = new FileOutputStream("C:\\Users\\HP\\IdeaProjects\\SubiectExamen_Penalizari\\DateIN\\apartamente.xml");
        transformer.transform(new DOMSource(document), new StreamResult(fisier));

    }
}
