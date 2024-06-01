package ceu;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainDashFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private QueryExecutions queryExecutor;
	String username;
	private JTable goalsTable;
	private JComboBox<String> comboBox;
	
	/**
	 * Create the frame.
	 */
	public MainDashFrame(String username) {
		this.username = username;
		queryExecutor = new QueryExecutions();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1347, 851);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(233, 150, 122));
		panel.setBounds(175, 77, 946, 561);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Create New Goal");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openGoalCreationFrame();
			}
		});
		btnNewButton.setForeground(Color.GRAY);
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.setBounds(789, 11, 131, 48);
		
		panel.add(btnNewButton);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("Rockwell", Font.BOLD, 14));
		comboBox.setBounds(21, 10, 141, 48);
		panel.add(comboBox);
		
		populateMemberBox();
		
		JPanel panel_1 = new JPanel();
        panel_1.setBounds(21, 80, 899, 452);
        panel_1.setLayout(new BorderLayout()); // Set BorderLayout for panel_1
        panel.add(panel_1);

        JLabel lblNewLabel = new JLabel("Welcome, " + username + "!");
        lblNewLabel.setForeground(new Color(255, 255, 224));
        lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 21));
        lblNewLabel.setBounds(10, 11, 438, 37);
        contentPane.add(lblNewLabel);

        goalsTable = new JTable();
        
        goalsTable.setFont(new Font("Tahoma", Font.PLAIN, 19));
        
        
        JScrollPane scrollPane = new JScrollPane(goalsTable);
        panel_1.add(scrollPane, BorderLayout.CENTER); 
        
        getGoalsData();
		
		goalsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent event) {
		        if (!event.getValueIsAdjusting() && goalsTable.getSelectedRow() != -1) {
		            String goal = goalsTable.getValueAt(goalsTable.getSelectedRow(), 0).toString();
		            PaymentTableFrame paymentTableFrame = new PaymentTableFrame(username, goal);
		            paymentTableFrame.setVisible(true);
		        }
		    }
		});
	}

	private void getGoalsData() {
        try {
            ResultSet rs = queryExecutor.getGoalsData();
            GoalsModel model = new GoalsModel(rs);
            goalsTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	private void populateMemberBox() {
        try {
        	ArrayList<String> users = new ArrayList<>();
        	users.add("Group Members");
            ResultSet rs = queryExecutor.getAllUsers();
            while (rs.next()) {
                users.add(rs.getString("userName"));
            }
            comboBox.setModel(new DefaultComboBoxModel<>(users.toArray(new String[0])));

            comboBox.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    evt.consume();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	private void openGoalCreationFrame() {
        GoalCreationFrame newGoalFrame = new GoalCreationFrame(this, username);
        newGoalFrame.setVisible(true);
    }

    public void refreshGoalsTable() {
    	getGoalsData();
    }
}
