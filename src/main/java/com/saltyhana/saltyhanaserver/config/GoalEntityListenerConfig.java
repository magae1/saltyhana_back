package com.saltyhana.saltyhanaserver.config;

import com.saltyhana.saltyhanaserver.listener.GoalEntityListener;
import com.saltyhana.saltyhanaserver.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
@Configuration
@RequiredArgsConstructor
public class GoalEntityListenerConfig {
    private final GoalEntityListener goalEntityListener;

    @Autowired
    public void init(ProgressService progressService) {
        goalEntityListener.setProgressService(progressService);
    }
}
