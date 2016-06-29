package com.dima.converter.config;

import com.dima.converter.utils.UrlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/register", "/greeting", "/mng").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    public UrlClient urlClient(){
        //return new RealUrlClient();
        return new UrlClient() {
            @Override
            public InputStream getResponse(String url) throws IOException {
                String str = "{" +
                        "  \"disclaimer\": \"Exchange rates are provided for informational purposes only, and do not constitute financial advice of any kind. Although every attempt is made to ensure quality, NO guarantees are given whatsoever of accuracy, validity, availability, or fitness for any purpose - please use at your own risk. All usage is subject to your acceptance of the Terms and Conditions of Service, available at: https://openexchangerates.org/terms/\"," +
                        "  \"license\": \"Data sourced from various providers with public-facing APIs; copyright may apply; resale is prohibited; no warranties given of any kind. All usage is subject to your acceptance of the License Agreement available at: https://openexchangerates.org/license/\"," +
                        "  \"timestamp\": 1207929600," +
                        "  \"base\": \"USD\"," +
                        "  \"rates\": {" +
                        "    \"AED\": 3.67315," +
                        "    \"AFN\": 46.39045," +
                        "    \"ALL\": 77.480705," +
                        "    \"AMD\": 305.973327," +
                        "    \"ANG\": 1.7825," +
                        "    \"AOA\": 74.834839," +
                        "    \"ARS\": 3.156525," +
                        "    \"AUD\": 1.075909," +
                        "    \"AWG\": 1.79025," +
                        "    \"AZN\": 0.829274," +
                        "    \"UAH\": 4.985052," +
                        "    \"UGX\": 1682.752653," +
                        "    \"USD\": 1," +
                        "    \"UYU\": 22.165937," +
                        "    \"UZS\": 1300.61896," +
                        "    \"VEF\": 2.1473," +
                        "    \"VND\": 16111.045096," +
                        "    \"VUV\": 93.35026," +
                        "    \"WST\": 2.545512," +
                        "    \"XAF\": 460.198682," +
                        "    \"XCD\": 2.667385," +
                        "    \"XDR\": 0.607572," +
                        "    \"XOF\": 414.348672," +
                        "    \"XPF\": 75.459488," +
                        "    \"YER\": 198.625371," +
                        "    \"ZAR\": 7.825643," +
                        "    \"ZMK\": 3550.253774," +
                        "    \"ZWD\": 29996.045128" +
                        "  }" +
                        "}";
                return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
            }
        };
    }
}