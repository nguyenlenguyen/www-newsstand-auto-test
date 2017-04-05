package objects;

import utilities.DateUtils;
import utilities.StringUtils;


public class CreditCard {
    public String firstName = StringUtils.randomString("FN_", 5);
    public String lastName = StringUtils.randomString("LN_", 5);
    public String fullName = firstName + " " + lastName;
    public String cardNumber = "";
    public String expireMonth = "12";
    public String expireYear = Integer.toString(Integer.parseInt(DateUtils.getCurrentDateTime("yyyy")) + 1);
    public String cvv = "111";
    public String postalCode = "10001";
    public String country = "United States";

    public CreditCard getValidCard() {
        this.cardNumber = "4012000033330620";
        return this;
    }

    public CreditCard getInvalidCard() {
        this.cardNumber = "4000111111111115";
        return this;
    }
}