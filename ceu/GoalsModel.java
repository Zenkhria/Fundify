package ceu;

import javax.swing.table.DefaultTableModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GoalsModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;
    
    private static final String[] columnNames = {"Goal", "Total Paid Amount", "Payment Goal"};

    public GoalsModel(ResultSet rs) throws SQLException {
        super(new Object[][]{}, columnNames);
        while (rs.next()) {
            Object[] row = {
                rs.getString("goal"),
                rs.getDouble("totalPaidAmount"),
                rs.getInt("paymentGoal")
            };
            addRow(row);
        }
    }
}