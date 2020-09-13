/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunhang.blog;

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

}
