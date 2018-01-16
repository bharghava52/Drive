package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet(name = "FriendData", urlPatterns = {"/FriendData"})
public class FriendData extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path=request.getParameter("path");
        String user[]=path.split("/");
        PrintWriter out= response.getWriter();
        out.println("<caption><input type='hidden' id='root' value='"+path+"'><input type='hidden' id='path' value='"+path+"'></caption>");
        out.println("<tr><td><a href='#' class='friend' user='"+user[0]+"'><div class='content'><img src='IMAGES/back.jpg'/></div></a></td></tr>");
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();ConnectionPooling cp=null;
        Connection con=null;
        try{
            cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false","root","root");
            ResultSet rs;
            con = cp.getConnection();
            for (int i = 0; i < listOfFiles.length; i++) 
            {
                if(listOfFiles[i].isDirectory()) 
                {
                    PreparedStatement prepStmt = con.prepareStatement("select users from mydata where path=?");
                    prepStmt.setString(1,path+"/"+ listOfFiles[i].getName());
                    rs=prepStmt.executeQuery();
                    if(rs.next()){
                        JSONParser parser=new JSONParser();
                        JSONObject jobj = (JSONObject) parser.parse(rs.getString("users"));
                        out.println("<tr><td><div class='content'><a href='#' class='frienddatas folder"+jobj.get(request.getSession().getAttribute("user"))+"' path='"+path+"/"+ listOfFiles[i].getName()+"' root='"+path+"'><img src='IMAGES/folder.jpg'/>" + listOfFiles[i].getName()+"</a></div></td></tr>");
                    }
                }
            }
            for (int i = 0; i < listOfFiles.length; i++) 
            {
               if (listOfFiles[i].isFile()) 
                {
                    PreparedStatement prepStmt = con.prepareStatement("select users from mydata where path=?");
                    prepStmt.setString(1,path+"/"+ listOfFiles[i].getName());
                    rs=prepStmt.executeQuery();
                    if(rs.next()){
                        JSONParser parser=new JSONParser();
                        JSONObject jobj = (JSONObject) parser.parse(rs.getString("users"));
                        String type=findtype(listOfFiles[i]); 
                        out.println("<tr><td><div class='content'><a  class='friendfile file"+jobj.get(request.getSession().getAttribute("user"))+"' href=\"DownloadFile?path="+path+"/"+ listOfFiles[i].getName()+"&file="+listOfFiles[i].getName()+"\" path='"+path+"/"+ listOfFiles[i].getName()+"' file='"+listOfFiles[i].getName()+"'>"+type + listOfFiles[i].getName()+"</a></div></td></tr>");
                    } 
                }    
            }
        }catch(Exception e){
            out.print("<script>alert(\"Error:"+e+"\");</script>");
        }finally{
         cp.free(con);
        }
        out.close();
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

    private String findtype(File file) {
        if(file.toString().contains(".zip")||file.toString().contains(".gz")||file.toString().contains(".bz2")||file.toString().contains(".xz")||file.toString().contains(".rar")||file.toString().contains(".tar")||file.toString().contains(".tgz")||file.toString().contains(".tbz2")||file.toString().contains(".z")||file.toString().contains(".7z"))
            return "<img src='IMAGES/zip.jpg' />";
        if(file.toString().contains(".mp3"))
            return "<img src='IMAGES/mp3.jpg' />";
        if(file.toString().contains(".xls")||file.toString().contains(".xlsx"))
            return "<img src='IMAGES/excel.jpg' />";
        if(file.toString().contains(".png")||file.toString().contains(".jpg")||file.toString().contains(".jpeg")||file.toString().contains(".gif")||file.toString().contains(".mpeg"))
            return "<img src='IMAGES/image.jpg' />";
        if(file.toString().contains(".pdf"))
            return "<img src='IMAGES/pdf.jpg' />";
        if(file.toString().contains(".ppt")||file.toString().contains(".pptx"))
            return "<img src='IMAGES/ppt.jpg' />";
        if(file.toString().contains(".txt")||file.toString().contains(".log"))
            return "<img src='IMAGES/txt.jpg' />";
        if(file.toString().contains(".doc")||file.toString().contains(".docx"))
            return "<img src='IMAGES/word.jpg' />";
        if(file.toString().contains(".html"))
            return "<img src='IMAGES/html.jpg' />";
        return "<img src='IMAGES/txt.jpg' />";
    }
}
