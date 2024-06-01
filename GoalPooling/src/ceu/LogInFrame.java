package ceu;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class LogInFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private QueryExecutions queryExecutor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogInFrame frame = new LogInFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LogInFrame() {
		queryExecutor = new QueryExecutions();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 497);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(new Color(233, 150, 122));
		panel.setBounds(165, 80, 480, 274);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("LOG IN");
		lblNewLabel.setFont(new Font("Rockwell", Font.BOLD, 24));
		lblNewLabel.setBounds(197, 11, 93, 45);
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(135, 100, 226, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(135, 172, 226, 20);
		panel.add(passwordField);
		
		JLabel lblNewLabel_1 = new JLabel("UserName:");
		lblNewLabel_1.setBounds(135, 83, 89, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password:");
		lblNewLabel_2.setBounds(135, 153, 89, 14);
		panel.add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("Enter");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				authenticateUser();
			}
		});
		btnNewButton.setBounds(272, 203, 89, 23);
		panel.add(btnNewButton);
		
		JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRegistrationFrame();
            }
        });
        btnRegister.setBounds(135, 203, 89, 23);
        panel.add(btnRegister);
	}
	
	private void authenticateUser() {
        String username = textField.getText();
        String password = new String(passwordField.getPassword());

        try {
            String query = "SELECT * FROM UserAuthen WHERE userName = '" + username + "' AND userPass = '" + password + "'";
            ResultSet rs = queryExecutor.executeQuery(query);
            if (rs.next()) {
                MainDashFrame mainFrame = new MainDashFrame(username);
                mainFrame.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	private void openRegistrationFrame() {
        UserRegis registrationFrame = new UserRegis();
        registrationFrame.setVisible(true);
    }
}
