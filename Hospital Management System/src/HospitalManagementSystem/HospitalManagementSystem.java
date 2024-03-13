package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url ="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="root";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter Your Choice : ");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        //add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //view patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //view Doctor
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        //Book Appointment
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid Choice !!!");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static  void  bookAppointment(Patient patient,Doctor doctor,Connection connection, Scanner scanner){
        System.out.print("Enter Patient Id : ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id :  ");
        int DoctorId = scanner.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(DoctorId)){
            if(checkDoctorAvailability(DoctorId,appointmentDate,connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id,doctor_id , appointment_date)VALUES(?,?,?)";
                        try{
                            PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                            preparedStatement.setInt(1,patientId);
                            preparedStatement.setInt(2,DoctorId);
                            preparedStatement.setString(3,appointmentDate);
                            int rowsaffected = preparedStatement.executeUpdate();
                            if (rowsaffected>0){
                                System.out.println("Appointment Booked !!!");
                            }
                            else{
                                System.out.println("Failed to Book Appointment!!!");
                            }


                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }

            }
            else{
                System.out.println("Doctor not Available on this date!!!");
            }

        }
        else{
            System.out.println("Either Doctor or Patient does't exit!!!");
        }


    }

    public static boolean checkDoctorAvailability(int DoctorId ,String appointmentDate,Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE Doctors_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,DoctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if (count==0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;


    }

}
