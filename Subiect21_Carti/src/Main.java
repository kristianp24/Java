import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Main {
    public static List<Carte> listCarti = new ArrayList<>();
    public static List<Imprumuturi> listaImprumutri = new ArrayList<>();
     public static void main(String[] args){
//        1. (2p) Să se citească cartile din fisierul text si sa se tipareasca la consola cartile publicate
//        inainte de 1940, ordonate alfabetic dupa titlul cartii, în formatul:
        System.out.println("----------CERINTA 1-----------");
        citireFisier();
        listCarti.stream().filter(carte -> carte.getAnul() < 1940).sorted(Comparator.comparing(Carte::getTitlu)).collect(Collectors.toList()).stream().forEach(System.out::println);
        System.out.println("----------CERINTA 2-----------");
        Connection connection = null ;
        try {
            Class.forName("org.sqlite.JDBC");
             connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\HP\\IdeaProjects\\Subiect21_Carti\\DateIN\\biblioteca.db");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            citireBd(connection);
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
//         2. (2p) Să se citească răspunsurile din baza de date și să se afișeze la consola lista cititorilor
//         care au imprumutat carti, in formatul:
         listCarti.stream().forEach(carte -> {
            // System.out.println(carte.getCota());
             listaImprumutri.stream().filter(imprumuturi -> imprumuturi.getCota().equals(carte.getCota())).distinct().forEach(imprumuturi -> {
                 System.out.println(imprumuturi.getNumeCititor());
             });
         });

//
//         3. (3p) Să se genereze un fișier text care să conțină lista de cititori ordonată descrescător
//         în funcție de numarul total de zile de imprumut a cartilor:
         System.out.println("-------CERINTA 3------------");
         Map<Integer,String> cititori = new HashMap<>();
         listaImprumutri.stream().collect(Collectors.groupingBy(imprumuturi -> imprumuturi.getNumeCititor())).forEach((nume,imprumut)->{
             var nrZile = listaImprumutri.stream().filter(imprumuturi -> imprumuturi.getNumeCititor().equals(nume)).mapToInt(Imprumuturi::getNrZileImprumut).sum();
            // System.out.println("Nume:"+nume+" nr zile:"+nrZile);
             cititori.put((int)nrZile,nume);
         });
         cititori.keySet().stream().sorted(Comparator.reverseOrder()).forEach(key -> {
            System.out.println("Nr zile:"+key+" Cititorul:"+cititori.get(key));
         });

         System.out.println("-------CERINTA 4------------ ");
         Thread serverThread = new Thread(new Runnable() {
             @Override
             public void run() {
                 System.out.println("Server has started");
                 try(ServerSocket serverSocket = new ServerSocket(7777);){
                     //Pornire server si preluare mesaj
                     Socket socket = serverSocket.accept();
                     InputStream inputStream = socket.getInputStream();
                     DataInputStream dataInputStream = new DataInputStream(inputStream);
                     String messageRecieved = dataInputStream.readUTF();

                     //Procesare date
                     final Integer[] anul = {0};
                     final String[] titlu = {""};
                     final String[] autorul = {""};
                     listCarti.stream().filter(carte -> carte.getCota().equals(messageRecieved)).forEach((carte)->{
                         anul[0] = carte.getAnul();
                         titlu[0] = carte.getTitlu();
                         autorul[0] = carte.getAutor();

                     });
                     String an = anul[0].toString();
                     String messageToBeSent = autorul[0]+","+titlu[0]+","+an;

                     //Trimitere info catre client
                     OutputStream outputStream = socket.getOutputStream();
                     DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                     dataOutputStream.writeUTF(messageToBeSent);

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
                 String message;
                 try(Socket socket = new Socket("localhost",7777);){
                     //Preluare si trimitere mesaj catre server
                     System.out.print("Cota cartei:");
                    message= scanner.nextLine();
                     OutputStream outputStream = socket.getOutputStream();
                     DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                     dataOutputStream.writeUTF(message);

                     //Primire raspuns de la server
                     InputStream inputStream = socket.getInputStream();
                     DataInputStream dataInputStream = new DataInputStream(inputStream);
                     String recievedAnswer = dataInputStream.readUTF();
                     System.out.printf("Autorul:%s, Cartea:%s, Anul:%d",recievedAnswer.split(",")[0],
                                               recievedAnswer.split(",")[1],Integer.parseInt(recievedAnswer.split(",")[2]));

                 }
                 catch (Exception e){
                     e.printStackTrace();
                 }
             }
         });

         serverThread.start();;
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

    public static void citireBd(Connection connection){
        String sqlStatement = "SELECT * from Imprumuturi";

        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while(rs.next()){
                String nume = rs.getString(1);
                String cota = rs.getString(2);
                int nr = rs.getInt(3);
                listaImprumutri.add(new Imprumuturi(cota,nume,nr));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static  void citireFisier(){
        try(FileInputStream fisier = new FileInputStream("C:\\Users\\HP\\IdeaProjects\\Subiect21_Carti\\DateIN\\carti.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fisier);
            BufferedReader reader = new BufferedReader(inputStreamReader);){

            while (reader.ready()){
                String linie = reader.readLine();
                String cota = linie.split("\t")[0];
                String numeCarte = linie.split("\t")[1];
                String numeAutor = linie.split("\t")[2];
                int an = Integer.parseInt(linie.split("\t")[3]);
                listCarti.add(new Carte(cota,numeCarte,numeAutor,an));
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
