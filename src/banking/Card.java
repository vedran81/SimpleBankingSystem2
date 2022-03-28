package banking;

import java.util.Random;

public class Card {
    private int id;
    private String cardNumber;
    private String pin;
    private int balance;

    static private final Random random = new Random();


    public static String newPin() {
        String pin;
        int lower = 1000;
        int upper = 9999;
        pin = Integer.toString(random.nextInt(upper - lower + 1) + lower);

        return pin;
    }

    public static boolean isValidCreditCardNumber(String cardNumber) {
        // int array for processing the cardNumber
        int[] cardIntArray = new int[cardNumber.length()];

        for (int i = 0; i < cardNumber.length(); i++) {
            char c = cardNumber.charAt(i);
            cardIntArray[i] = Integer.parseInt("" + c);
        }

        for (int i = cardIntArray.length - 2; i >= 0; i = i - 2) {
            int num = cardIntArray[i];
            num = num * 2;  // step 1
            if (num > 9) {
                num = num % 10 + num / 10;  // step 2
            }
            cardIntArray[i] = num;
        }

        int sum = sumDigits(cardIntArray);  // step 3

        // step 4
        return sum % 10 == 0;
    }

    private static int sumDigits(int[] cardIntArray) {
        int res = 0;
        for (int j : cardIntArray) {
            res = res + j;
        }
        return res;
    }

    static public String newValidCardNumber() {
        String cardNum;
        String bin = "400000";
        int lowerAccNr = 100000000;
        int upperAccNr = 999999999;
        int accNr = random.nextInt(upperAccNr - lowerAccNr + 1) + lowerAccNr;
        String digits = bin + accNr;

        int sum = 0;
        boolean alternate = false;

        for (int i = digits.length() - 1; i >= 0; --i) {
            int digit = Character.getNumericValue(digits.charAt(i));
            digit = (alternate = !alternate) ? (digit * 2) : digit;
            digit = (digit > 9) ? (digit - 9) : digit;
            sum += digit;
        }
        int lastDigit = (sum * 9) % 10;

        cardNum = digits + lastDigit;

        return cardNum;
    }

    public Card(boolean empty) {
        if (!empty) {
            setBalance(0);
            setCardNumber(newValidCardNumber());
            setPin(newPin());
        }

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}