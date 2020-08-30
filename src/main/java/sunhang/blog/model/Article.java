package sunhang.blog.model;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 需要继承Serializable，否则Article不可以传递到request.setAttribute中
 *
 * @author sunhang
 *
 */
public class Article implements Serializable {

    private String title;
    private String time;
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return title + " " + time;
    }

    public static List<Article> fakeData() {
        final List<Article> list = new ArrayList<Article>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i = 0; i < 10; i++) {
            Article article = new Article();
            article.setTitle("title " + i);
            article.setTime(sdf.format(new Date()));
            list.add(article);
        }

        return list;
    }
}
