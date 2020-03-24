package io.github.rdx7777.absencemanagementsystem.configuration;

import io.github.rdx7777.absencemanagementsystem.model.User;
import io.github.rdx7777.absencemanagementsystem.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;
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

    private final UserRepository repository;

    @Value("${spring.security.user.name}")
    private String adminName;

    @Value("${spring.security.user.password}")
    private String adminPassword;

    public WebSecurityConfiguration(DataSource dataSource, UserRepository repository) {
        this.dataSource = dataSource;
        this.repository = repository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public void setTestUser() {
        User user = User.builder()
            .withName("Jack")
            .withSurname("Lynn")
            .withEmail("jack@test.com")
            .withPassword("$2a$10$u3AJC2e8fQ7bapCZh6I6Re4siOLimyBkPp.E//Ae07CSdW1SrRrFu")
            .withJobTitle("Math teacher")
            .withIsActive(true)
            .withRole("USER")
            .build();
        Collection<String> userEmails = repository.findAll().stream().map(User::getEmail).collect(Collectors.toList());
        if (userEmails.contains(user.getEmail())) {
            repository.deleteById(repository.findUserByEmail(user.getEmail()).get().getId());
            repository.save(user);
        }
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//            .withUser(adminName).password(passwordEncoder().encode(adminPassword)).roles("ADMIN")
//            .and().withUser("user").password(passwordEncoder().encode("user")).roles("USER")
//            .and().withUser("cs_supervisor").password(passwordEncoder().encode("cs")).roles("CS_SUPERVISOR")
//            .and().withUser("head_teacher").password(passwordEncoder().encode("ht")).roles("HEAD_TEACHER")
//            .and().withUser("hr_supervisor").password(passwordEncoder().encode("hr")).roles("HR_SUPERVISOR");
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
//            .usersByUsernameQuery("SELECT email, passwordHash, active FROM users WHERE email=?")
//            .authoritiesByUsernameQuery("SELECT email, authority FROM authorities WHERE email=?")
            .usersByUsernameQuery("SELECT email, password, is_active FROM users WHERE email=?")
            .authoritiesByUsernameQuery("SELECT email, role FROM users WHERE email=?")
            .passwordEncoder(passwordEncoder());

        auth.inMemoryAuthentication()
            .withUser(adminName)
            .password(passwordEncoder().encode(adminPassword))
            .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/users", "/api/users/*").hasAnyRole("CS_SUPERVISOR", "HEAD_TEACHER", "HR_SUPERVISOR", "ADMIN")
            .antMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/api/users/*").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/api/users/*").hasRole("ADMIN")
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
            .disable() // umożliwia dostęp poprzez klientów innych, niż przeglądarki
        // = wyłączenie opcji Spring Security zapobiegającej atakom CSRF
        // działa pod endpointem "/logout"
        ;
    }
}
