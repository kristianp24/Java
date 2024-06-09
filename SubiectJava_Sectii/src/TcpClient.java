import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient{
    Socket socket;
    public TcpClient(Socket socket){
        this.socket = socket;

    }

    public static void main(String[] args){


        try(Socket socket = new Socket("localhost",7777);){

                //Preluare mesaj SERVER
                String message;
                Scanner scanner = new Scanner(System.in);

                System.out.print("Cod sectie:");
                OutputStream outputStream = socket.getOutputStream();
                message = scanner.nextLine();

                DataOutputStream outputStream1 = new DataOutputStream(outputStream);
                outputStream1.writeUTF(message);

                //Preluare mesaj SERVER
                InputStream inputStream = null;
                inputStream = socket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                String mesajPrimit = dataInputStream.readUTF();
                System.out.println("Numar locuri libere:"+mesajPrimit);


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
