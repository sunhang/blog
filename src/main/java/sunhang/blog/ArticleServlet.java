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
        File posts = null;
        try {
            posts = Utils.getPosts(this);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.writeException(resp.getWriter(), e);
            return;
        }

        File file = new File(posts, fileName);
        List<String> lines = readFile(file);
        String title;
        // 兼容hexo生成的文章
        if (!isFromHexo(lines)) {
            title = fileName;
        } else {
            // 获取标题
            title = lines.get(1).replace("title:", "").trim();

            // 删除前边冗余的行
            int count = redundantLineCount(lines);
            while (count-- > 0) {
                lines.remove(0);
            }
        }

        req.setAttribute("is_from_mobile", Utils.isMobileDevice(req));
        req.setAttribute("title", title);
        req.setAttribute("content", convert(toString(lines)));
        req.getRequestDispatcher("article.jsp").forward(req, resp);
    }

    private String convert(String content) {
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
//            System.out.println(html);

            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * 从文件中读取markdown内容，每一行放入到列表中
     *
     * @param file
     * @return
     */
    private List<String> readFile(final File file) {
        List<String> list = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private String toString(final List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private boolean isFromHexo(List<String> list) {
        if (list.size() < 6) {
            return false;
        }
        if (!list.get(0).equals("---")) {
            return false;
        }
        if (!list.get(1).startsWith("title:")) {
            return false;
        }
        if (!list.get(2).startsWith("date:")) {
            return false;
        }
        if (!list.get(3).startsWith("tags:")) {
            return false;
        }
        if (!list.get(4).startsWith("categories:") && !list.get(4).equals("---")) {
            return false;
        }
        if (list.get(4).startsWith("categories:") && !list.get(5).equals("---")) {
            return false;
        }

        return true;
    }

    /**
     * 前提是hexo文章
     *
     * @param list
     * @return
     */
    private int redundantLineCount(List<String> list) {
        if (list.get(4).equals("---")) {
            return 5;
        }

        if (list.get(5).equals("---")) {
            return 6;
        }

        return -1;
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

            if (true) {
                return;
            }

            File file = new File(rootDir, attributes.getValue("src"));
            attributes.replaceValue("src", file.getAbsolutePath());
        }
    }
}
