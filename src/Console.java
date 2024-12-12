import java.util.Scanner;

public class Console {
    static Scanner scanner = new Scanner(System.in);
    public static String readLine(){
        while (true){
            String line = scanner.nextLine();
            if(!line.isEmpty()){
                return line;
            }
        }
    }
}
