package com.bravemamoru.testprojectunibot;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.OpenOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StringGeneratorVerticle extends AbstractVerticle {

    @Autowired
    Random random;

    @Override
    public void start() throws Exception {
        super.start();
        OpenOptions options = new OpenOptions().setWrite(false).setCreate(false);
        vertx.fileSystem().open("/dev/urandom", options, result -> {
            if (result.succeeded()) {
                result.result().setReadBufferSize(11).handler(this::createName);
            }
        });

    }

    private void createName(Buffer data) {
        byte[] bytes = data.getBytes();
        char[] chars = new char[nextRandomSizeOfName()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (Math.abs(bytes[i]) % 26 + 65);
        }
        vertx.eventBus().send("name", String.valueOf(chars));
    }

    private int nextRandomSizeOfName() {
        return random.nextInt(9) + 2;
    }
}
