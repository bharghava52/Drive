package JAVA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBOperations implements DBModel{
    ConnectionPooling cp=null;
    
    public DBOperations(){
     try{
        cp=ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false", "root","root");
     }catch(Exception e){
     }
    }
    
    @Override
    public int Update(String url){ 
        
        Connection con=null;
        try{
            con=cp.getConnection();
            PreparedStatement prepStmt = con.prepareStatement("update "+url);
            return prepStmt.executeUpdate();
        }catch(Exception e){
        }finally{
         cp.free(con);
        }
        return 0;
    }
    
    @Override
    public int Delete(String url){
        Connection con=null;
        try{
            con=cp.getConnection();
            PreparedStatement prepStmt = con.prepareStatement("delete from "+url);
            return prepStmt.executeUpdate();
        }catch(Exception e){
        }finally{
         cp.free(con);
        }
        return 0;
    }
    
    @Override
    public ResultSet Select(String url){
        ResultSet rs=null;
        Connection con=null;
        try{
            con=cp.getConnection();
            PreparedStatement prepStmt = con.prepareStatement("Select "+url);
            rs=prepStmt.executeQuery();
        }catch(Exception e){
        }finally{
         cp.free(con);
        }
        return rs;
    }
    
    @Override
    public int Insert(String url){
        Connection con=null;
        try{
            con=cp.getConnection();
            PreparedStatement prepStmt = con.prepareStatement("Insert into "+url);
            return prepStmt.executeUpdate();
        }catch(Exception e){
        }finally{
         cp.free(con);
        }
        return 0;
    }
}
