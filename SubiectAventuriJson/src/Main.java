import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static List<Aventuri> listaAventure = new ArrayList<>();
    public static List<Rezervari> listaRezervari = new ArrayList<>();
    public static void main(String[] args) throws FileNotFoundException {
            System.out.println("CERINTA 1-----------------");
            citireJSON();
            listaAventure.stream().filter(aventuri -> aventuri.getLocuriDisp() >= 20).forEach(aventura->{
                System.out.println(aventura.toString());
            });

        System.out.println("CERINTA 2-----------------");
        citireRezervari();
        listaAventure.stream().forEach(aventuri -> {
            var locuriOcup = listaRezervari.stream().filter(rezervare-> rezervare.getCod() == aventuri.getCod()).mapToInt(rezervari -> rezervari.getLocuriRezervate()).sum();
            //var locuriTotal = listaAventure.stream().mapToInt(Aventuri::getLocuriDisp);
            var locuriFree = aventuri.getLocuriDisp() - locuriOcup;
            if(locuriFree >= 5)
                System.out.println("Cod:"+aventuri.getCod()+" Den:"+aventuri.getDenumire()+" Locuri disp:"+locuriFree);

        });

        System.out.println("--------CERINTA 3---------");
        try(FileOutputStream fisier = new FileOutputStream("C:\\Users\\HP\\IdeaProjects\\SubiectAventuriJson\\DateIN\\valori.txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fisier);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);){

            var lista = listaAventure.stream().sorted(Comparator.comparing(Aventuri::getDenumire)).collect(Collectors.toList());
            lista.forEach(aventura -> {
                var locuriOcup = listaRezervari.stream().filter(rezervare-> rezervare.getCod() == aventura.getCod()).mapToInt(rezervari -> rezervari.getLocuriRezervate()).sum();
                var venituri = locuriOcup* aventura.getTarif();
                try {
                    writer.write(aventura.getDenumire()+","+locuriOcup+","+venituri);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //System.out.println(aventura.getDenumire()+","+locuriOcup+","+venituri);
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //scriereJSON();
        final int port = 7777;
        Thread severThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try(ServerSocket serverSocket = new ServerSocket(port);){
                    System.out.println("Server started on port "+port);
                    Socket socket = serverSocket.accept();
                    //Preluare date de la client
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String messageRecieved = dataInputStream.readUTF();

                    //Procesare date si returnare rezultatul dorit catre client
                    Integer infoDeTrimis = -1;
                    for(Aventuri a:listaAventure){
                        if(a.getCod() == Integer.parseInt(messageRecieved)){
                            infoDeTrimis = a.getLocuriDisp();
                            break;
                        }
                    }

                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(infoDeTrimis.toString());
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
                String message  =  null;
                try(Socket socket = new Socket("localhost",7777);){
                    //Trimitere mesaj catre server
                    System.out.print("Codul aventurei:");
                    message = scanner.nextLine();
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(message);

                    //Primire raspuns de la server
                    InputStream inputStream  = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String recievedMessage = dataInputStream.readUTF();
                    if(recievedMessage.equals("-1"))
                        System.out.println("Aventura cu codul respectiv nu exista.");
                    else
                        System.out.println("Aventura are "+recievedMessage+" locuri disponibile");


                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        severThread.start();
        clientThread.start();
        try {
            severThread.join();
            clientThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public static void scriereJSON(){
        try(var fisier = new PrintWriter("C:\\Users\\HP\\IdeaProjects\\SubiectAventuriJson\\DateIN\\myjson.json");){
            JSONArray jsonArray = new JSONArray();

            listaRezervari.forEach(rezervari -> {
                JSONObject object = new JSONObject();
                object.put("cod",rezervari.getCod());
                object.put("id_rezervare",rezervari.getId_rezervare());
                object.put("nr_locuri_rez",rezervari.getLocuriRezervate());

                jsonArray.put(object);
            });

            fisier.write(jsonArray.toString(3));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void citireRezervari(){
        try(var fisier = new FileInputStream("C:\\Users\\HP\\IdeaProjects\\SubiectAventuriJson\\DateIN\\rezervari.txt");
            var inputReader = new InputStreamReader(fisier);
            var reader = new BufferedReader(inputReader);){

            while(reader.ready()){
                String linie = reader.readLine();
                String id = linie.split(",")[0];
                int codRez = Integer.parseInt(linie.split(",")[1]);
                int locuriRez = Integer.parseInt(linie.split(",")[2]);
                listaRezervari.add(new Rezervari(id,codRez,locuriRez));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void citireJSON(){
        try(var fisier = new FileReader("C:\\Users\\HP\\IdeaProjects\\SubiectAventuriJson\\DateIN\\aventuri.json")){
            var tokener = new JSONTokener(fisier);
            var aventuri = new JSONArray(tokener);

            for(int i=0;i< aventuri.length();i++){
                var aventura = aventuri.getJSONObject(i);
                int cod = aventura.getInt("cod_aventura");
                String den = aventura.getString("denumire");
                float tarif = aventura.getFloat("tarif");
                int locuri_disp = aventura.getInt("locuri_disponibile");
                listaAventure.add(new Aventuri(cod,den,tarif,locuri_disp));
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
