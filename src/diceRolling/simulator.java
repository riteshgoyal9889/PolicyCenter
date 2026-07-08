package diceRolling;

import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;
public class simulator {
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);

        System.out.println("enter the dice you want to roll");
        int numberofdice= scanner.nextInt();
        System.out.println("about to roll "+numberofdice);

        Random rand=new Random();
        int rolledNumber=rand.nextInt(6)+1;
        System.out.println(rolledNumber);
       System.out.println(display(rolledNumber));
    }
    static String display(int value){
        switch(value){
            case 1:return "-------\n|     |\n|  O  |\n|     |\n-------";
            case 2:return "-------\n|O    |\n|     |\n|   O |\n-------";
            case 3:return "-------\n|O    |\n|  O  |\n|    O|\n-------";
            case 4:return "-------\n|O   O|\n|     |\n|O   O|\n-------";
            case 5:return "-------\n|O   O|\n|  O  |\n|O   O|\n--------";
            case 6:return "-------\n|O   O|\n|O   O|\n|O   O|\n-------";
            default:
                return "no dice";
        }
    }
}
