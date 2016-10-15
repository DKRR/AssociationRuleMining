import java.util.Scanner;

/**
 * Created by VenkataRamesh on 10/13/2016.
 */
public class RunPart2 {

    public static void main(String[] args) throws Exception{


        System.out.println("Enter support count in percent value: ");
        Scanner scanner = new Scanner(System.in);
        Integer percent = null;
        Integer confidence = null;
        try {
            percent = scanner.nextInt();
            if (percent < 1 || percent > 100) {
                System.out.println("Invalid Input, enter percent value between 1 and 100");
                return;
            }
            System.out.println("Enter confidence value in percent value: ");
            confidence = scanner.nextInt();
            if (confidence < 1 || confidence > 100) {
                System.out.println("Invalid Input, enter percent value between 1 and 100");
                return;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid Input");
            return;
        }
        double support = (double) percent / 100;
        double confidenceVal = (double) confidence / 100;
        AssociationRuleFinder assfinder = new AssociationRuleFinder(support, confidenceVal);
        assfinder.getAllPossibleAssociationRules();
    }
}
