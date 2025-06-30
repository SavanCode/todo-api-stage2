package com.example.todoapi.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {

    private final Environment environment;

    public StartupListener(Environment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String port = environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        String activeProfiles = environment.getActiveProfiles().length > 0 ? 
                               String.join(", ", environment.getActiveProfiles()) : "default";
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎉 Todo API Application Started Successfully!");
        System.out.println("=".repeat(60));
        System.out.println("📍 Local URL:     http://localhost:" + port + contextPath);
        System.out.println("📚 Swagger UI:    http://localhost:" + port + contextPath + "/swagger-ui/index.html");
        System.out.println("📖 API Docs:      http://localhost:" + port + contextPath + "/v3/api-docs");
        System.out.println("🗄️  H2 Console:    http://localhost:" + port + contextPath + "/h2-console");
        System.out.println("🔐 JWT Auth:      Enabled");
        System.out.println("📊 Database:      H2 (In-Memory)");
        System.out.println("⚡ Environment:   " + activeProfiles);
        System.out.println("=".repeat(60));
        System.out.println("🚀 Ready to handle requests!");
        System.out.println("=".repeat(60) + "\n");
    }
} 