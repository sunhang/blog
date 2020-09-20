package sunhang.blog;

import java.io.File;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sunhang.blog.model.Article;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 如果不注释掉super，就会报错Servlet  error：HTTP method GET is not supported by this URL
        // super.doGet(req, resp);
        try {
            handleGet(req, resp);
        } catch (Exception e) {
            Utils.writeException(resp.getWriter(), e);
            e.printStackTrace();
        }
    }

    private void handleGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        final List<Article> list = new ArrayList<Article>();
        File posts = Utils.getPosts(this); 

//        2020-01-15-关于android异步编程的学习.md
        for (File file : posts.listFiles()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String fileName = file.getName();
            Pattern pattern = Pattern.compile("(\\d{4}\\-\\d{1,2}\\-\\d{1,2})\\-(([u4e00-u9fa5]|.)*)\\.md");
            Matcher matcher = pattern.matcher(fileName);

            if (!matcher.find()) {
                System.out.println(fileName);
                continue;
            }

            Article article = new Article();
            article.setTime(matcher.group(1));
            article.setTitle(matcher.group(2));
            article.setFile(file);

            list.add(article);
        }

        Collections.sort(list, new Comparator<Article>() {

            @Override
            public int compare(Article o1, Article o2) {
                // 倒序，往前从近到远
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        req.setAttribute("is_from_mobile", Utils.isMobileDevice(req));
        req.setAttribute("articles", list);
        // 参数不能写成"/index.jsp"，不然参数传递不过去
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
