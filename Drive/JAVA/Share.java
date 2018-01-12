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
            HttpSession ck=request.getSession();
            String Id1=ck.getAttribute("user").toString();
            String Id=request.getParameter("mail");
            if(Id1.equals(Id)){
                out.print("cannot share to self");
            }else{
                String path=request.getParameter("path");
                String path1[]=path.split("/");
                PreparedStatement prepStmt = con.prepareStatement("select data from mydata where mail=?");
                prepStmt.setString(1,path1[0]);
                ResultSet rs=prepStmt.executeQuery();
                if(rs.next()){
                    JSONParser parser=new JSONParser();
                    JSONObject jobj =(JSONObject) parser.parse(rs.getString("data"));
                    JSONObject jobj1=(JSONObject) jobj.get(path);
                    jobj1.put(Id,"v");
                    jobj.replace(path,jobj1);
                    prepStmt =con.prepareStatement("update mydata set data=? where mail=?");
                    prepStmt.setString(1,jobj.toString());
                    prepStmt.setString(2,path1[0]);
                    int Status=prepStmt.executeUpdate();
                    if(Status==1){
                        prepStmt = con.prepareStatement("select * from share where mail=?");
                        prepStmt.setString(1,Id);
                        rs1 = prepStmt.executeQuery();
                        if(rs1.next()){
                            int checker=0;
                            jobj =(JSONObject) parser.parse(rs1.getString("sharedby"));
                            if(jobj.containsKey(path1[0])){
                                JSONObject jobj2= (JSONObject) jobj.get(path1[0]);
                                if(jobj2.containsKey(path)){
                                 out.println("you have already shared this file/folder with this user");
                                 checker=1;
                                }else{
                                 jobj2.put(path,"V");
                                 jobj.replace(path1[0],jobj2);
                                }
                            }else{
                                JSONObject jobj2=new JSONObject();
                                jobj2.put(path,"V");
                                jobj.put(path1[0],jobj2);
                            }
                            prepStmt = con.prepareStatement("update share set sharedby=? where mail=?");
                            prepStmt.setString(1,jobj.toString());
                            prepStmt.setString(2,Id);
                            int status=prepStmt.executeUpdate();
                            if(status!=-1 && checker==0){
                                out.print("shared with the user");
                            }
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
