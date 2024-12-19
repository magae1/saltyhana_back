package com.saltyhana.saltyhanaserver;

import java.util.stream.IntStream;

import com.saltyhana.saltyhanaserver.entity.Icon;
import com.saltyhana.saltyhanaserver.repository.IconRepository;
import com.saltyhana.saltyhanaserver.repository.UserRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Log4j2
@Component
public class AppStartupListener implements ApplicationListener<ApplicationReadyEvent> {
    private final IconRepository iconRepo;

    public AppStartupListener(IconRepository iconRepo) {
        this.iconRepo = iconRepo;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        
    }

}
