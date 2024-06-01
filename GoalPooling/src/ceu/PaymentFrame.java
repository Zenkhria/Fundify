package ceu;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class PaymentFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
    private QueryExecutions queryExecutor;
    private String username;
    private String goal;
    private PaymentTableFrame parentFrame;

	/**
	 * Create the frame.
	 */
	public PaymentFrame(String username, String goal, PaymentTableFrame parentFrame) {
		this.username = username;
        this.goal = goal;
        this.parentFrame = parentFrame;
        queryExecutor = new QueryExecutions();
        
		setForeground(Color.BLACK);
		setBackground(Color.PINK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 551, 363);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Payment Channel");
		lblNewLabel.setBounds(164, 32, 211, 29);
		lblNewLabel.setFont(new Font("Rockwell", Font.BOLD, 24));
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(164, 135, 211, 36);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Confirm Payment");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 confirmPayment();
			}
		});
		btnNewButton.setBounds(232, 182, 143, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("Insert Amount Here:");
		lblNewLabel_1.setBounds(164, 116, 128, 14);
		contentPane.add(lblNewLabel_1);
	}

	private void confirmPayment() {
	    try {
	        double amount = Double.parseDouble(textField.getText());
	        
	        // Insert payment into Payments table
	        String insertPaymentQuery = "INSERT INTO Payments (userID, amountPaid, datePaid, goalID) VALUES ((SELECT userID FROM UserAuthen WHERE userName = ?), ?, NOW(), (SELECT goalID FROM Goals WHERE goal = ?))";
	        PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(insertPaymentQuery);
	        pstmt.setString(1, username);
	        pstmt.setDouble(2, amount);
	        pstmt.setString(3, goal);
	        pstmt.executeUpdate();
	        pstmt.close();

	        // Update totalPaidAmount in Payments table
	        String updateTotalPaymentQuery = "UPDATE Payments SET totalPaidAmount = (SELECT SUM(amountPaid) FROM Payments WHERE goalID = (SELECT goalID FROM Goals WHERE goal = ?)) WHERE goalID = (SELECT goalID FROM Goals WHERE goal = ?)";
	        pstmt = DatabaseConnection.getConnection().prepareStatement(updateTotalPaymentQuery);
	        pstmt.setString(1, goal);
	        pstmt.setString(2, goal);
	        pstmt.executeUpdate();
	        pstmt.close();

	        parentFrame.refreshPaymentsData(); // Refresh the payment table
	        dispose();
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}
