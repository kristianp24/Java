import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerError;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Main {
    public static List<Santier> listaSantiere = new ArrayList<>();
    public  static List<Capitol> listaCapitole = new ArrayList<>();
    public static void main(String [] args){
//        1) Să afișeze la consolă santierele si valoarea medie estimata a acestora
//                (media valorilor estimate prin autorizatiile de constructie).
        System.out.println("-------CERINTA 1---------");
        citireJSON();
        listaSantiere.stream().forEach(System.out::println);
        OptionalDouble average = listaSantiere.stream().mapToDouble(Santier::getValoare).average();
        System.out.println("Valoarea medie:"+average.toString());

//        Să afișeze la consolă cantitatile totale pentru fiecare capitol, astfel:
//        cod capitol,cantitate
        System.out.println("----------CERINTA 2------------");
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
             connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\HP\\IdeaProjects\\Subiect25\\DateIN\\devize.db");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            citireBD(connection);
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
       // listaCapitole.stream().forEach(System.out::println);
        listaCapitole.stream().collect(Collectors.groupingBy(capitol -> capitol.getCodCapitol())).forEach((cod,capitol) ->{
            var cantitate = listaCapitole.stream().filter(capitol1 -> cod == capitol1.getCodCapitol()).mapToDouble(Capitol::getCantitate).sum();
            System.out.println("Capitol :"+cod+" are cantitatea totala: "+cantitate);
        });


        System.out.println("---------CERINTA 3------------");
//        Să salveze în fișierul devize.txt o situație a cheltuielilor la nivel de capitole,
//                cu sumarizare pe santiere, astfel:
//
//        cod capitol
//        cod santier,valoare
//        cod santier,valoare
//...
//        cod capitol
//        cod santier,valoare
//        cod santier,valoare
//...
//
//        unde 'valoare' se calculeaza prin inmultirea cantitatii cu pretul unitar.

        try(FileOutputStream fisier = new FileOutputStream("C:\\Users\\HP\\IdeaProjects\\Subiect25\\DateIN\\devize.txt");
            OutputStreamWriter inputStreamReader = new OutputStreamWriter(fisier);
            BufferedWriter writer = new BufferedWriter(inputStreamReader);){

            listaCapitole.stream().collect(Collectors.groupingBy(capitol -> capitol.getCodCapitol())).forEach((cod,capitols)->{
               try {
                   writer.write(String.valueOf(cod));
                   writer.newLine();
                   ;
                   for (Capitol c : capitols) {
                       var valoare = listaCapitole.stream().filter(capitol -> capitol.getCodSantier() == c.getCodSantier()).mapToDouble(capitolul -> capitolul.getCantitate() * capitolul.getPretUnitar()).sum();
                      // System.out.println("Cod santier:" + c.getCodSantier() + ",valoare:" + valoare);
                       writer.write(String.valueOf(c.getCodSantier()));
                       writer.write(",");
                       writer.write(String.valueOf(valoare));
                       writer.newLine();
                   }
               }
               catch (Exception e){
                   e.printStackTrace();
               }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Fisierul creat!");
        }

        System.out.println("----------CERINTA 4--------------");
        final int port = 7777;
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try(ServerSocket serverSocket  =new ServerSocket(port);){
                    System.out.println("Server started on port "+port);
                    Socket socket = serverSocket.accept();
                    //Preluare mesaj de la client
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String recievedMessage = dataInputStream.readUTF();

                    //Procesare date care trebuie trimise
                    int cod = Integer.parseInt(recievedMessage);
                    String obiectiv = null;
                    Double valoare = 0.0;
                    for(Santier s:listaSantiere){
                        if(s.getCodSantier()==cod){
                            obiectiv = s.getObiectiv();
                            valoare  =s.getValoare();
                        }
                    }
                    String answerFromServer = obiectiv+","+valoare.toString();

                    //Trimitere raspuns la client
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(answerFromServer);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                String messageToBeSent = null;
                try(Socket socket = new Socket("localhost",7777);)
                {
                    //Trimitere mesaj catre server;
                    System.out.println("Codul santierului:");
                    messageToBeSent = scanner.nextLine();
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream dataOutputStream= new DataOutputStream(outputStream);
                    dataOutputStream.writeUTF(messageToBeSent);

                    //Primire raspuns de la server
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    String answerRecieved = dataInputStream.readUTF();
                    System.out.println("Obiectivul:"+answerRecieved.split(",")[0]+", Valoarea:"+answerRecieved.split(",")[1]);

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


    public static void citireBD(Connection connection){
        String sqlQuery  ="SELECT * FROM Capitole";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);
            while(rs.next()){
                int codCpitol = rs.getInt(1);
                int codSantier = rs.getInt(2);
                String denumireChel = rs.getString(3);
                String um = rs.getString(4);
                float cantitat = rs.getFloat(5);
                float punitar = rs.getFloat(6);
                listaCapitole.add(new Capitol(codCpitol,codSantier,denumireChel,um,cantitat,punitar));
            }
//            1,1,Fier beton,kg,13000.0,0.8
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ;
    }

    public static void citireJSON(){
        try(var fisier = new FileReader("C:\\Users\\HP\\IdeaProjects\\Subiect25\\DateIN\\santiere.json");){
            var tokener = new JSONTokener(fisier);
            var jsonArray  =new JSONArray(tokener);

            for(int i=0;i< jsonArray.length();i++){
                var object = jsonArray.getJSONObject(i);
                int cod = object.getInt("Cod Santier");
                String localitate = object.getString("Localitate");
                String strada = object.getString("Strada");
                String obiecctiv = object.getString("Obiectiv");
                double val = object.getDouble("Valoare");
                listaSantiere.add(new Santier(cod,localitate,strada,obiecctiv,val));

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
