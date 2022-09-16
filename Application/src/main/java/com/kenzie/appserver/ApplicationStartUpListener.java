package com.kenzie.appserver;


import com.kenzie.appserver.service.SongService;
import com.kenzie.appserver.service.model.Song;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ApplicationStartUpListener {

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Perform any application start-up tasks
        SongService songService = event.getApplicationContext().getBean(SongService.class);
    }
}
