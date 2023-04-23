import Controller.ExistController;
import View.Menu;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        ExistController ec = new ExistController();
        Scanner sc = new Scanner(System.in);
        String userInput, name, desc, format, country, newName, field;
        Menu menu = new Menu();
        int opcio, userOp;
        opcio = menu.mainMenu();
        while (opcio >= 0) {
            switch (opcio) {
                case 0:
                    System.out.println("Adeu!!");
                    System.exit(1);
                    break;
                case 1:
                    ec.queryAllSpiceName();
                    break;
                case 2:
                    System.out.println("Nom del país d'origen de la espècia/as a consultar");
                    userInput = sc.nextLine();
                    ec.queryByCountry(userInput);
                    break;
                case 3:
                    System.out.println("Nom de la zona culinaria interessat. (Exemple: Chinese, Japanase...)");
                    userInput = sc.nextLine();
                    ec.queryByCuisine(userInput);
                    break;
                case 4:
                    ec.printFormat();
                    System.out.println("Introdueix el format");
                    userInput = sc.nextLine();
                    ec.queryByFormat(userInput);
                    break;
                case 5:
                    System.out.println("Introdueix nom de la espàcia");
                    name = sc.nextLine();
                    System.out.println("Introdueix una breu descripció");
                    desc = sc.nextLine();
                    System.out.println("Introdueix el format de la espècia");
                    format = sc.nextLine();
                    System.out.println("Introdueix el pías d'orígen");
                    country = sc.nextLine();
                    ec.insertSpice(name,desc,format,country);
                    break;
                case 6:
                    System.out.println("Introdueix el nom de la espècia que es vol esborrar.");
                    name = sc.nextLine();
                    ec.deleteSpiceByName(name);
                    break;
                case 7:
                    System.out.println("Introdueix el nom de la espècia a canviar.");
                    name = sc.nextLine();
                    System.out.println("Introdueix el nou nom a posar.");
                    newName = sc.nextLine();
                    ec.updateSpiceByName(name,newName);
                    break;
                case 8:
                    System.out.println("Introdueix el nom del país de les especias a esborrar");
                    newName = sc.nextLine();
                    ec.deleteSpiceByCountry(newName);
                    break;
                case 9:
                    System.out.println("Introdueix el nom del format de les especias a esborrar");
                    newName = sc.nextLine();
                    ec.deleteSpiceByProductStyle(newName);
                    break;
                case 10:
                    System.out.println("Introdueix el nom de la espeècia a modificar:");
                    name = sc.nextLine();
                    System.out.println("Introdueix el camp que es desitja canviar:");
                    field = sc.nextLine();
                    System.out.println("Introdueix el nou valor per el camp seleccionat");
                    newName = sc.nextLine();
                    ec.updateSpiceAllField(name,field,newName);
                    break;
                default:
                    break;
            }
            opcio = menu.mainMenu();
        }
    }
}

