import java.util.Scanner;

public class Main {

    static Scanner input = new Scanner(System.in);
    public static void main(String [] args) throws CloneNotSupportedException {
       Autobuz a=new Autobuz();
       Autobuz a1 = new Autobuz(2005,"Audi",40,1569);
       Autobuz a2=new Autobuz(a);
       a=a1.clone();

//       System.out.println("Puneti ceva:");
//       String ceva = input.nextLine();
//       a2.setMarca(input.nextLine());

       System.out.println(a1);
       System.out.println(a2);

       Autobaza ab=new Autobaza();
       Autobaza ab1=new Autobaza();
       ab1=(Autobaza)ab.clone();

    }

}