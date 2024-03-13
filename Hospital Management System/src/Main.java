import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        try{
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root","root");
            System.out.println(conn);
    }
        catch (Exception e){

        }

        }
}