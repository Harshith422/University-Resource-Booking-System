import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class UniversityResourceBookingSystem extends JFrame {
    private boolean loginSuccessful;
    private JTextField capacityField;
    private JSpinner fromTimeSpinner;
    private JSpinner toTimeSpinner;
    private JComboBox<String> hallDropdown;
    private boolean bookingSuccessful;

    public UniversityResourceBookingSystem() {
        setTitle("Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Name:");
        hallDropdown = new JComboBox<>();
        hallDropdown.addItem("Hall 1");
        hallDropdown.addItem("Hall 2");
        hallDropdown.addItem("Hall 3");

        JLabel capacityLabel = new JLabel("Capacity:");
        capacityField = new JTextField();
        capacityField.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel fromTimeLabel = new JLabel("From Time:");
        fromTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor fromTimeEditor = new JSpinner.DateEditor(fromTimeSpinner, "hh:mm a");
        fromTimeSpinner.setEditor(fromTimeEditor);

        JLabel toTimeLabel = new JLabel("To Time:");
        toTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor toTimeEditor = new JSpinner.DateEditor(toTimeSpinner, "hh:mm a");
        toTimeSpinner.setEditor(toTimeEditor);

        JButton bookButton = new JButton("Book");

        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = hallDropdown.getSelectedItem().toString();
                String capacity = capacityField.getText();
                String fromTime = getTimeString(fromTimeSpinner);
                String toTime = getTimeString(toTimeSpinner);
                if (!name.isEmpty() && !capacity.isEmpty() && !fromTime.isEmpty() && !toTime.isEmpty()) {
                    // Perform booking logic here
                    boolean success = storeBookingDetails(name, capacity, fromTime, toTime);
                    if (success) {
                        bookingSuccessful = true;
                        String message = "Booking Details:\n" +
                                "Name: " + name + "\n" +
                                "Capacity: " + capacity + "\n" +
                                "From Time: " + fromTime + "\n" +
                                "To Time: " + toTime + "\n";
                        JOptionPane.showMessageDialog(null, message);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to store booking details.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter all the booking details.");
                }
            }
        });

        add(nameLabel);
        add(hallDropdown);
        add(capacityLabel);
        add(capacityField);
        add(fromTimeLabel);
        add(fromTimeSpinner);
        add(toTimeLabel);
        add(toTimeSpinner);
        add(bookButton);
    }

    private String getTimeString(JSpinner spinner) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(spinner.getValue());
    }

    private static boolean storeUserDetails(String name, String id) {
        // Establish database connection
        String url = "jdbc:mysql://localhost:3306/hall/users";
        String username = "root";
        String password = "Harshith@123";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check for duplicate entries based on name and id
            String duplicateEntriesQuery = "SELECT COUNT(*) FROM users WHERE name = ? AND id = ?";
            PreparedStatement duplicateEntriesStatement = connection.prepareStatement(duplicateEntriesQuery);
            duplicateEntriesStatement.setString(1, name);
            duplicateEntriesStatement.setString(2, id);
            ResultSet duplicateEntriesResult = duplicateEntriesStatement.executeQuery();
            int duplicateEntriesCount = 0;
            if (duplicateEntriesResult.next()) {
                duplicateEntriesCount = duplicateEntriesResult.getInt(1);
            }
            duplicateEntriesResult.close();
            duplicateEntriesStatement.close();

            if (duplicateEntriesCount > 0) {
                JOptionPane.showMessageDialog(null, "A user with the same details already exists. Please enter different details.");
                return false;
            }

            // Create the SQL statement
            String query = "INSERT INTO users (name, id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, id);

            // Execute the statement
            int rowsInserted = statement.executeUpdate();

            // Close resources
            statement.close();
            connection.close();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean storeBookingDetails(String name, String capacity, String fromTime, String toTime) {
        // Establish database connection
    String url = "jdbc:mysql://localhost:3306/ha";
        String username = "root";
        String password = "Harshith@123";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check for existing bookings within the given time period
            String existingBookingsQuery = "SELECT COUNT(*) FROM bookings WHERE from_time < ? AND to_time > ?";
            PreparedStatement existingBookingsStatement = connection.prepareStatement(existingBookingsQuery);
            existingBookingsStatement.setString(1, toTime);
            existingBookingsStatement.setString(2, fromTime);
            int existingBookingsCount = 0;
            ResultSet existingBookingsResult = existingBookingsStatement.executeQuery();
            if (existingBookingsResult.next()) {
                existingBookingsCount = existingBookingsResult.getInt(1);
            }
            existingBookingsResult.close();
            existingBookingsStatement.close();

            if (existingBookingsCount > 0) {
                JOptionPane.showMessageDialog(null, "Another booking exists within the given time period. Please choose different time slots.");
                return false;
            }

            // Check for duplicate entries based on name, capacity, fromTime, and toTime
            String duplicateEntriesQuery = "SELECT COUNT(*) FROM bookings WHERE name = ? AND capacity = ? AND from_time = ? AND to_time = ?";
            PreparedStatement duplicateEntriesStatement = connection.prepareStatement(duplicateEntriesQuery);
            duplicateEntriesStatement.setString(1, name);
            duplicateEntriesStatement.setString(2, capacity);
            duplicateEntriesStatement.setString(3, fromTime);
            duplicateEntriesStatement.setString(4, toTime);
            ResultSet duplicateEntriesResult = duplicateEntriesStatement.executeQuery();
            int duplicateEntriesCount = 0;
            if (duplicateEntriesResult.next()) {
                duplicateEntriesCount = duplicateEntriesResult.getInt(1);
            }
            duplicateEntriesResult.close();
            duplicateEntriesStatement.close();

            if (duplicateEntriesCount > 0) {
                JOptionPane.showMessageDialog(null, "A booking with the same details already exists. Please enter different details.");
                return false;
            }

            // Create the SQL statement
            String query = "INSERT INTO bookings (name, capacity, from_time, to_time) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, capacity);
            statement.setString(3, fromTime);
            statement.setString(4, toTime);

            // Execute the statement
            int rowsInserted = statement.executeUpdate();

            // Close resources
            statement.close();
            connection.close();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showLoginScreen();
            }
        });
    }

    private static void showLoginScreen() {
        JFrame loginFrame = new JFrame();
        loginFrame.setTitle("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();

                if (!name.isEmpty() && !id.isEmpty()) {
                    boolean success = storeUserDetails(name, id);
                    if (success) {
                        loginFrame.setVisible(false);
                        UniversityResourceBookingSystem bookingSystem = new UniversityResourceBookingSystem();
                        bookingSystem.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to store user details.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter your name and ID.");
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        loginFrame.add(nameLabel);
        loginFrame.add(nameField);
        loginFrame.add(idLabel);
        loginFrame.add(idField);
        loginFrame.add(loginButton);
        loginFrame.add(cancelButton);
        loginFrame.setVisible(true);
    }

    public void showDialog() {
        if (bookingSuccessful) {
            JOptionPane.showMessageDialog(null, "Booking Successful.");
            dispose();
        }
    }
}