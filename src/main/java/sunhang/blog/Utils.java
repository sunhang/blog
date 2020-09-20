/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunhang.blog;

import java.io.File;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sunhang
 */
public class Utils {

    private Utils() {
    }

    public static boolean isMobileDevice(HttpServletRequest req) {
        String ua = req.getHeader("user-agent");
        return ua.contains("Android") || ua.contains("iPhone");
    }

    public static File getPosts(HttpServlet servlet) throws Exception {
        ServletContext application = servlet.getServletContext();
        String param = (String) application.getInitParameter("markdown-path");
        String[] paths = param.split(":");
        File posts = null;
        for (String path : paths) {
            File file = new File(new File(path), "_posts");
            if (file.exists() && file.isDirectory()) {
                posts = file;
                break;
            }
        }
        if (posts == null || posts.listFiles() == null) {
            throw new RuntimeException("listFiles return null");
        }

        return posts;
    }

    public static void writeException(PrintWriter writer, Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement ele : e.getStackTrace()) {
            sb.append(ele.getClassName())
                    .append("#").append(ele.getMethodName()).append("+").append(ele.getLineNumber()).append("\n");
        }
        writer.print(e.toString() + "\n" + sb.toString());
    }

}
