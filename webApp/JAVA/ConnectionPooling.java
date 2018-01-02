package Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import java.lang.String;

public class ConnectionPooling {
    private static String driver, url, username, password;
    private static int no_of_conn;
    public  static Vector<Connection> availableConnections, busyConnections;
    private static ConnectionPooling Cpobj;
    
    private ConnectionPooling(){}
    
    public static ConnectionPooling getInstance(String driver,String url, String username, String password) throws ClassNotFoundException, SQLException{
        if(Cpobj == null){
            ConnectionPooling.driver = driver;
            ConnectionPooling.url = url;
            ConnectionPooling.username = username;
            ConnectionPooling.password = password;
            ConnectionPooling.no_of_conn= 10;
            availableConnections = new Vector<Connection>();
            busyConnections = new Vector<Connection>();
            for(int i=0;i<no_of_conn;i++){
                availableConnections.addElement(makeNewConnection());
            }
            Cpobj = new ConnectionPooling();
        }
        return Cpobj;
    }
    
    private static Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username,password);
        return (connection);
    }
    
    public synchronized Connection getConnection() throws SQLException {
        if (!availableConnections.isEmpty()) {
            Connection existingConnection = (Connection) availableConnections.lastElement();
            int lastIndex = availableConnections.size() - 1;
            availableConnections.removeElementAt(lastIndex);
            busyConnections.addElement(existingConnection);
            return (existingConnection);
        } else {
            try {
                wait();
            } catch (InterruptedException ie) {
            }
            return (getConnection());
        }
    }
    
    public synchronized void free(Connection connection) {
        busyConnections.removeElement(connection);
        availableConnections.addElement(connection);
        notifyAll();
    }
}
