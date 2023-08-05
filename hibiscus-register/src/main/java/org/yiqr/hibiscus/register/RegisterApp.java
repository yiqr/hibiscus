package org.yiqr.hibiscus.register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author rose
 * @create 2023/7/17 17:08
 */
@Slf4j
@EnableEurekaServer
@EnableDiscoveryClient
@SpringBootApplication
public class RegisterApp {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(RegisterApp.class);
        Environment env = app.run(args).getEnvironment();
        log.info(
                """
                        
                ----------------------------------------------------------
                        Application profiles active: '{}'
                        Application '{}' is running! Access URLs:
                        Local:http://127.0.0.1:{}
                        External: http://{}:{}
                ----------------------------------------------------------""",
                env.getProperty("spring.profiles.active"),
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http.csrf().ignoringRequestMatchers("/eureka/**");
    //     // http.csrf().ignoringAntMatchers("/eureka/**");
    //     // http.csrf().disable()
    //     //         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //     //         .and()
    //     //         .authorizeHttpRequests(
    //     //         )
    //     //         .requestMatchers("/user/account/token/", "/user/account/register/","/websocket/**").permitAll()
    //     //         .requestMatchers("/pk/start/game/", "/pk/receive/bot/move/" ).access(hasIpAddress("127.0.0.1"))
    //     //         .requestMatchers(HttpMethod.OPTIONS).permitAll()
    //     //         .anyRequest().authenticated();
    //     // http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    //     return http.build();
    // }
    //
    // private static AuthorizationManager<RequestAuthorizationContext> hasIpAddress(String ipAddress) {
    //     IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ipAddress);
    //     return (authentication, context) -> {
    //         HttpServletRequest request = context.getRequest();
    //         return new AuthorizationDecision(ipAddressMatcher.matches(request));
    //     };
    // }
}
