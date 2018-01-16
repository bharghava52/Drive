package JAVA;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

@WebServlet(name = "Share", urlPatterns = {"/Share"})
public class Share extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ConnectionPooling cp = null;
        Connection con = null;
        PrintWriter out= response.getWriter();
        ResultSet rs1=null;
        try {
            cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false","root","root");
            con = cp.getConnection();
            String Id1=request.getParameter("path").split("/")[0];
            String Id=request.getParameter("mail");
            if(Id.equals(Id1)){
                out.print("cannot share to the owner");
            }else{
                String path=request.getParameter("path");
                PreparedStatement prepStmt=con.prepareStatement("select users from mydata where path=?");
                prepStmt.setString(1,path);
                ResultSet rs=prepStmt.executeQuery();
                if(rs.next()){
                    JSONParser parser=new JSONParser();
                    JSONObject jobj =(JSONObject) parser.parse(rs.getString("users"));
                    if(jobj.containsKey(Id) && jobj.get(Id).toString().equals(request.getParameter("type"))){
                     out.print("you have already shared this data with this user");
                    }else{
                        prepStmt = con.prepareStatement("select users,path from mydata where path like ?");
                        prepStmt.setString(1,path+"%");
                        rs=prepStmt.executeQuery();
                        while(rs.next()){
                            parser=new JSONParser();
                            jobj =(JSONObject) parser.parse(rs.getString("users"));
                            if(jobj.containsKey(Id)){
                                jobj.replace(Id,request.getParameter("type"));
                                prepStmt =con.prepareStatement("update mydata set users=? where path=?");
                                prepStmt.setString(1,jobj.toString());
                                prepStmt.setString(2,rs.getString("path"));
                                prepStmt.executeUpdate();
                            }else{
                                jobj.put(Id,request.getParameter("type"));
                                prepStmt =con.prepareStatement("update mydata set users=? where path=?");
                                prepStmt.setString(1,jobj.toString());
                                prepStmt.setString(2,rs.getString("path"));
                                prepStmt.executeUpdate();
                            }
                        }
                        prepStmt=con.prepareStatement("delete from share where mail=? and path=?");
                        prepStmt.setString(1,Id);
                        prepStmt.setString(2,path);
                        prepStmt.executeUpdate();
                        prepStmt=con.prepareStatement("insert into share set mail=?,path=?,mode=?");
                        prepStmt.setString(1,Id);
                        prepStmt.setString(2,path);
                        prepStmt.setString(3,request.getParameter("type"));
                        if(prepStmt.executeUpdate()==1){
                            out.print("files/Folders has been shared");
                            prepStmt=con.prepareStatement("delete from share where mail=? and path like ? ");
                            prepStmt.setString(1,Id);
                            prepStmt.setString(2,path+"/%");
                            prepStmt.executeUpdate();
                        }    
                    }
                }    
            }
        }catch(Exception e){
            out.printf("error:"+e);
        }finally{
          cp.free(con);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
