import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static List<Sectie> listaSectii = new ArrayList<>();
    public static  List<Pacient> listaPacienti = new ArrayList<>();
    public static void main(String[] args){
        //1) Să afișeze la consolă lista secțiilor spitalului cu un număr de locuri strict mai mare decât 10
        System.out.println("-------CERINTA 1-----------");
        citireSectii();
         listaSectii.stream().filter(sectia -> sectia.getNr_locuri() > 10).forEach(System.out::println);
       // 2) Să afișeze la consolă lista secțiilor spitalului sortată descrescător după varsta medie a pacientilor internați pe secție.
        System.out.println("-------CERINTA 2-----------");
        citirePacienti();
        //listaPacienti.forEach(System.out::println);
//        listaSectii.stream().distinct().sorted(Comparator.comparingInt(sectie ->{
//            var varstaMedie = listaPacienti.stream().filter(pacient -> pacient.getCodSectie()== sectie.getCod()).mapToInt(pacient -> pacient.getVarsta()).sum();
//            var nrPacienti = listaPacienti.stream().filter(pacient -> pacient.getCodSectie()== sectie.getCod()).count();
//            var mediaVarsta = 0;
//            if(nrPacienti > 0) {
//                 mediaVarsta = (int)(varstaMedie / nrPacienti);
//                 return mediaVarsta;
//            }
//            else
//                return (int)mediaVarsta;
//
//
//        })).collect(Collectors.toList()).forEach(sectie -> {
//            System.out.println(sectie);
//        });

        var distinctList = listaSectii.stream().distinct().collect(Collectors.toList());
        Map<Double,Sectie> sectieMap = new HashMap<>();
        distinctList.stream().forEach(sectie -> {
            var varstaMedie = listaPacienti.stream().filter(pacient -> pacient.getCodSectie()== sectie.getCod()).mapToInt(pacient -> pacient.getVarsta()).sum();

            var nrPacienti = listaPacienti.stream().filter(pacient -> pacient.getCodSectie()== sectie.getCod()).count();
            if (nrPacienti > 0) {
                var mediaVarsta = varstaMedie / nrPacienti;
                sectieMap.put((double)mediaVarsta,sectie);
            }
        });

        sectieMap.keySet().stream().sorted(Comparator.reverseOrder()).forEach(key->{
            System.out.println("Sectia:"+sectieMap.get(key)+" are media de varsta:"+key);
        });

        System.out.println("----------CERINTA 3------------");
//        3) Să se scrie în fișierul text jurnal.txt un raport al internărilor pe secții, de forma:
//        cod_secție_1,nume secție_1,numar_pacienti_1
        try(FileOutputStream fisier = new FileOutputStream("C:\\Users\\HP\\IdeaProjects\\SubiectJava_Sectii\\DataIN\\jurnal.txt");
           OutputStreamWriter inputStreamReader = new OutputStreamWriter(fisier);
           BufferedWriter writer = new BufferedWriter(inputStreamReader);){

            listaSectii.stream().collect(Collectors.groupingBy(sectie -> sectie.getCod())).forEach((integer, secties) -> {
                long numarPacienti = listaPacienti.stream().filter(pacient -> pacient.getCodSectie() == integer).count();
                try {
                    writer.write(integer+","+secties.get(0).getDenumire()+","+numarPacienti);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            });

        }
        catch (Exception e){
            e.printStackTrace();
        }

       System.out.println("------CERINTA 4------------");
        TcpServer server = new TcpServer(listaPacienti,listaSectii);
        server.start();

    }

    public static void citirePacienti(){
        try(FileInputStream fisier =  new FileInputStream("C:\\Users\\HP\\IdeaProjects\\SubiectJava_Sectii\\DataIN\\pacienti.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fisier);
            BufferedReader reader = new BufferedReader(inputStreamReader);){

            while(reader.ready()){
                String linie = reader.readLine();
                long cnp = Long.parseLong(linie.split(",")[0]);
                String nume = linie.split(",")[1];
                int varsta = Integer.parseInt(linie.split(",")[2]);
                int codSpital = Integer.parseInt(linie.split(",")[3]);
                listaPacienti.add(new Pacient(cnp,nume,varsta,codSpital));
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void citireSectii(){
        try(var fisier = new FileReader("C:\\Users\\HP\\IdeaProjects\\SubiectJava_Sectii\\DataIN\\sectii.json");){
            var tokener = new JSONTokener(fisier);
            var jsonArray = new JSONArray(tokener);

            for(int i=0;i< jsonArray.length();i++){
                var object = jsonArray.getJSONObject(i);
                int cod = object.getInt("cod_sectie");
                String denumire = object.getString("denumire");
                int nr_locuri = object.getInt("numar_locuri");
                listaSectii.add(new Sectie(cod,denumire,nr_locuri));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
