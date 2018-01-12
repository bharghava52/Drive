package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet(name = "FriendFiles", urlPatterns = {"/FriendFiles"})
public class FriendFiles extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ConnectionPooling cp = null;
        Connection con = null;
        PrintWriter out = null;
        try {
            cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false","root","root");
            String Id=request.getSession().getAttribute("user").toString();
            out= response.getWriter();      
            con = cp.getConnection();
            ResultSet rs;
            PreparedStatement prepStmt = con.prepareStatement("select sharedby from share where mail=?");
            prepStmt.setString(1,Id);
            rs = prepStmt.executeQuery();
            out.println("<tr><td><a href='#' class='sharedwithme'><--Back</a></td></tr>");
            if(rs.next()){
                int checker=0;
                JSONParser parser=new JSONParser();
                JSONObject jobj =(JSONObject) parser.parse(rs.getString("sharedby"));
                JSONObject jobj1= (JSONObject) jobj.get(request.getParameter("friend"));
                prepStmt = con.prepareStatement("select data from mydata where mail=? ");
                prepStmt.setString(1,request.getParameter("friend").toString());
                ResultSet rs1=prepStmt.executeQuery();
                JSONObject jobj2=new JSONObject();
                if(rs1.next()){
                    JSONObject jfobj=(JSONObject) parser.parse(rs1.getString("data"));
                    for(Object key : jobj1.keySet()){
                        String keyStr = (String)key;
                        String keyStr1[]=keyStr.split("/");
                        File temp=new File(keyStr);
                        if(temp.exists()){
                            JSONObject jfobj1=(JSONObject) jfobj.get(key);
                            if(jfobj1.containsKey(Id)){
                                jobj2.put(key,jobj1.get(key));
                                if(keyStr1[keyStr1.length-1].indexOf('.')==-1)
                                    out.println("<tr><td><a href='#' class='frienddata' path='"+keyStr+"'>"+keyStr1[keyStr1.length-1]+"</a><tr><td>");
                                else
                                    out.println("<tr><td>"+"<a  class='friendfile' href=\"DownloadFile?path="+keyStr+"&file="+keyStr1[keyStr1.length-1]+"\" path='"+keyStr+"' file='"+keyStr1[keyStr1.length-1]+"'>" +keyStr1[keyStr1.length-1]+"</a></td></tr>");
                            }
                        }
                    }
                }
                if(checker==1){
                    if(jobj2.isEmpty())
                        jobj.remove(request.getParameter("friend"));
                    else
                        jobj.replace(request.getParameter("friend"), jobj2);
                    prepStmt=con.prepareStatement("update share set sharedby=? where mail=?");
                    prepStmt.setString(1,jobj.toString());
                    prepStmt.setString(2,Id);
                    prepStmt.executeUpdate();
                }
            }
        }catch(Exception e){
            out.print("<tr><td><a href='#'>error:"+e+"</td></tr>");
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
