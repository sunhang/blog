/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunhang.blog;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sunhang
 */
public class ArticleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 如果不注释掉super，就会报错Servlet  error：HTTP method GET is not supported by this URL
        // super.doGet(req, resp);
        resp.setContentType("text/html;charset=utf-8");//设置编码格式，以防前端页面出现中文乱码
        
        String fileName = req.getParameter("file");
        ServletContext application=this.getServletContext();  
        String path = (String)application.getInitParameter("markdown-path");
        
        File file = new File(path + File.separator + "_posts" + File.separator + fileName);
        resp.getWriter().print(convert(file));
    }

    private String convert(File file) {
        String content = null;
        // 从文件中读取markdown内容
        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            content = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

        // markdown to image
        try {
            MutableDataSet options = new MutableDataSet();
            options.setFrom(ParserEmulationProfile.MARKDOWN);
            options.set(Parser.EXTENSIONS, Arrays.asList(new Extension[]{TablesExtension.create()}));
            Parser parser = Parser.builder(options).build();
            HtmlRenderer renderer = HtmlRenderer.builder(options).build();

            Node document = parser.parse(content);
            String html = renderer.render(document);

            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
