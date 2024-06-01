package ceu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutions {

    public ResultSet executeQuery(String query) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        }
        return null;
    }

    public int executeUpdate(String query) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            Statement stmt = connection.createStatement();
            return stmt.executeUpdate(query);
        }
        return 0;
    }
    
    public ResultSet getGoalsData() throws SQLException {
        String query = "SELECT g.goal, IFNULL(SUM(p.amountPaid), 0) AS totalPaidAmount, g.paymentGoal " +
                       "FROM Goals g " +
                       "LEFT JOIN Payments p ON g.goalID = p.goalID " +
                       "GROUP BY g.goal, g.paymentGoal";
        return executeQuery(query);
    }
    
    public ResultSet getGoalDetails(String goal) throws SQLException {
        String query = "SELECT g.goal, g.paymentGoal, IFNULL(SUM(p.amountPaid), 0) AS totalPaidAmount "
                     + "FROM Goals g "
                     + "LEFT JOIN Payments p ON g.goalID = p.goalID "
                     + "WHERE g.goal = '" + goal + "' "
                     + "GROUP BY g.goal, g.paymentGoal";
        return executeQuery(query);
    }
    
    public ResultSet getPaymentsDataForGoal(String goal) throws SQLException {
        String query = "SELECT u.userName, p.amountPaid, p.datePaid " +
                       "FROM Payments p " +
                       "JOIN Goals g ON p.goalID = g.goalID " +
                       "JOIN UserAuthen u ON p.userID = u.userID " +
                       "WHERE g.goal = ?";
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, goal);
            rs = pstmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            throw e;
        }
    }
    
    public void updateTotalPaymentForGoal(String goal) throws SQLException {
        String query = "UPDATE Goals g "
                     + "SET g.totalPaidAmount = (SELECT SUM(p.amountPaid) FROM Payments p WHERE p.goalID = g.goalID) "
                     + "WHERE g.goal = '" + goal + "'";
        executeUpdate(query);
    }
    
    public ResultSet getAllUsers() throws SQLException {
        String query = "SELECT userName FROM UserAuthen";
        return executeQuery(query);
    }
    
    public void insertNewGoal(String goalName, int paymentGoal, String username) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            int userID = getUserID(username);
            
            if (userID != -1) {
                String query = "INSERT INTO Goals (goal, paymentGoal, userID) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setString(1, goalName);
                    pstmt.setInt(2, paymentGoal);
                    pstmt.setInt(3, userID);
                    pstmt.executeUpdate();
                }
            } else {
                System.out.println("User not found!");
            }
        }   
    }
    
    public void insertNewUser(String username, String password) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String query = "INSERT INTO UserAuthen (userName, userPass) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            }
        }
    }
    
    private int getUserID(String username) throws SQLException {
        String query = "SELECT userID FROM UserAuthen WHERE userName = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("userID");
            }
        }
        return -1;
    }
}