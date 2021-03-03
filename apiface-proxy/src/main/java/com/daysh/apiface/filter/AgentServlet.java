package com.daysh.apiface.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.filter
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 07:19
 * @Description: <p>  </p>
 */

/**
 * @Description: <p> 代理类 </p>
 * @ClassName: AgentFilter
 * @ClassAllName: com.daysh.apiface.filter.AgentFilter
 * @Author： Daye Shing
 * @Date： 2020-08-14 07:19
 */
@Slf4j
public class AgentServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        log.debug("init");
        super.init();
    }

    @Override
    public void destroy() {
        log.debug("destroy");
        super.destroy();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handler(request, response);
    }

    /**
     * handler
     * @description: <p> 操作 </p>
     * @methodName handler
     * @param req -
     * @param resp -
     * @return void -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 8:00
     * @since 1.0
     */
    protected void handler(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求编码，响应不需设置编码，响应编码是终端服务确定的
        req.setCharacterEncoding("UTF-8");
        //让浏览器用utf8来解析返回的数据
        resp.setHeader("Content-Type", "text/html;charset=UTF-8");
        //告诉servlet用UTF-8转码，而不是用默认的ISO8859
        resp.setCharacterEncoding("UTF-8");
        String url = req.getHeader("Self-Agent-Address");
        log.debug("代理地址:{}",url);
        if(StringUtils.isNotEmpty(url)){
            try {
                fetch(req, resp, url);
            } catch (Exception e) {
                resp.setStatus(500);
                log.error("代理错误:{}",e);
            }
            log.debug("代理成功");
            return;
        }
        log.debug("404");
        resp.setStatus(404);
    }

    /**
     * fetch
     * @description: <p> 获取远程资源 </p>
     * @methodName fetch
     * @param req -
     * @param resp -
     * @param url -
     * @return void -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 8:00
     * @since 1.0
     */
    protected void fetch(HttpServletRequest req, HttpServletResponse resp, String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
        setHeaders(req, conn);
        writeInputStream(req, conn);
        addRespHeaders(conn, resp);
        writeBytes(conn, resp);
    }

    /**
     * writeInputStream
     * @description: <p> req 写入conn </p>
     * @methodName writeInputStream
     * @param req -
     * @param conn -
     * @return void -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 8:00
     * @since 1.0
     */
    protected void writeInputStream(HttpServletRequest req, HttpURLConnection conn) throws IOException {
        String method = req.getMethod();
        conn.setRequestMethod(req.getMethod());
        conn.setUseCaches(false);
        if ("GET".equalsIgnoreCase(method)) {
            return;
        } else {
            conn.setDoOutput(true);
        }
        InputStream in = req.getInputStream();
        byte[] b = new byte[1024];
        int len = -1;
        OutputStream out = conn.getOutputStream();
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
            out.flush();
        }
        out.close();
    }

    /**
     * writeBytes
     * @description: <p> conn 写出 resp </p>
     * @methodName writeBytes
     * @param conn -
     * @param resp -
     * @return void -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 8:01
     * @since 1.0
     */
    protected void writeBytes(HttpURLConnection conn, HttpServletResponse resp) throws IOException {
        OutputStream out = resp.getOutputStream();
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            out.flush();
        }
        if (out != null) {
            out.close();
        }
        conn.disconnect();
    }

    /**
     * addRespHeaders
     * @description: <p> 添加响应头 </p>
     * @methodName addRespHeaders
     * @param conn -
     * @param resp -
     * @return void -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 8:02
     * @since 1.0
     */
    protected void addRespHeaders(HttpURLConnection conn, HttpServletResponse resp) {
        String contentType = conn.getContentType();
        String encoding = conn.getContentEncoding();
        if (contentType != null && "".equals(contentType.trim())) {
            resp.setContentType(contentType);
        }
        if (encoding != null && !"".equals(encoding.trim())) {
            resp.setCharacterEncoding(encoding);
        }
        int i = 0;
        boolean hasMore = true;
        while (hasMore) {
            String key = conn.getHeaderFieldKey(i);
            if (key != null && key.equals("Transfer-Encoding")) {
                i++;
                continue;
            }
            String value = conn.getHeaderField(i);
            if (key != null) {
                resp.addHeader(key, value);
            }
            if (value == null && key == null) {
                hasMore = false;
            }
            i++;
        }
    }

    /**
     * setHeaders
     * @description: <p> 添加请求头 </p>
     * @methodName setHeaders
     * @param req -
     * @param conn -
     * @return void -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 8:02
     * @since 1.0
     */
    protected void setHeaders(HttpServletRequest req, HttpURLConnection conn) {
        Enumeration<String> hNames = req.getHeaderNames();
        while (hNames.hasMoreElements()) {
            String name = hNames.nextElement();
            if ("Cache-Control".equalsIgnoreCase(name)) {
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("pragma", "no-cache");
                continue;
            }
            // 设置不使用缓存，主要是这个起效
            if ("if-none-match".equalsIgnoreCase(name) || "if-modified-since".equalsIgnoreCase(name) || "cookie-x".equalsIgnoreCase(name)) {
                continue;
            }
            conn.addRequestProperty(name, req.getHeader(name));
        }
        if (req.getContentType() != null) {
            conn.setRequestProperty("Content-Type", req.getContentType());
        }
    }

}
