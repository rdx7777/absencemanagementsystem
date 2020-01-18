package io.github.rdx7777.absencemanagementsystem.configuration;

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

    @Value("${spring.security.user.name}")
    private String adminName;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser(adminName).password(passwordEncoder().encode(adminPassword)).roles("ADMIN")
            .and().withUser("user").password(passwordEncoder().encode("user")).roles("USER")
            .and().withUser("cs_supervisor").password(passwordEncoder().encode("cs")).roles("CS_SUPERVISOR")
            .and().withUser("head_teacher").password(passwordEncoder().encode("ht")).roles("HEAD_TEACHER")
            .and().withUser("hr_supervisor").password(passwordEncoder().encode("hr")).roles("HR_SUPERVISOR");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/users").hasAnyRole("CS_SUPERVISOR", "HEAD_TEACHER", "HR_SUPERVISOR", "ADMIN")
            .antMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/users").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/users").hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/api/cases", "/api/cases/", "/api/cases/active", "/api/cases/active/", "/api/cases/active/ht/*", "/api/cases/active/ht/*/").hasAnyRole("CS_SUPERVISOR", "HEAD_TEACHER", "HR_SUPERVISOR", "ADMIN")
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
            .disable() // umożliwia dostęp poprzez klientów innych, niż przeglądarki
            // = wyłączenie opcji Spring Security zapobiegającej atakom CSRF
             // działa pod endpointem "/logout"
        ;
    }
}
