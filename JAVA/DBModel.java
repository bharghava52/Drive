package JAVA;

import java.sql.ResultSet;

public interface DBModel {
    
    public int Insert(String url);
    public int Update(String url);
    public int Delete(String url);
    public ResultSet Select(String url);
    
}
