package valuetype;

import valuetype.embedded.Address;

public class ValueMain {

    public static void main(String[] args) {
        Address address1 = new Address("city", "street", "123-123");
        Address address2 = new Address("city", "street", "123-123");

        // equals override
        System.out.println("address1 equals address2: " + address1.equals(address2));
    }
}
