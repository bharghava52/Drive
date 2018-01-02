package Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    ConnectionPooling cp;
  
    public User() throws ClassNotFoundException, SQLException {
        this.cp = ConnectionPooling.getInstance("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/Drive", "root","root");
    }
  
    public int insert(Profile p){
        try { 
            Connection con=cp.getConnection();
            PreparedStatement prepStmt = con.prepareStatement("insert into users values(?,?,?,?)");
            prepStmt.setString(1,p.getFirstname());
            prepStmt.setString(2,p.getLastname());
            prepStmt.setString(3,p.getMail());
            prepStmt.setString(4,p.getPassword());
            int status=prepStmt.executeUpdate();
            cp.free(con);
            return status;       
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public ResultSet select(Profile p){
       Connection con=null; 
       ResultSet rs=null;
       try { 
            con=cp.getConnection();
            PreparedStatement prepStmt = con.prepareStatement("Select * from users where mail=?");
            rs=prepStmt.executeQuery();      
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
           cp.free(con);
        } 
        return rs;
    } 
}
