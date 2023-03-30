package com.msr.better.security.filter;

import com.msr.better.security.secret.PasswordEncryptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: maisrcn@qq.com
 * @change:
 * @date: 2023-03-22 14:09:13
 */
public class CustomLoginFilter extends AbstractAuthenticationProcessingFilter {

    private PasswordEncryptor passwordEncryptor;

    public CustomLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public CustomLoginFilter(String defaultFilterProcessesUrl, PasswordEncryptor passwordEncryptor) {
        super(defaultFilterProcessesUrl);
        this.passwordEncryptor = passwordEncryptor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        return null;
    }
}
