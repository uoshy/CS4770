package chat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import javax.servlet.ServletException;

import java.util.Date;

public class LogFilter implements Filter {
    private FilterConfig filterConfig;
    private ServletContext context;

    public void doFilter (
        ServletRequest request,
        ServletResponse response,
        FilterChain chain )
        throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest)request;
        if ( context != null ) 
            context.log( "LOG:" + req.getRemoteHost() + ":" + req.getRequestURI() );
        chain.doFilter(request, response);
    }

    public void init (FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.context = filterConfig.getServletContext();
    }

    public void destroy () {
        this.filterConfig = null;
        this.context = null;
    }
}
