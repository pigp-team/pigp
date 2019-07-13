package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.MultiGoal;
import com.feng.pigp.fans.model.SingletonGoal;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author feng
 * @date 2019/7/10 9:29
 * @since 1.0
 */
@Service
public class GoalPoolService {

    private static Logger LOGGER = LoggerFactory.getLogger(GoalPoolService.class);
    private static final String FILE = "goal.pwd";
    private static final String USERNAMEFLAG = "#&#userName#^#=";
    private List<MultiGoal> multiGoals = Lists.newArrayList();

    @PostConstruct
    public void init() throws IOException {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        String userName = null;
        while((line=br.readLine())!=null){
            if(line.contains(USERNAMEFLAG)){
                userName = line.replace(USERNAMEFLAG, "");
                continue;
            }

            MultiGoal goal = new MultiGoal();
            goal.setUrl(line);
            goal.setUserName(userName);
            submitMulti(goal);
        }

        LOGGER.info("goal load finish :{}", multiGoals.size());
    }

    public synchronized List<SingletonGoal> getAllGaols() {
        return Lists.newArrayList();
    }

    public void submitMulti(MultiGoal goal){

        multiGoals.add(goal);
    }

    public List<MultiGoal> getMulti(){
        return multiGoals;
    }
}