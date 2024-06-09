import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class TcpServer extends Thread {
    private List<Pacient> infoPacienti;
    private List<Sectie> infoSectii;
    private final int port  = 7777;
    public TcpServer(List<Pacient> listaPaceinti, List<Sectie> listaSectii){
      this.infoPacienti = listaPaceinti;
      this.infoSectii = listaSectii;
    }

    @Override
    public void run() {
        super.run();

        try(ServerSocket server = new ServerSocket(port)){
            System.out.println("Server started on port "+ port);


                Socket kanali = server.accept();
                //Primire mesaj
                InputStream inputStream = kanali.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                String message = dataInputStream.readUTF();

                int codPrimit = Integer.parseInt(message);
                //Procesare Date
                final Integer[] nrLocuriLibere = {0};
                infoSectii.stream().filter(sectie -> sectie.getCod() == codPrimit).forEach(sectie -> {
                    var nrLocuriOcup = infoPacienti.stream().filter(pacient -> pacient.getCodSectie() == codPrimit).count();
                    nrLocuriLibere[0] = (int) (sectie.getNr_locuri() - nrLocuriOcup);
                    //nrLocurilibere = (int)nrLocurilibere;

                });
                //Trimitere inapoi la client
                OutputStream outputStream = kanali.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(nrLocuriLibere[0].toString());


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
