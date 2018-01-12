package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "FriendDatas", urlPatterns = {"/FriendDatas"})
public class FriendDatas extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();
        String root=request.getParameter("root");
        String path=request.getParameter("path");
        String path1[]=path.split("/");
        out.println("<caption><input type='hidden' id='root' value='"+root+"'></input><input type='hidden' id='path' value='"+path+"'></input></caption>");
        if(path1.length>1){
            String path2="";
            for(int i=0;i<path1.length-1;i++)
                path2+=path1[i]+"/";
            path2=path2.substring(0,path2.length()-1);
            if(path2.equals(root))
                out.println("<tr><td>"+"<a href='#' class='frienddata' path='"+path2+"'><--Back</a></td></tr>");
            else
                out.println("<tr><td>"+"<a href='#' class='frienddatas' path='"+path2+"'><--Back</a></td></tr>");
        }
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) 
        {
            if(listOfFiles[i].isDirectory()) 
            {
                out.println("<tr><td>"+"<a href='#' class='frienddatas' path='"+path+"/"+ listOfFiles[i].getName()+"'>" + listOfFiles[i].getName()+"</a></td></tr>");
            }
        }
        for (int i = 0; i < listOfFiles.length; i++) 
        {
           if (listOfFiles[i].isFile()) 
            {
                out.println("<tr><td>"+"<a  class='friendfile' href=\"DownloadFile?path="+path+"/"+ listOfFiles[i].getName()+"&file="+listOfFiles[i].getName()+"\" path='"+path+"/"+ listOfFiles[i].getName()+"' file='"+listOfFiles[i].getName()+"'>" + listOfFiles[i].getName()+"</a></td></tr>");
            } 
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

}
