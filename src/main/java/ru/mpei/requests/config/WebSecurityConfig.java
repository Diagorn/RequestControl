package ru.mpei.requests.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mpei.requests.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //Making the role system work
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { //Configuring Spring Security
    @Autowired
    private UserService userService; //Used for loading user by his username

    @Bean //Encoding passwords
    public PasswordEncoder getPasswordEncoder () { //Creating a bean of the real password encoder
        return new BCryptPasswordEncoder(8);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception { //Setting up the security
        http
                .authorizeRequests() //Authorising all server requests
                    .antMatchers("/", "/registration", "/static/**").permitAll() //except these
                    .anyRequest().authenticated() //All others must be authorised
                .and()
                    .formLogin()//Showing that we have our own login form
                    .loginPage("/login")//By this mapping
                    .permitAll()//All can use it
                .and()
                    .logout() //Logout can be used by anyone
                    .permitAll();
//                .and()
//                    .rememberMe(); //For not to enter the login and password
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService) //Loading users with UserService
                .passwordEncoder(passwordEncoder); //Encoding passwords
    }
}