package com.dima.converter.config;

import com.dima.converter.utils.RealUrlClient;
import com.dima.converter.utils.UrlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/register", "/greeting").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);//.passwordEncoder(new BCryptPasswordEncoder());
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
    }

    @Bean
    public UrlClient urlClient(){
        //return new RealUrlClient();
        return new UrlClient() {
            @Override
            public InputStream getResponse(String url) throws IOException {
                String str = "{disclaimer: \"https://openexchangerates.org/terms/\"" +
                        "license: \"https://openexchangerates.org/license/\"," +
                "timestamp: 1449877801," +
                        "base: \"USD\"," +
                        "rates: {" +
                        "EUR: 3.672538," +
                        "CAD: 66.809999," +
                        "   ALL: 125.716501," +
                        "   AMD: 484.902502," +
                        "   ANG: 1.788575," +
                        "   AOA: 135.295998," +
                        "   ARS: 9.750101," +
                        "   AUD: 1.390866}}";
                String aa = "{\"abc\": 12}";
                return new ByteArrayInputStream(aa.getBytes(StandardCharsets.UTF_8));
            }
        };
    }
}