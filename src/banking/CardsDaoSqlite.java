package banking;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CardsDaoSqlite {

    private Connection conn;

    public CardsDaoSqlite(String fileName) {

        String createTableSql = "CREATE TABLE IF NOT EXISTS card ("
                + "id INTEGER PRIMARY KEY, "
                + "number TEXT, "
                + "pin TEXT, "
                + "balance INTEGER DEFAULT 0 "
                + ");";

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);

            Statement s = conn.createStatement();
            s.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCard(Card card) {
        String addCardSql = "INSERT INTO card (number, pin, balance) "
                + "VALUES (? ,? ,?)";

        try {
            PreparedStatement statement = conn.prepareStatement(addCardSql);
            statement.setString(1, card.getCardNumber());
            statement.setString(2, card.getPin());
            statement.setInt(3, card.getBalance());

            statement.execute();

            card.setId(statement.getGeneratedKeys().getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public Map<String, Card> loadAllCards() {
        String sql = "SELECT * FROM card";
        Map<String, Card> returnMap = new HashMap<>();
        try {
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                Card card = new Card(true);
                card.setId(result.getInt("id"));
                card.setCardNumber(result.getString("number"));
                card.setPin(result.getString("pin"));
                card.setBalance(result.getInt("balance"));
                returnMap.put(card.getCardNumber(), card);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return returnMap;
    }

    public void deleteCard(Card card) {
        String sql = "DELETE FROM card WHERE id = " + card.getId();

        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCardsBalance(Card[] card) {
        try {
            Statement statement = conn.createStatement();
            for (Card value : card) {
                String sql = "UPDATE card"
                        + " SET balance = " + value.getBalance()
                        + " WHERE id = " + value.getId();
                statement.addBatch(sql);
            }

            statement.executeBatch();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}