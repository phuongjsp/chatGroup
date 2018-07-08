package phuong.jsp.chatGroup.configuration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl ();
        tokenRepository.setDataSource (dataSource);
        return tokenRepository;
    }

    @Autowired
    void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
        authenticationManagerBuilder.
                userDetailsService (userDetailsService).
                passwordEncoder (passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests ()
                .antMatchers ("/webjars/**",
                        "/vendor/**",
                        "/css/**",
                        "/js/**",
                        "/scss/**",
                        "/pug/**", "/error", "/404",
                        "/resister").permitAll ()
                .anyRequest ().authenticated ()
                .and ()
                .formLogin ()
                .loginPage ("/login").permitAll ()
                .and ()
                .rememberMe ()
                .rememberMeParameter ("j_remember")

                .tokenRepository (persistentTokenRepository ())
                .and ().logout ().logoutUrl ("/logout").permitAll ().invalidateHttpSession (true)
                .and ().exceptionHandling ().accessDeniedPage ("/403");
        http.csrf ().disable ();
        http.headers ().frameOptions ().disable ();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean ();
    }
}
