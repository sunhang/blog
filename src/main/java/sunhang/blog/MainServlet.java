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
import javax.servlet.http.HttpSession;

public class MainServlet extends HttpServlet {

    public static final int COUNT_PY_PAGE = 20;

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
        // 如果不存在 session 会话，则创建一个 session 对象
        HttpSession session = req.getSession(true);
        List<Article> list = (List<Article>) session.getAttribute("articles");
        if (list == null) {
            synchronized (MainServlet.class) {
                list = getArticles();
            }
            session.setAttribute("articles", list);
        }
        int visitorCount = getVisitorCount(session.isNew());
        int pageIndex = getPageIndex(req);
        List<Article> result = getSubList(list, pageIndex);
        req.setAttribute("is_from_mobile", Utils.isMobileDevice(req));
        req.setAttribute("articles", result);
        req.setAttribute("pre_page", getPrePage(pageIndex));
        req.setAttribute("next_page", getNextPage(pageIndex, list.size()));
        req.setAttribute("visitor_count", visitorCount);
        // 参数不能写成"/index.jsp"，不然参数传递不过去
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    private int getVisitorCount(boolean isSessionNew) {
        ServletContext context = getServletContext();   // 获得ServletContext对象
        Integer count = (Integer) context.getAttribute("counter");   // 从ServletContext中获得计数器对象
        if (count == null) {      // 如果为空，则在ServletContext中设置一个计数器的属性.即在第一次提交请求时创建该属性
            count = 0;
        }
        if (isSessionNew) {
            count++;
        }
        context.setAttribute("counter", count);
        return count;
    }

    private List<Article> getArticles()
            throws Exception {
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

        return list;
    }

    private int getPageIndex(HttpServletRequest req) {
        String str = req.getParameter("page_index");
        if (str == null) {
            str = "0";
        }
        return Integer.parseInt(str);
    }

    private List<Article> getSubList(List<Article> list, int pageIndex) {
        int from = pageIndex * COUNT_PY_PAGE;
        int to = (pageIndex + 1) * COUNT_PY_PAGE;
        if (to > list.size()) {
            to = list.size();
        }

        if (from > list.size()) {
            return new ArrayList<>();
        } else {
            return list.subList(from, to);
        }
    }

    private int getPrePage(int pageIndex) {
        int result = pageIndex - 1;
        if (result < -1) {
            result = -1;
        }
        return result;
    }

    private int getNextPage(int pageIndex, int totalSize) {
        int result = pageIndex + 1;
        if (result * COUNT_PY_PAGE >= totalSize) {
            return -1;
        }
        return result;
    }
}
