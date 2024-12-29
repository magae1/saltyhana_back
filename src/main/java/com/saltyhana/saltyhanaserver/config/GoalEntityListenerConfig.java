package com.saltyhana.saltyhanaserver.config;

import com.saltyhana.saltyhanaserver.listener.GoalEntityListener;
import com.saltyhana.saltyhanaserver.listener.TransferEntityListener;
import com.saltyhana.saltyhanaserver.service.ProgressService;
import com.saltyhana.saltyhanaserver.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
@Configuration
@RequiredArgsConstructor
public class GoalEntityListenerConfig {
    private final GoalEntityListener goalEntityListener;
    private final TransferEntityListener transferEntityListener;

    @Autowired
    public void init(ProgressService progressService, TransferService transferService) {
        goalEntityListener.setProgressService(progressService);
        transferEntityListener.setTransferService(transferService);
    }
}
