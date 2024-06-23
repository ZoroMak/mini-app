package org.example.miniapp.configuration;

import lombok.extern.slf4j.Slf4j;
import org.example.miniapp.ApiCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;

@Configuration
@Slf4j
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

    @Autowired
    private ApiCaller apiCaller;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        apiCaller.callAllApis();

        callMethodOnce();
    }

    private static void callMethodOnce() {
        log.info("Все задачи выполнены останавливаем приложение.");
        System.exit(0);
    }
}

