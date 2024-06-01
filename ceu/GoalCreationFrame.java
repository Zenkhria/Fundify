package ceu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class GoalCreationFrame extends JFrame {

    private MainDashFrame parentFrame;
    private JTextField txtGoalName;
    private JTextField txtPaymentGoal;
    private String username;
    private QueryExecutions queryExecutor;

    public GoalCreationFrame(MainDashFrame parentFrame, String username) {
        this.parentFrame = parentFrame;
        this.username = username;
        this.queryExecutor = new QueryExecutions();

        setTitle("Create New Goal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(parentFrame);
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(Color.PINK);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel lblGoalName = new JLabel("Goal Name:");
        panel.add(lblGoalName);

        txtGoalName = new JTextField();
        panel.add(txtGoalName);

        JLabel lblPaymentGoal = new JLabel("Payment Goal:");
        panel.add(lblPaymentGoal);

        txtPaymentGoal = new JTextField();
        panel.add(txtPaymentGoal);

        JButton btnCreateGoal = new JButton("Create Goal");
        btnCreateGoal.setBackground(new Color(255, 255, 255));
        btnCreateGoal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewGoal();
            }
        });
        getContentPane().add(btnCreateGoal, BorderLayout.SOUTH);
    }

    private void createNewGoal() {
        String goalName = txtGoalName.getText();
        int paymentGoal = Integer.parseInt(txtPaymentGoal.getText());

        try {
            queryExecutor.insertNewGoal(goalName, paymentGoal, username);
            JOptionPane.showMessageDialog(this, "New goal created successfully.");
            parentFrame.refreshGoalsTable();
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating new goal.");
        }
    }
}