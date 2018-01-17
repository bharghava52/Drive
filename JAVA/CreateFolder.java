package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet(name = "CreateFolder", urlPatterns = {"/CreateFolder"})
public class CreateFolder extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ParseException {
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();
        String s=request.getParameter("path")+"/"+request.getParameter("name");
        File theDir = new File(s);
            if (!theDir.exists()) {
                try {
                    DBOperations dbo=new DBOperations();
                    ResultSet rs=dbo.Select("users from mydata where path='"+request.getParameter("path")+"'");
                    JSONParser parser=new JSONParser();
                    JSONObject jobj;
                    if(rs.next()){
                     jobj=(JSONObject) parser.parse(rs.getString("users"));
                    }else{
                     jobj=(JSONObject) parser.parse("{}");
                    }
                    int Status=dbo.Insert("mydata set mail='"+request.getParameter("path").split("/")[0]+"',path='"+s+"',users='"+jobj.toString()+"'");
                    if(Status==1){
                        theDir.mkdir();
                        out.println("folder created");
                    }else{
                     out.print("unable to create folder");
                    }
                }catch(Exception e){
                    out.println("error:"+e);
                }
            }
            else{
                out.println("this folder already exists:");
            }
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CreateFolder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CreateFolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CreateFolder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CreateFolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
