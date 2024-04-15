import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class Profesor{
    private int nrMatricol;
    private String nume;
    private String departament;
    private String prenume;

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public int getNrMatricol() {
        return nrMatricol;
    }

    public void setNrMatricol(int nrMatricol) {
        this.nrMatricol = nrMatricol;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public Profesor(int nrMatricol, String nume, String departament,String prenume) {
        this.nrMatricol = nrMatricol;
        this.nume = nume;
        this.prenume=prenume;
        this.departament = departament;

    }

    public Profesor() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Profesor{");
        sb.append("nrMatricol=").append(nrMatricol);
        sb.append(", nume='").append(nume).append('\'');
        sb.append(", prenume='").append(prenume).append('\'');
        sb.append(", departament='").append(departament).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

class Programari{
    private String ziua;
    private String ora;
    private Profesor prof;
    private String sala;
    private boolean ocupat;
    private String formatie;
    private String materia;

    public String getMateria() {
        return materia;
    }

    public String getZiua() {
        return ziua;
    }

    public String getOra() {
        return ora;
    }

    public Profesor getProf() {
        return prof;
    }

    public String getSala() {
        return sala;
    }

    public boolean isOcupat() {
        return ocupat;
    }

    public String getFormatie() {
        return formatie;
    }

    public  Programari(String ziua,String ora,Profesor Prof,String materia,String sala,
                       boolean isOcupat,String formatie){
        this.ziua=ziua;
        this.ora=ora;
        this.prof = Prof;
        this.materia=materia;
        this.sala=sala;
        this.ocupat=isOcupat;
        this.formatie=formatie;

    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Programari{");
        sb.append("ziua='").append(ziua).append('\'');
        sb.append(", ora='").append(ora).append('\'');
        sb.append(", Profesor=").append(this.prof.toString());
        sb.append(", materia=").append(materia);
        sb.append(", sala=").append(sala);
        sb.append(", ocupat=").append(ocupat);
        sb.append(", formatie='").append(formatie).append('\'');
        sb.append('}');
        return sb.toString();
    }
}


public class Orar {

    static void afisareOrarGrupa(
            String grupa,
            List<Programari> programari,
            Map<String, List<String>> componentaSerii){

        List.of("Luni","Marti","Miercuri","Vineri","Joi").forEach(ziua -> {
            List.of("Ora730","Ora900","Ora1030","Ora1200","Ora1330","Ora1500","Ora1630","Ora1800").forEach(ora->{
                programari.stream().filter(grupas -> grupas.getFormatie().equals(grupa)).
                        filter(programare -> programare.getZiua().equals(ziua)).
                        filter(programare -> programare.getOra().equals(ora)).forEach(programare->{
                            System.out.printf("%s\t%s\t%s\t%s\t%s\n",
                                    ziua,ora,programare.getMateria(),
                                    programare.getSala(),programare.getFormatie());
                        });
            });

        });


    }



    public static void main(String[] args) throws  Exception{
        Map<Integer,Profesor> profesori = new HashMap<>();
        List<Programari> programari = new ArrayList<>();
        Map<String, List<String>> componentaSerii = new HashMap<>();

        try(FileInputStream f = new FileInputStream("Inputs/profesori.txt"))
        {
            InputStreamReader streamReader =  new InputStreamReader(f);
            BufferedReader reader = new BufferedReader(streamReader);
            String linie = "";
//            profesori=reader.lines().map(linie->new Profesor(Integer.parseInt(linie.split("\t")[0]),
//                            linie.split("\t")[1],
//                            linie.split("\t")[3],
//                            linie.split("\t")[2]))
//                    .collect(Collectors.toMap(Profesor::getNrMatricol, Function.identity()));
            while(reader.ready()){
                linie = reader.readLine();
                profesori.put(Integer.parseInt(linie.split("\t")[0]),
                        new Profesor(Integer.parseInt(linie.split("\t")[0]),
                                linie.split("\t")[1],linie.split("\t")[3],
                                linie.split("\t")[2]) );
                //profesori.put(p.getNrMatricol(),p);

            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
//        for (var p : profesori.entrySet()){
//            System.out.println(p.getValue().toString());
//        }

        try(FileInputStream f = new FileInputStream("Inputs/programari.txt")){
            InputStreamReader streamReader = new InputStreamReader(f);
            BufferedReader reader = new BufferedReader(streamReader);
           String linie ="";

            while(reader.ready()){
                linie = reader.readLine();
                programari.add(new Programari(linie.split("\t")[0],
                        linie.split("\t")[1],
                       profesori.get(Integer.parseInt((linie.split("\t")[2]))),
                        linie.split("\t")[3],
                        linie.split("\t")[4],Boolean.parseBoolean(linie.split("\t")[5]),
                        linie.split("\t")[6]));
            }

//            programari = reader.lines().map(lines -> new Programari(lines.split("\t")[0],
//                                                        lines.split("\t")[1],
//                                                        Integer.parseInt(lines.split("\t")[2]),
//                                                        lines.split("\t")[3],
//                                                        Integer.parseInt( lines.split("\t")[4]),Boolean.parseBoolean(lines.split("\t")[5]),
//                                                        lines.split("\t")[6])).collect(Collectors.toList());

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

//        for (var prog : programari){
//            System.out.println(prog.toString());
//        }

        //2.1 Afișare lista cursuri în ordine alfabetică
        programari.stream().filter(c -> c.isOcupat()).map(cursuri -> cursuri.getMateria()).distinct().sorted().
                forEach(System.out::println);
        //2.2 Afișare număr de activități pentru fiecare profesor
        //List<Profesor> profi = programari.stream().map(Programari::getProf).toList();
        programari.stream().collect(Collectors.groupingBy(p -> p.getProf())).forEach((prof,p) ->{
            var cursuri = p.stream().filter(Programari::isOcupat).count();
            var seminari = p.stream().filter(c->!c.isOcupat()).count();
            System.out.printf("%-40s %-20d %-20d\n", prof.getPrenume(),cursuri,seminari);
        });
//        2.3 Lista departamentelor ordonate descrescator dupa numărul de activități
//        Utila clasa Departament cu atributele: (String) denumire, (long) numarActivitati.
        class  Departament{
            private String denumire;
            private long nrActivitati;

            public String getDenumire() {
                return denumire;
            }

            public long getNrActivitati() {
                return nrActivitati;
            }

            public Departament(String denumire, long nrActivitati) {
                this.denumire = denumire;
                this.nrActivitati = nrActivitati;
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("Departament{");
                sb.append("denumire='").append(denumire).append('\'');
                sb.append(", nrActivitati=").append(nrActivitati);
                sb.append('}');
                return sb.toString();
            }
        }
        List<Departament> departamente = new ArrayList<>();

        programari.stream().map(departament -> departament.getProf().getDepartament()).distinct()
                .map(denumire -> {
                    var nractivitati = programari.stream().filter(dep -> dep.getProf().getDepartament().equals(denumire)).
                            count();
                    return new Departament(denumire,nractivitati);
                }).forEach(System.out::println);

        //3
        List<String> seriaC = new ArrayList<>();
        Collections.addAll(seriaC,"1045","1046","1047","1048","1049");
        List<String> seriaD = new ArrayList<>();
        Collections.addAll(seriaD,"1050","1051","1052","1053","1054");
        List<String> seriaE = new ArrayList<>();
        Collections.addAll(seriaE,"1055","1056","1057","1058");
        componentaSerii.put("C",seriaC);
        componentaSerii.put("D",seriaD);
        componentaSerii.put("E",seriaE);

       // afisareOrarGrupa("1056",programari,componentaSerii);

        FileOutputStream f = new FileOutputStream("fisier.bin");
        DataOutputStream data = new DataOutputStream(f);
        for(var p:programari){
            data.writeUTF(p.getMateria());

            data.writeUTF(p.getZiua());
        }
        f.close();

        FileInputStream If = new FileInputStream("fisier.bin");
        DataInputStream dI = new DataInputStream(If);
        int aux;
       

    }
}