package jdbc;

import java.sql.*;


public class DatabaseOfParticipants {

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatementRegistration;
    private static PreparedStatement preparedStatementChangeData;
    private static PreparedStatement preparedStatementGetNicknameByLoginAndPassword;
    private static PreparedStatement preparedStatementUniqueness;


    private static void createDB() throws SQLException {
    /*"CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT,
    'name' text, 'phone' INT);")*/
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS [chat-users] (\n" +
                "    id       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                     NOT NULL,\n" +
                "    nickname STRING  NOT NULL,\n" +
                "    login    STRING  NOT NULL,\n" +
                "    password STRING  NOT NULL\n" +
                ");");
    }

    public static boolean registration(String nick, String login, String password) throws SQLException {
        preparedStatementRegistration = preparedStatementRegistration();
        preparedStatementRegistration.setString(1, nick);
        preparedStatementRegistration.setString(2, login);
        preparedStatementRegistration.setString(3, password);
        preparedStatementRegistration.executeUpdate();
        return true;
    }

    public static boolean presenceInDatabase(String nick, String login, String password) throws SQLException {
        boolean result = false;
        String preRequestNick = "SELECT EXISTS(SELECT * FROM [chat-users] WHERE nickname = ?)";
   /* String preRequestLogin = "SELECT login FROM [chat-users] WHERE login=?";
    String preRequestPass = "SELECT password FROM [chat-users] WHERE password=?";*/
        preparedStatementUniqueness = connection.prepareStatement(preRequestNick);
        preparedStatementUniqueness.setString(1, preRequestNick);
        ResultSet resultSet = preparedStatementUniqueness.executeQuery();
        if (resultSet.next()) return result;
        return true;
    }



    public static boolean changeData(String nick, String login, String password) throws SQLException {
        preparedStatementChangeData.setString(1, nick);
        preparedStatementChangeData.setString(2, login);
        preparedStatementChangeData.setString(3, password);
        preparedStatementChangeData.executeUpdate();
        return true;
    }

    public static String getNicknameByLoginAndPassword(String login, String password) throws SQLException {
        String nickname = null;
        preparedStatementGetNicknameByLoginAndPassword.setString(3, login);
        preparedStatementGetNicknameByLoginAndPassword.setString(4, password);
        ResultSet resultSet = preparedStatementGetNicknameByLoginAndPassword.executeQuery();
        if (resultSet.next()) {
            nickname = resultSet.getString(2);
        }
        return nickname;
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:users.db");
        statement = connection.createStatement();
        System.out.println("База Подключена!");
    }

    private static void disconnect() throws SQLException {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static PreparedStatement preparedStatementRegistration() throws SQLException {
        preparedStatementRegistration = connection.prepareStatement("INSERT INTO 'chat-users' (nickname, login, password) VALUES (?, ?, ?);");
        return preparedStatementRegistration;
    }

    private static PreparedStatement preparedStatementGetNicknameByLoginAndPassword() throws SQLException {
        preparedStatementGetNicknameByLoginAndPassword = connection.prepareStatement("SELECT nickname FROM 'chat-users' WHERE nickname = ? AND password = ?;");
        return preparedStatementGetNicknameByLoginAndPassword;
    }

    private static PreparedStatement preparedStatementsChangeData() throws SQLException {
        preparedStatementChangeData = connection.prepareStatement("UPDATE 'chat-users' SET (nickname, login, password) VALUES (?, ?, ?);");
        return preparedStatementChangeData;
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String nick = "Mark";
        String login = "mark";
        String password = "pass11";
        connect();
        createDB();
        if (userForRegistration(nick, login, password)) return;
        disconnect();
    }

    private static boolean userForRegistration(String nick, String login, String password) throws SQLException {
        boolean flag = presenceInDatabase(nick, login, password);
        System.out.println(flag);
        if(flag) {
            System.out.println("Пользователь есть в базе");
            return true;
        } else {
            registration(nick, login, password);
        }

        return false;
    }

}


