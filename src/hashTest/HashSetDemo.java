package hashTest;

import java.util.HashSet;
import java.util.Set;

public class HashSetDemo {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("WITHOUT OVERRIDING");
        System.out.println("====================================");

        Set<PolicyWithoutOverride> set1 = new HashSet<>();

        PolicyWithoutOverride p1 =
                new PolicyWithoutOverride("POL-1001", "Ritesh");

        PolicyWithoutOverride p2 =
                new PolicyWithoutOverride("POL-1001", "Ritesh");

        System.out.println("p1 Hash Value : " + p1.hashCode());
        System.out.println("p2 Hash Value : " + p2.hashCode());

        System.out.println("p1.equals(p2) : " + p1.equals(p2));

        set1.add(p1);
        set1.add(p2);

        System.out.println("HashSet Size : " + set1.size());



        System.out.println("\n====================================");
        System.out.println("WITH OVERRIDING");
        System.out.println("====================================");

        Set<PolicyWithOverride> set2 = new HashSet<>();

        PolicyWithOverride p3 =
                new PolicyWithOverride("POL-1001", "Ritesh");

        PolicyWithOverride p4 =
                new PolicyWithOverride("POL-1001", "Ritesh");

        System.out.println("p3 Hash Value : " + p3.hashCode());
        System.out.println("p4 Hash Value : " + p4.hashCode());

        System.out.println("p3.equals(p4) : " + p3.equals(p4));

        set2.add(p3);
        set2.add(p4);

        System.out.println("HashSet Size : " + set2.size());
    }
}