package com.demo.authserver.configuration;
import com.demo.authserver.configuration.auth_providers.UsernameAndPasswordAuthProvider;
import com.demo.authserver.configuration.filters.JwtTokenVerifier;
import com.demo.authserver.model.exceptions.RestAccessDeniedHandler;
import com.demo.authserver.model.exceptions.RestAuthenticationEntryPoint;
import com.demo.authserver.service.JwtService;
import com.demo.authserver.service.PrivilegeService;
import com.demo.authserver.service.RoleService;
import com.demo.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Configuration
@EnableWebSecurity
@EnableSwagger2
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private RestAccessDeniedHandler accessDeniedHandler;
    private RestAuthenticationEntryPoint unauthorizedHandler;
    private static final List<String> DEFAULT_PERMIT_ALL = Collections.unmodifiableList(Collections.singletonList("*"));

    private final UsernameAndPasswordAuthProvider usernameAndPasswordAuthProvider;
    private final JwtService jwtService;
    private final UserService userService;
    private final PrivilegeService privilegeService;
    private final RoleService roleService;
    @Autowired
    public WebSecurityConfig(UsernameAndPasswordAuthProvider usernameAndPasswordAuthProvider, JwtService jwtService, UserService userService, PrivilegeService privilegeService, RoleService roleService) {
        this.usernameAndPasswordAuthProvider = usernameAndPasswordAuthProvider;
        this.jwtService = jwtService;
        this.userService = userService;
        this.privilegeService = privilegeService;
        this.roleService = roleService;
    }
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http.cors().configurationSource(corsConfigurationSource())
                .and().csrf().disable().
                authorizeRequests()
                .antMatchers("/oauth/**", "/admin/**", "/api/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(unauthorizedHandler)
                .and()
                    .addFilterAfter(new JwtTokenVerifier(jwtService, userService, privilegeService,roleService), UsernamePasswordAuthenticationFilter.class)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /// @formatter:on
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(DEFAULT_PERMIT_ALL);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Origin","Resource","Request_Type", "Content-Type", "Accept", "whitelist", "X-Requested-With", "Origin", "Authorization", "Accept-Encoding", "X-Auth-Token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(usernameAndPasswordAuthProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
