import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static  List<Canditat> canditatList = new ArrayList<>();
    public static void main(String[] args){
        try {
            citireJSON();
            scriereXML();
            scriereJSON();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void scriereXML() throws ParserConfigurationException, TransformerConfigurationException, FileNotFoundException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var document = builder.newDocument();

        var root = document.createElement("Canditati");
        document.appendChild(root);

        for(Canditat c:canditatList){
            var nodCanditat = document.createElement("Canditat");
            root.appendChild(nodCanditat);

            var id = document.createElement("ID");
            id.setTextContent(String.valueOf( c.getCod()));
            nodCanditat.appendChild(id);

            var den = document.createElement("Nume");
            den.setTextContent(c.getNume());
            nodCanditat.appendChild(den);

            var medie = document.createElement("Medie");
            medie.setTextContent(String.valueOf(c.getMedia()));
            nodCanditat.appendChild(medie);
        }

        //Salvare in fisier XML
        var transformerfactory = TransformerFactory.newInstance();
        var transformer = transformerfactory.newTransformer();
        //Linie f importanta
        transformer.setOutputProperty(OutputKeys.INDENT,"yes");
        var fisier = new FileOutputStream("C:\\Users\\HP\\IdeaProjects\\ParsareXML\\DataIN\\canditat.xml");
        try {
            transformer.transform(new DOMSource(document),new StreamResult(fisier));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        System.out.println("XML creat!");

    }

    public static void citireXML() throws Exception{
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var document = builder.parse("C:\\Users\\HP\\IdeaProjects\\ParsareXML\\DataIN\\produse.xml");
        var root = document.getDocumentElement();

        NodeList noduri = root.getElementsByTagName("produs");
        for(int i=0;i< noduri.getLength();i++){
            var nodProdus = (Element)noduri.item(i);
            int cod = Integer.parseInt(nodProdus.getElementsByTagName("cod").item(0).getTextContent());
            String den = nodProdus.getElementsByTagName("denumire").item(0).getTextContent();
            System.out.println("Cod:"+cod+" Denumire:"+den);

            var nodTranzactii = (Element)nodProdus.getElementsByTagName("tranzactii").item(0);
            var noduriTranzactie = nodTranzactii.getElementsByTagName("tranzactie");

            for(int j=0;j<noduriTranzactie.getLength();j++){
                System.out.println("Tranzactie");
                var nodTranzactie = (Element)noduriTranzactie.item(j);
                String tip = nodTranzactie.getAttribute("tip");
                int cantitate = Integer.parseInt(nodTranzactie.getAttribute("cantitate"));
                System.out.println("Tip:"+tip+" Cantitate:"+cantitate);
            }

        }

    }

    public static void citireJSON(){
        try(var fisier = new FileReader("C:\\Users\\HP\\IdeaProjects\\ParsareXML\\DataIN\\candidati.json");){
            var tokener =  new JSONTokener(fisier);
            var arrayJSON = new JSONArray(tokener);

            for(int i=0;i< arrayJSON.length();i++){
                var object = arrayJSON.getJSONObject(i);
                int cod = object.getInt("cod_candidat");
                String nume = object.getString("nume_candidat");
                Float media = object.getFloat("media");
                canditatList.add(new Canditat(cod,nume,media));
               // System.out.println("Cod:"+cod+" Nume:"+nume+" Media:"+media);
               // System.out.println("Optiuni");

                var arr2 = new JSONArray(object.getJSONArray("optiuni"));

                for(int j=0;j<arr2.length();j++){
                    var object2 = arr2.getJSONObject(j);
                    int codLiceu = object2.getInt("cod_liceu");
                    int codSpecializare = object2.getInt("cod_specializare");
                   // System.out.println("Cod liceu:"+codLiceu+" Cod Specializare:"+codSpecializare);
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void scriereJSON(){
        try(var fisier = new PrintWriter("C:\\Users\\HP\\IdeaProjects\\ParsareXML\\DataIN\\myJSON.json");){
            JSONArray jsonArray = new JSONArray();

            for(Canditat c:canditatList){
                var object = new JSONObject();
                object.put("id",c.getCod());
                object.put("Nume",c.getNume());
                object.put("Media",c.getMedia());
                jsonArray.put(object);
            }
            fisier.write(jsonArray.toString(3));
            //3 pentru ca avem 3 elemente pentru fiecare clasa scrisa in fisier
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}
