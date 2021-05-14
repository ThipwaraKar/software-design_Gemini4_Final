package com.gemini4.loginpage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.gemini4.loginpage.controller.ScienceplanController;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;
    /*ADMIN*/
    @Configuration
    @Order(1)
    public class SecurityConfigurationAdmin extends WebSecurityConfigurerAdapter {

        SecurityConfigurationAdmin() {
            super();
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth.
                    jdbcAuthentication()
                    .usersByUsernameQuery(usersQuery)
                    .authoritiesByUsernameQuery(rolesQuery)
                    .dataSource(dataSource)
                    .passwordEncoder(bCryptPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.
                    authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/registration").permitAll()
                    .antMatchers("/Astronomer/").hasAnyAuthority("USER", "ADMIN","ASTRONOMER")
                    .antMatchers("/SciObserver/").hasAnyAuthority("USER", "ADMIN","ASTRONOMER","SCIOBSERVER")
                    .antMatchers("/admin/**").hasAnyAuthority("ADMIN","USER","ASTRONOMER","SCIOBSERVER").anyRequest()
                    //.antMatchers("/user/**").hasAnyAuthority("USER", "ADMIN")
                    //.antMatchers("/admin/**").hasAnyAuthority("ADMIN","USER").anyRequest()
                    //.antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                    .authenticated().and().csrf().disable().formLogin()

                    .loginPage("/login").permitAll().failureUrl("/login?error=true")
                    .defaultSuccessUrl("/homePage")
                   // .defaultSuccessUrl("/user/userHome")

                    .usernameParameter("email")
                    .passwordParameter("password")
                    /*.antMatchers("/user/**").hasAuthority("USER").anyRequest()
                    .authenticated().and().csrf().disable().formLogin()
                    .loginPage("/login").failureUrl("/login?error=true")
                    .defaultSuccessUrl("/user/userHome")
                    .usernameParameter("email")
                    .passwordParameter("password")*/
                    .and().logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/").and().exceptionHandling()
                    .accessDeniedPage("/access-denied");
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .maximumSessions(2)

            ;
        }
        @Override
        public void configure(WebSecurity web) throws Exception {

            web.ignoring()
                    .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/console/**");
        }
    }
        /*USER*/
        @Configuration
        @Order(2)
        public class SecurityConfigurationUser extends WebSecurityConfigurerAdapter {



            @Override
            protected void configure(AuthenticationManagerBuilder auth)
                    throws Exception {
                auth.
                        jdbcAuthentication()
                        .usersByUsernameQuery(usersQuery)
                        .authoritiesByUsernameQuery(rolesQuery)
                        .dataSource(dataSource)
                        .passwordEncoder(bCryptPasswordEncoder);
            }

            @Override
            protected void configure(HttpSecurity http) throws Exception {

               http //.antMatcher("/login/user")
                      //.authorizeRequests().anyRequest().hasRole("USER")
                        .authorizeRequests()
                        .antMatchers("/").permitAll()
                       .antMatchers("/login").permitAll()
                        .antMatchers("/login/user").permitAll()
                        .antMatchers("/registration").permitAll()




                        .antMatchers("/user/**").hasAuthority("USER")
                        .antMatchers("/registration").permitAll()
                        .and().formLogin()
                        .loginPage("/login/**").failureUrl("/login/**?error=true")
                        .defaultSuccessUrl("/user/userHome")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .and().logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/").permitAll().deleteCookies("JSESSIONID").and().exceptionHandling()
                        .accessDeniedPage("/access-denied").and().csrf().disable();

                http.sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(2)
                        .expiredUrl("/login/timeoutPage")

                ;
            }
            @Override
            public void configure(WebSecurity web) throws Exception {

                web.ignoring()
                        .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/console/**");
            }
        }


    }
