package org.example.configuration;

import org.reflections.Reflections;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.persistence.Entity;
import java.util.Set;

public class SessionFactoryUtil {
    private static SessionFactory sessionFactory;
    private static final Logger log = LogManager.getLogger(SessionFactoryUtil.class);

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();

            // Auto-scan all @Entity classes in the org.example.entity package
            Reflections reflections = new Reflections("org.example.entity");
            Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
            
            for (Class<?> entityClass : entityClasses) {
                configuration.addAnnotatedClass(entityClass);
                log.info("Registered entity: " + entityClass.getSimpleName());
            }
            
            ServiceRegistry serviceRegistry =
                    new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}