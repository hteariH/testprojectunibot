package com.bravemamoru.testprojectunibot;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.RecordParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class NamesVerticle extends AbstractVerticle {

    private Set<String> names = new HashSet<>();

    @Autowired
    ResourceLoader loader;

    @Override
    public void start() throws Exception {
        super.start();
        OpenOptions options = new OpenOptions();
        String path = loader.getResource("classpath:names.txt").getFile().getPath();;
        AsyncFile asyncFile = vertx.fileSystem().openBlocking(path, options);
        RecordParser recordParser = RecordParser.newDelimited("\n", bufferedLine -> {
            String s = bufferedLine.toString();
            String substring = s.substring(0, s.indexOf(' '));
            names.add(substring);
        });
        asyncFile.handler(recordParser);
        asyncFile.endHandler(h -> asyncFile.close());

        EventBus eventBus = vertx.eventBus();

       eventBus.<String>consumer("name").handler(readRnd());
    }

    private Handler<Message<String>> readRnd() {
        return event -> {
            if(names.contains(event.body())){
                System.out.println(event.body());
            }
        };
    }
}
