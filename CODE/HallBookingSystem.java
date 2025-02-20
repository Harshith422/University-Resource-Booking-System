import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

//Registering and Login

public class HallBookingSystem extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private HomeScreen homeScreen;

    public HallBookingSystem() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));

        JLabel idLabel = new JLabel("Registered ID:");
        JLabel passwordLabel = new JLabel("Password:");
        idField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String password = new String(passwordField.getPassword());

                if (!id.isEmpty() && !password.isEmpty()) {
                    // Perform login logic here
                    boolean success = authenticateUser(id, password);
                    if (success) {
                        dispose(); // Close the login screen

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                homeScreen = new HomeScreen(id);
                                homeScreen.setVisible(true);
                            }
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid ID or password.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter your ID and password.");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the login screen

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        RegisterScreen registerScreen = new RegisterScreen();
                        registerScreen.setVisible(true);
                    }
                });
            }
        });

        add(idLabel);
        add(idField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        add(loginButton);
    }

    public boolean authenticateUser(String id, String password) {
        // Establish database connection
        String url = "jdbc:mysql://localhost:3306/database";
        String username = "root";
        String dbPassword = "1234";
        try {
            Connection connection = DriverManager.getConnection(url, username, dbPassword);

            // Check if the user exists in the database
            String query = "SELECT * FROM users WHERE id = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            boolean success = resultSet.next();

            resultSet.close();
            statement.close();
            connection.close();

            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public class RegisterScreen extends JFrame {
        private JTextField idField;
        private JPasswordField passwordField;

        public RegisterScreen() {
            setTitle("Register");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(300, 150);
            setLayout(new GridLayout(3, 2));

            JLabel idLabel = new JLabel("New ID:");
            JLabel passwordLabel = new JLabel("New Password:");
            idField = new JTextField();
            passwordField = new JPasswordField();

            JButton registerButton = new JButton("Register");
            JButton backButton = new JButton("Back");

            registerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String id = idField.getText();
                    String password = new String(passwordField.getPassword());

                    if (!id.isEmpty() && !password.isEmpty()) {
                        // Perform registration logic here
                        boolean success = registerUser(id, password);
                        if (success) {
                            dispose(); // Close the registration screen

                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    homeScreen = new HomeScreen(id);
                                    homeScreen.setVisible(true);
                                }
                            });
                        } else {
                            JOptionPane.showMessageDialog(null, "Registration failed. Please try again.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a new ID and password.");
                    }
                }
            });

            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    HallBookingSystem loginScreen = new HallBookingSystem();
                    loginScreen.setVisible(true); // Redirect to Login Screen
                }
            });

            add(idLabel);
            add(idField);
            add(passwordLabel);
            add(passwordField);
            add(backButton);
            add(registerButton);
        }

        private boolean registerUser(String id, String password) {
            // Establish database connection
            String url = "jdbc:mysql://localhost:3306/database";
            String username = "root";
            String dbPassword = "1234";
            try {
                Connection connection = DriverManager.getConnection(url, username, dbPassword);

                // Check if the user already exists in the database
                String query = "SELECT COUNT(*) FROM users WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, id);
                ResultSet resultSet = statement.executeQuery();

                int count = 0;
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }

                resultSet.close();
                statement.close();

                if (count > 0) {
                    connection.close();
                    return false; // User already exists
                }

                // Register the new user
                String insertQuery = "INSERT INTO users (id, password) VALUES (?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, id);
                insertStatement.setString(2, password);
                insertStatement.executeUpdate();

                insertStatement.close();
                connection.close();
                return true; // Registration successful
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Failed to register user
            }
        }
    }

    public class HomeScreen extends JFrame {
        private String id;

        public HomeScreen(String id) {
            this.id = id;

            setTitle("Home Screen");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 200);
            setLayout(new GridLayout(4, 1));

            JButton newBookingButton = new JButton("New Booking");
            JButton viewBookingButton = new JButton("View Booking");
            JButton editBookingButton = new JButton("Edit Booking");
            JButton cancelBookingButton = new JButton("Cancel Booking");

            newBookingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Close the home screen

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            NewBookingScreen newBookingScreen = new NewBookingScreen(id);
                            newBookingScreen.setVisible(true);
                        }
                    });
                }
            });

            viewBookingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    BookingDetails bookingDetails = fetchBookingDetails(id);
                    if (bookingDetails != null) {
                        String message = "Booking Details:\n" +
                                "Registered ID: " + id + "\n" +
                                "Hall Allotted: " + bookingDetails.getHallName() + "\n" +
                                "Expected number of people: " + bookingDetails.getCapacity() + "\n" +
                                "Date: " + bookingDetails.getDateString() + "\n" +
                                "Start Time: " + bookingDetails.getFromTime() + "\n" +
                                "End Time: " + bookingDetails.getToTime() + "\n";
                        JOptionPane.showMessageDialog(null, message);
                    } else {
                        JOptionPane.showMessageDialog(null, "No booking details found for the user.");
                    }
                }
            });

            editBookingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    BookingDetails bookingDetails = fetchBookingDetails(id);
                    if (bookingDetails != null) {
                        dispose(); // Close the home screen

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                EditBookingScreen editBookingScreen = new EditBookingScreen(id, bookingDetails);
                                editBookingScreen.setVisible(true);
                            }
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "No booking details found for the user.");
                    }
                }
            });

            cancelBookingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the booking?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteBookingDetails(id);
                        JOptionPane.showMessageDialog(null, "Booking canceled successfully.");
                    }
                }
            });

            add(newBookingButton);
            add(viewBookingButton);
            add(editBookingButton);
            add(cancelBookingButton);
        }
    }

//Make Booking    
    
    public class NewBookingScreen extends JFrame {
        private String id;
        private JSpinner capacitySpinner;
        private JSpinner dateSpinner;
        private JSpinner fromTimeSpinner;
        private JSpinner toTimeSpinner;

        public NewBookingScreen(String id) {
            this.id = id;

            setTitle("New Booking");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 250);
            setLayout(new GridLayout(6, 2));

            JLabel idLabel = new JLabel("Registered ID:");

            JLabel capacityLabel = new JLabel("Expected number of people:");
            capacitySpinner = new JSpinner(new SpinnerNumberModel(25, 25, 1000,25));
            JSpinner.DefaultEditor capacityEditor = (JSpinner.DefaultEditor) capacitySpinner.getEditor();
            capacityEditor.getTextField().setHorizontalAlignment(SwingConstants.LEFT);
            JLabel dateLabel = new JLabel("Date:");
            dateSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
            dateSpinner.setEditor(dateEditor);

            JLabel fromTimeLabel = new JLabel("Start Time:");
            fromTimeSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor fromTimeEditor = new JSpinner.DateEditor(fromTimeSpinner, "hh:mm a");
            fromTimeSpinner.setEditor(fromTimeEditor);

            JLabel toTimeLabel = new JLabel("End Time:");
            toTimeSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor toTimeEditor = new JSpinner.DateEditor(toTimeSpinner, "hh:mm a");
            toTimeSpinner.setEditor(toTimeEditor);

            JButton bookButton = new JButton("Confirm Booking");
            JButton backButton = new JButton("Back");

            bookButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int capacity = (int) capacitySpinner.getValue();
                    String dateString = getDateString(dateSpinner);
                    String fromTime = getTimeString(fromTimeSpinner);
                    String toTime = getTimeString(toTimeSpinner);

                    if (!dateString.isEmpty() && !fromTime.isEmpty() && !toTime.isEmpty()) {
                        String hallName = assignHall(capacity);

                        // Parse date and time strings to Date objects
                        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                        Date startDate;
                        Date endDate;
                        try {
                            startDate = dateTimeFormat.parse(dateString + " " + fromTime);
                            endDate = dateTimeFormat.parse(dateString + " " + toTime);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to parse date/time.");
                            return;
                        }

                        // Perform booking logic here
                        boolean success = storeBookingDetails(id, hallName, String.valueOf(capacity), startDate, endDate);
                        if (success) {
                            String message = "Booking Overview:\n" +
                                    "Registered ID: " + id + "\n" +
                                    "Hall Allotted: " + hallName + "\n" +
                                    "Expected number of people: " + capacity + "\n" +
                                    "Date: " + dateString + "\n" +
                                    "Start Time: " + fromTime + "\n" +
                                    "End Time: " + toTime + "\n";
                            JOptionPane.showMessageDialog(null, message);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Booking already exists");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter all the booking details.");
                    }
                }
            });

            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    homeScreen.setVisible(true); // Redirect to Home Screen
                }
            });

            add(idLabel);
            add(new JLabel(id));
            add(capacityLabel);
            add(capacitySpinner);
            add(dateLabel);
            add(dateSpinner);
            add(fromTimeLabel);
            add(fromTimeSpinner);
            add(toTimeLabel);
            add(toTimeSpinner);
            add(backButton);
            add(bookButton);
        }
    }

    private String getTimeString(JSpinner spinner) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(spinner.getValue());
    }

    public String getDateString(JSpinner spinner) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(spinner.getValue());
    }

    public String assignHall(int capacity) {
        if (capacity < 100) {
            return "Sandeepani";
        } else if (capacity < 200) {
            return "Amriteshwari";
        } else {
            return "Pandal";
        }
    }

    public boolean storeBookingDetails(String id, String hallName, String capacity, Date startDate, Date endDate) {
        // Establish database connection
        String url = "jdbc:mysql://localhost:3306/database";
        String username = "root";
        String password = "1234";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check for existing bookings within the given time period
            String existingBookingsQuery = "SELECT COUNT(*) FROM bookings WHERE hall_name = ? AND date_time_start < ? AND date_time_end > ?";
            PreparedStatement existingBookingsStatement = connection.prepareStatement(existingBookingsQuery);
            existingBookingsStatement.setString(1, hallName);
            existingBookingsStatement.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
            existingBookingsStatement.setTimestamp(3, new java.sql.Timestamp(startDate.getTime()));
            int existingBookingsCount = 0;
            ResultSet existingBookingsResult = existingBookingsStatement.executeQuery();
            if (existingBookingsResult.next()) {
                existingBookingsCount = existingBookingsResult.getInt(1);
            }
            existingBookingsResult.close();
            existingBookingsStatement.close();

            if (existingBookingsCount > 0) {
                connection.close();
                return false; // Booking already exists
            }

            // Store the new booking details
            String insertQuery = "INSERT INTO bookings (id, hall_name, capacity, date_time_start, date_time_end) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, id);
            insertStatement.setString(2, hallName);
            insertStatement.setString(3, capacity);
            insertStatement.setTimestamp(4, new java.sql.Timestamp(startDate.getTime()));
            insertStatement.setTimestamp(5, new java.sql.Timestamp(endDate.getTime()));
            insertStatement.executeUpdate();

            insertStatement.close();
            connection.close();
            return true; // Booking stored successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Failed to store booking
        }
    }
    
//View Booking Module

    public BookingDetails fetchBookingDetails(String id) {
        // Establish database connection
        String url = "jdbc:mysql://localhost:3306/database";
        String username = "root";
        String password = "1234";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Fetch the user's booking details from the database
            String query = "SELECT * FROM bookings WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            BookingDetails bookingDetails = null;
            if (resultSet.next()) {
                String hallName = resultSet.getString("hall_name");
                int capacity = resultSet.getInt("capacity");
                Date startDate = resultSet.getTimestamp("date_time_start");
                Date endDate = resultSet.getTimestamp("date_time_end");
                bookingDetails = new BookingDetails(hallName, capacity, startDate, endDate);
            }

            resultSet.close();
            statement.close();
            connection.close();

            return bookingDetails;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class BookingDetails {
        private String hallName;
        private int capacity;
        private Date startDate;
        private Date endDate;

        public BookingDetails(String hallName, int capacity, Date startDate, Date endDate) {
            this.hallName = hallName;
            this.capacity = capacity;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getHallName() {
            return hallName;
        }

        public int getCapacity() {
            return capacity;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public String getDateString() {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(startDate);
        }

        public String getFromTime() {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            return format.format(startDate);
        }

        public String getToTime() {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            return format.format(endDate);
        }
    }

//Cancel Booking
    
    private void deleteBookingDetails(String id) {
        // Establish database connection
        String url = "jdbc:mysql://localhost:3306/database";
        String username = "root";
        String password = "1234";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Delete the user's booking details from the database
            String deleteQuery = "DELETE FROM bookings WHERE id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, id);
            deleteStatement.executeUpdate();

            deleteStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//Edit Booking

    public class EditBookingScreen extends JFrame {
        private String id;
        private BookingDetails bookingDetails;
        private JSpinner capacitySpinner;
        private JSpinner dateSpinner;
        private JSpinner fromTimeSpinner;
        private JSpinner toTimeSpinner;

        public EditBookingScreen(String id, BookingDetails bookingDetails) {
            this.id = id;
            this.bookingDetails = bookingDetails;

            setTitle("Edit Booking");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 300);
            setLayout(new GridLayout(6, 2));

            JLabel idLabel = new JLabel("Registered ID:");

            JLabel capacityLabel = new JLabel("Expected number of people:");
            capacitySpinner = new JSpinner(new SpinnerNumberModel(bookingDetails.getCapacity(), 25, 1000, 25));
            JSpinner.DefaultEditor capacityEditor = (JSpinner.DefaultEditor) capacitySpinner.getEditor();
            capacityEditor.getTextField().setHorizontalAlignment(SwingConstants.LEFT);
            JLabel dateLabel = new JLabel("Date:");
            dateSpinner = new JSpinner(new SpinnerDateModel(bookingDetails.getStartDate(), null, null, Calendar.DAY_OF_MONTH));
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
            dateSpinner.setEditor(dateEditor);

            JLabel fromTimeLabel = new JLabel("Start Time:");
            fromTimeSpinner = new JSpinner(new SpinnerDateModel(bookingDetails.getStartDate(), null, null, Calendar.HOUR_OF_DAY));
            JSpinner.DateEditor fromTimeEditor = new JSpinner.DateEditor(fromTimeSpinner, "hh:mm a");
            fromTimeSpinner.setEditor(fromTimeEditor);

            JLabel toTimeLabel = new JLabel("End Time:");
            toTimeSpinner = new JSpinner(new SpinnerDateModel(bookingDetails.getEndDate(), null, null, Calendar.HOUR_OF_DAY));
            JSpinner.DateEditor toTimeEditor = new JSpinner.DateEditor(toTimeSpinner, "hh:mm a");
            toTimeSpinner.setEditor(toTimeEditor);

            JButton updateButton = new JButton("Update Booking");
            JButton backButton = new JButton("Back");

            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int capacity = (int) capacitySpinner.getValue();
                    String dateString = getDateString(dateSpinner);
                    String fromTime = getTimeString(fromTimeSpinner);
                    String toTime = getTimeString(toTimeSpinner);

                    if (!dateString.isEmpty() && !fromTime.isEmpty() && !toTime.isEmpty()) {
                        String hallName = assignHall(capacity);

                        // Parse date and time strings to Date objects
                        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                        Date startDate;
                        Date endDate;
                        try {
                            startDate = dateTimeFormat.parse(dateString + " " + fromTime);
                            endDate = dateTimeFormat.parse(dateString + " " + toTime);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to parse date/time.");
                            return;
                        }

                        // Perform booking update logic here
                        boolean success = updateBookingDetails(id, hallName, String.valueOf(capacity), startDate, endDate);
                        if (success) {
                            String message = "Booking updated successfully.\n" +
                                    "New Booking Details:\n" +
                                    "Registered ID: " + id + "\n" +
                                    "Hall Allotted: " + hallName + "\n" +
                                    "Expected number of people: " + capacity + "\n" +
                                    "Date: " + dateString + "\n" +
                                    "Start Time: " + fromTime + "\n" +
                                    "End Time: " + toTime + "\n";
                            JOptionPane.showMessageDialog(null, message);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Booking update failed.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter all the booking details.");
                    }
                }
            });

            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    homeScreen.setVisible(true); // Redirect to Home Screen
                }
            });

            add(idLabel);
            add(new JLabel(id));
            add(capacityLabel);
            add(capacitySpinner);
            add(dateLabel);
            add(dateSpinner);
            add(fromTimeLabel);
            add(fromTimeSpinner);
            add(toTimeLabel);
            add(toTimeSpinner);
            add(backButton);
            add(updateButton);
        }
    }

    public boolean updateBookingDetails(String id, String hallName, String capacity, Date startDate, Date endDate) {
        // Establish database connection
        String url = "jdbc:mysql://localhost:3306/database";
        String username = "root";
        String password = "1234";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check for existing bookings within the given time period
            String existingBookingsQuery = "SELECT COUNT(*) FROM bookings WHERE hall_name = ? AND date_time_start < ? AND date_time_end > ? AND id <> ?";
            PreparedStatement existingBookingsStatement = connection.prepareStatement(existingBookingsQuery);
            existingBookingsStatement.setString(1, hallName);
            existingBookingsStatement.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
            existingBookingsStatement.setTimestamp(3, new java.sql.Timestamp(startDate.getTime()));
            existingBookingsStatement.setString(4, id);
            int existingBookingsCount = 0;
            ResultSet existingBookingsResult = existingBookingsStatement.executeQuery();
            if (existingBookingsResult.next()) {
                existingBookingsCount = existingBookingsResult.getInt(1);
            }
            existingBookingsResult.close();
            existingBookingsStatement.close();

            if (existingBookingsCount > 0) {
                connection.close();
                return false; // Booking already exists
            }

            // Update the booking details
            String updateQuery = "UPDATE bookings SET hall_name = ?, capacity = ?, date_time_start = ?, date_time_end = ? WHERE id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, hallName);
            updateStatement.setString(2, capacity);
            updateStatement.setTimestamp(3, new java.sql.Timestamp(startDate.getTime()));
            updateStatement.setTimestamp(4, new java.sql.Timestamp(endDate.getTime()));
            updateStatement.setString(5, id);
            updateStatement.executeUpdate();

            updateStatement.close();
            connection.close();
            return true; // Booking details updated successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Failed to update booking details
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                HallBookingSystem loginScreen = new HallBookingSystem();
                loginScreen.setVisible(true);
            }
        });
    }
}