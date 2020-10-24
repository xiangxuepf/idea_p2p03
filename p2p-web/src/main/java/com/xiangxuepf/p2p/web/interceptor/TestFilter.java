package com.xiangxuepf.p2p.web.interceptor;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author mhw
 */

/**
 * 2. 生命周期的方法：

     1). init方法：

       在创建完过滤器对象之后被调用。只执行一次

       注：过滤器在Web服务器中也是单例模式

     2). doFilter方法：

       执行过滤任务方法。执行多次。

     3). destroy方法：

       Web服务器停止或者Web应用重新加载，销毁过滤器对象。

 */
public class TestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
