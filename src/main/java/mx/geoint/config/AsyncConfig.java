package mx.geoint.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); //El minimo de hilo a usar
        executor.setMaxPoolSize(2); //El maximo de hilo a crear despues de estos 4 estan ocuapdos
        executor.setQueueCapacity(50); //Cantidad de hilo en esperar
        executor.setThreadNamePrefix("AsyncThread-"); // Nombre del hilo
        executor.initialize();
        return executor;
    }
}
