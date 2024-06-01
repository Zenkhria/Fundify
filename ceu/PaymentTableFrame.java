package ceu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentTableFrame extends JFrame {
    private JTable paymentsTable;
    private JLabel lblGoalDetails;
    private QueryExecutions queryExecutor;
    private JLabel lblComplete;
    private String username;
    private String goal;
    

    public PaymentTableFrame(String username, String goal) {
        this.username = username;
        this.goal = goal;
        queryExecutor = new QueryExecutions();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(Color.PINK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

     // Add label for goal details
        lblGoalDetails = new JLabel();
        lblGoalDetails.setBounds(10, 5, 431, 33);
        lblGoalDetails.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblGoalDetails.setForeground(Color.WHITE);
        lblGoalDetails.setBackground(new Color(233, 150, 122));
        lblGoalDetails.setOpaque(true);
        lblGoalDetails.setBorder(new LineBorder(Color.BLACK, 1));
        lblGoalDetails.setHorizontalAlignment(SwingConstants.CENTER);
        lblGoalDetails.setVerticalAlignment(SwingConstants.CENTER);
        lblGoalDetails.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(lblGoalDetails);

        // Add button to redirect to PaymentFrame
        JButton btnAddPayment = new JButton("Add Payment");
        btnAddPayment.setBackground(new Color(233, 150, 122));
        btnAddPayment.setBounds(545, 5, 229, 33);
        btnAddPayment.addActionListener(e -> {
            PaymentFrame paymentFrame = new PaymentFrame(username, goal, this);
            paymentFrame.setVisible(true);
        });
        contentPane.add(btnAddPayment);

        // Initialize payments table
        paymentsTable = new JTable();
        paymentsTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(paymentsTable);
        scrollPane.setBackground(Color.GRAY);
        scrollPane.setBounds(10, 42, 764, 495);
        contentPane.add(scrollPane);
        
     // Add label for completion status
        lblComplete = new JLabel("Complete");
        lblComplete.setForeground(new Color(0, 128, 0)); // Green color
        lblComplete.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblComplete.setBounds(455, 8, 78, 30);
        lblComplete.setVisible(false); // Initially hidden
        contentPane.add(lblComplete);

        // Update goal details and load payments data
        loadGoalDetails();
        loadPaymentsData();
    }

    private void loadGoalDetails() {
        try {
            ResultSet rs = queryExecutor.getGoalDetails(goal);
            if (rs.next()) {
                int totalPaidAmount = rs.getInt("totalPaidAmount");
                int paymentGoal = rs.getInt("paymentGoal");
                lblGoalDetails.setText("Goal: " + goal + " | Total Paid Amount: " + totalPaidAmount + " | Payment Goal: " + paymentGoal);
            
	            if (totalPaidAmount >= paymentGoal) {
	                lblComplete.setVisible(true);
	            } else {
	                lblComplete.setVisible(false);
	            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshPaymentsData() {
    	loadPaymentsData();
        loadGoalDetails();
    }

    private void loadPaymentsData() {
        try {
            ResultSet rs = queryExecutor.getPaymentsDataForGoal(goal);
            PaymentsModel model = new PaymentsModel(rs);
            paymentsTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}