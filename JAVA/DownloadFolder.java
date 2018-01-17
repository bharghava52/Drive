package JAVA;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DownloadFolder", urlPatterns = {"/DownloadFolder"})
public class DownloadFolder extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sourceDirPath=request.getParameter("path");
        String zipFilePath=request.getParameter("file")+".zip";
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ZipHelper zippy = new ZipHelper();
        try {
            zippy.zipDir(sourceDirPath,zipFilePath);
            Path temp = Files.move(Paths.get(zipFilePath),Paths.get(sourceDirPath+".zip"));
            out.print("Zip file created");
        } catch(IOException e2) {
            System.err.println(e2);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
