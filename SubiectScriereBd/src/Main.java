import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class Main {
    public static List<Canditat> listaCanditati = new ArrayList<>();
    public static List<Liceu> listaLicee = new ArrayList<>();
    public static void main(String[] args){
        System.out.println("----------CERINTA 1---------------");
        citireJSON();
        // 1. Să afișeze la consolă numărul de candidați cu medii mai mari sau egale cu 9
        listaCanditati.stream().filter(canditat -> canditat.getMedia()>=9).forEach(System.out::println);

        // 2. Să se afișeze lista liceelor sortată descrescător după numărul total de locuri.
// Pentru fiecare liceu se va afișa codul liceului, numele liceului și numărul total de locuri.
        citireFisier();
       // listaLicee.stream().forEach(System.out::println);
        System.out.println("-----------CERINTA 2------------");
        Map<Integer,Liceu> liceuMap  = new HashMap<>();
        listaLicee.stream().forEach(liceu -> {
            var sumaLocuri = Arrays.stream(liceu.getNrlocuriSpecializare()).sum();
            liceuMap.put(sumaLocuri,liceu);
        });
        liceuMap.keySet().stream().sorted(Comparator.reverseOrder()).forEachOrdered(liceu->{
            System.out.println("Nr locuri:"+liceu+" Liceu:"+liceuMap.get(liceu));
        });
        // 3. Să se listeze în fișierul jurnal.txt candidații ordonați descrescător după numărul de opțiuni (criteriul 1) iar în caz de egalitate după medie (criteriul 2).
// Pentru fiecare candidat se va scrie codul, numele, numărul de opțiuni și media de admitere.
        System.out.println("--------CERINTA 3------------");
     


      System.out.println("----------CERINTA 4--------------");
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\HP\\IdeaProjects\\SubiectScriereBd\\DateIN\\candidati.db");
            scriereBd(connection);
            connection.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            System.out.println("Datele salvate!");
        }

    }

    public static void scriereBd(Connection connection){
        String sqlCreate = "create table IF NOT EXISTS CANDIDATI (cod_candidat integer,nume_candidat text,medie double,numar_optiuni integer)";
        try {
            Statement statement  = connection.createStatement();
            statement.executeUpdate(sqlCreate);
           // connection.commit();
            for(Canditat c: listaCanditati){
                String sqlInsertWithParams = "insert into candidati values(?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertWithParams);
                preparedStatement.setInt(1,c.getCodCanditat());
                preparedStatement.setString(2,c.getNumeCanditat());
                preparedStatement.setDouble(3,c.getMedia());
                preparedStatement.setInt(4,c.getListaOptiuni().size());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                //connection.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static void citireFisier(){
        try(FileInputStream fisier = new FileInputStream("C:\\Users\\HP\\IdeaProjects\\SubiectScriereBd\\DateIN\\licee.txt");
            InputStreamReader streamReader = new InputStreamReader(fisier);
            BufferedReader reader = new BufferedReader(streamReader);){

            while (reader.ready()){
                String linie= reader.readLine();
                int codLiceu = Integer.parseInt(linie.split(",")[0]);
                String numeLiceu = linie.split(",")[1];
                int nrSpecializari = Integer.parseInt(linie.split(",")[2]);
                String linie2 = reader.readLine();
                int[] coduriSpecializari = new int[nrSpecializari];
                int[] locuriSpecializari = new int[nrSpecializari];
                int j=0;
                for(int i=0;i<nrSpecializari;i++){
                    if(j == nrSpecializari*2-2+1)
                        break;
                    coduriSpecializari[i] = Integer.parseInt(linie2.split(",")[j]);
                    j+=2;
                }
                int k=-1;
                for (int i=0;i<nrSpecializari;i++){
                    k+=2;
                    if(k == nrSpecializari*2)
                        break;
                    locuriSpecializari[i] = Integer.parseInt(linie2.split(",")[k]);

                }
                listaLicee.add(new Liceu(codLiceu,numeLiceu,nrSpecializari,coduriSpecializari,locuriSpecializari));

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void citireJSON(){
        try(var fisier = new FileReader("C:\\Users\\HP\\IdeaProjects\\SubiectScriereBd\\DateIN\\candidati.json");)
        {
            var tokener = new JSONTokener(fisier);
            var jsonArray = new JSONArray(tokener);

            for(int i=0;i< jsonArray.length();i++){
                var object = jsonArray.getJSONObject(i);
                int cod = object.getInt("cod_candidat");
                String nume = object.getString("nume_candidat");
                float meda = object.getFloat("media");
                var jsonArrayOtiuni = object.getJSONArray("optiuni");
                List<Optiune> optiuni = new ArrayList<>();
                for(int j=0;j<jsonArrayOtiuni.length();j++){
                    var objectOptiuni = jsonArrayOtiuni.getJSONObject(j);
                    int codLiceu = objectOptiuni.getInt("cod_liceu");
                    int codSpecializare = objectOptiuni.getInt("cod_specializare");
                    optiuni.add(new Optiune(codLiceu,codSpecializare));
                }

                listaCanditati.add(new Canditat(cod,nume,meda,optiuni));
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
