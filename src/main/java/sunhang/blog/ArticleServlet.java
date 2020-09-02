/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunhang.blog;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.html.Attribute;
import com.vladsch.flexmark.util.html.Attributes;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sunhang
 */
public class ArticleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 如果不注释掉super，就会报错Servlet  error：HTTP method GET is not supported by this URL
        // super.doGet(req, resp);
        resp.setContentType("text/html;charset=utf-8");//设置编码格式，以防前端页面出现中文乱码

        String fileName = req.getParameter("file");
        ServletContext application = this.getServletContext();
        String path = (String) application.getInitParameter("markdown-path");

        File file = new File(path + File.separator + "_posts" + File.separator + fileName);
        resp.getWriter().print(convert(file));
    }

    private String convert(final File file) {
        final String content = readFile(file);

        // markdown to image
        try {
            MutableDataSet options = new MutableDataSet();
            options.setFrom(ParserEmulationProfile.MARKDOWN);
            options.set(Parser.EXTENSIONS, Arrays.asList(new Extension[]{TablesExtension.create()}));
            Parser parser = Parser.builder(options).build();
            HtmlRenderer renderer = HtmlRenderer.builder(options)
//                    .nodeRendererFactory()
//                    .linkResolverFactory()
//                    .htmlIdGeneratorFactory()
//                    .attributeProviderFactory(new IndependentAttributeProviderFactory() {
//                        @Override
//                        public AttributeProvider create(NodeRendererContext nodeRendererContext) {
//                            File rootDir = file.getParentFile().getParentFile();
//                            return new CustomAttributeProvider(rootDir);
//                        }
//                    })
                    .build();

            Node document = parser.parse(content);
            String html = renderer.render(document);
            System.out.println(html);

            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private String readFile(final File file) {
        // 从文件中读取markdown内容
        List<String> list = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

        // 兼容hexo生成的文章
        if (isFromHexo(list)) {
            int count = 6;
            while (count-- > 0) list.remove(0);
        }

        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private boolean isFromHexo(List<String> list) {
        if (list.size() < 6) return false;
        if (!list.get(0).equals("---")) return false;
        if (!list.get(1).startsWith("title:")) return false;
        if (!list.get(2).startsWith("date:")) return false;
        if (!list.get(3).startsWith("tags:")) return false;
        if (!list.get(4).startsWith("categories:") && !list.get(4).equals("---")) return false;
        if (list.get(4).startsWith("categories:") && !list.get(5).equals("---")) return false;

        return true;
    }

    /**
     * 处理标签的属性
     */
    static class CustomAttributeProvider implements AttributeProvider {
        private File rootDir;

        public CustomAttributeProvider(File rootDir) {
            this.rootDir = rootDir;
        }

        @Override
        public void setAttributes(Node node, AttributablePart attributablePart, Attributes attributes) {
            if (false == node instanceof Image || !"LINK".equals(attributablePart.getName())) {
                return;
            }

            if (true) return;

            File file = new File(rootDir, attributes.getValue("src"));
            attributes.replaceValue("src", file.getAbsolutePath());
        }
    }
}
