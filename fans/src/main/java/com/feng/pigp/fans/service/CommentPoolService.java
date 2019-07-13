package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.Goal;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import jdk.internal.util.xml.impl.Input;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

/**
 * @author feng
 * @date 2019/7/12 14:03
 * @since 1.0
 */
@Service
public class CommentPoolService {

    private static final Random RANDOM = new Random();
    private static final List<String> list = Lists.newArrayList();
    private static final String FILE = "comment.txt";
    private String defaultValue;
    private int size;

    @PostConstruct
    public void init() throws IOException {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = br.readLine();
        if(StringUtils.isEmpty(line)){
            return;
        }

        defaultValue = line;

        while((line=br.readLine())!=null){

            list.add(line);
        }

        size = list.size();
    }

    public String queryCommentWithShare(Goal goal, String topic){

        String randomMessage = getRandomMesage();
        return "#"+topic+"#@"+ goal.getUserName()+defaultValue+randomMessage;
    }


    public String queryCommentWithComment(Goal goal, String topic){

        return defaultValue + getRandomMesage() + "@" + goal.getUserName();
    }

    public String queryCommentWithInternalComment(Goal goal, String topic){

        return defaultValue + getRandomMesage();
    }

    public String getRandomMesage() {

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<15; i++){
            sb.append(list.get(RANDOM.nextInt(size)));
        }
        return sb.toString();
    }
}