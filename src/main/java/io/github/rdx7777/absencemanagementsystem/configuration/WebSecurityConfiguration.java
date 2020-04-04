package io.github.rdx7777.absencemanagementsystem.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Value("${spring.security.user.name}")
    private String adminName;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    public WebSecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("SELECT email, password, is_active FROM users WHERE email=?")
            .authoritiesByUsernameQuery("SELECT email, role FROM users WHERE email=?")
            .passwordEncoder(passwordEncoder());

        auth.inMemoryAuthentication()
            .withUser(adminName)
            .password(passwordEncoder().encode(adminPassword))
            .roles("ADMIN");
    }

//    TODO: change hasAnyRole to hasRole in lines 57-59
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/users", "/api/users/*").hasAnyRole("CS_SUPERVISOR", "HEAD_TEACHER", "HR_SUPERVISOR", "ADMIN")
            .antMatchers(HttpMethod.POST, "/api/users").hasAnyRole("HEAD_TEACHER", "ADMIN")/*.hasRole("ADMIN")*/
            .antMatchers(HttpMethod.PUT, "/api/users/*").hasAnyRole("HEAD_TEACHER", "ADMIN")/*.hasRole("ADMIN")*/
            .antMatchers(HttpMethod.DELETE, "/api/users/*").hasAnyRole("HEAD_TEACHER", "ADMIN")/*.hasRole("ADMIN")*/
            .antMatchers(HttpMethod.GET, "/api/cases", "/api/cases/", "/api/cases/active", "/api/cases/active/", "/api/cases/active/ht/*", "/api/cases/active/ht/*/").hasAnyRole("CS_SUPERVISOR", "HEAD_TEACHER", "HR_SUPERVISOR", "ADMIN")
            .antMatchers(HttpMethod.GET, "/api/cases/{%d}").hasAnyRole("USER", "CS_SUPERVISOR", "HEAD_TEACHER", "HR_SUPERVISOR", "ADMIN")
            .antMatchers(HttpMethod.GET, "/api/cases/user").hasAnyRole("USER", "CS_SUPERVISOR", "HEAD_TEACHER", "HR_SUPERVISOR", "ADMIN")
            .antMatchers(HttpMethod.POST, "/api/cases").hasAnyRole("USER", "CS_SUPERVISOR", "HEAD_TEACHER", "HR_SUPERVISOR", "ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/cases").hasAnyRole("USER", "CS_SUPERVISOR", "HEAD_TEACHER", "HR_SUPERVISOR", "ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/cases").hasRole("ADMIN")
            .anyRequest().fullyAuthenticated()
            .and()
            .httpBasic()
            .and()
            .formLogin().permitAll()
            .and()
            .logout().permitAll()
            .and()
            .cors()
            .disable()
            .csrf()
            .disable()
        ;
    }
}
