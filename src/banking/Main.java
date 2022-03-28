package banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    static CardsDaoSqlite dao;
    public static Map<String, Card> cardsCache = new HashMap<>();

    static private boolean exitMainMenu = false;

    static public Card tryLogin() {

        System.out.println("Enter your card number:");
        String cardNumberCheck = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pinCheck = scanner.nextLine();

        Card tryCard = cardsCache.get(cardNumberCheck);
        if (tryCard != null) {
            if (tryCard.getPin().equals(pinCheck)) {
                System.out.println("You have successfully logged in!");
                return tryCard;
            }
        }
        System.out.println("Wrong card number or PIN!");
        return null;
    }

    static void accountMenu(Card workCard) {

        String menuChoice;
        do {
            System.out.println("1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out \n0. Exit");

            menuChoice = scanner.nextLine();

            switch (menuChoice) {
                case "1":
                    System.out.println("Balance: " + workCard.getBalance());
                    break;
                case "2":
                    int income;
                    System.out.println("Enter income:");
                    income = Integer.parseInt(scanner.nextLine());
                    workCard.setBalance(workCard.getBalance() + income);
                    dao.updateCardsBalance(new Card[]{workCard});
                    //
                    break;
                case "3":
                    System.out.println("Transfer\nEnter card number:");
                    String cardNum = (scanner.nextLine());
                    if (!Card.isValidCreditCardNumber(cardNum)) {
                        System.out.println("Probably you made a mistake in the card number.\nPlease try again!");
                    } else {
                        Card destCard = cardsCache.get(cardNum);
                        if (destCard == null) {
                            System.out.println("Such a card does not exist.");
                        } else {
                            System.out.println("Enter how much money you want to transfer:");
                            int transferAmount = Integer.parseInt(scanner.nextLine());
                            if (transferAmount > workCard.getBalance()) {
                                System.out.println("Not enough money!");
                            } else {
                                workCard.setBalance(workCard.getBalance() - transferAmount);
                                destCard.setBalance(destCard.getBalance() + transferAmount);
                                dao.updateCardsBalance(new Card[]{workCard, destCard});
                                System.out.println("Success!");
                            }
                        }
                    }
                    break;
                case "4":
                    cardsCache.remove(workCard);
                    dao.deleteCard(workCard);
                    System.out.println("Account closed!");
                    menuChoice = "0";
                    break;
                case "5":
                    System.out.println("You have successfully logged out!");
                    menuChoice = "0";
                    break;
                case "0":
                    System.out.println("Bye!");
                    exitMainMenu = true;
                    break;
                default:
                    System.out.println("Wrong input");
            }
        } while (!menuChoice.equals("0"));
    }

    static public void mainMenu() {

        String menuChoice;

        do {
            System.out.println("1. Create an account \n2. Log into account \n0. Exit");
            menuChoice = scanner.nextLine();

            switch (menuChoice) {
                case "0":
                    exitMainMenu = true;
                    break;
                case "1":
                    Card newCard = new Card(false);

                    cardsCache.put(newCard.getCardNumber(), newCard);
                    dao.saveCard(newCard);

                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    System.out.println(newCard.getCardNumber());
                    System.out.println("Your card PIN:");
                    System.out.println(newCard.getPin());
                    break;
                case "2":
                    Card loginCard = tryLogin();
                    if (loginCard != null) {
                        accountMenu(loginCard);
                    }
                    break;
                default: {
                    System.out.println("Wrong input");
                }
            }
        } while (!exitMainMenu);
    }


    public static void main(String[] args) {

        String dbFileName = "db.s3db";

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-fileName")) {

                dbFileName = args[i + 1];
                break;
            }
        }

        dao = new CardsDaoSqlite(dbFileName);
        cardsCache = dao.loadAllCards();

        mainMenu();
    }
}