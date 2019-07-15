package com.feng.pigp.fans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author feng
 * @date 2019/7/15 15:01
 * @since 1.0
 */

@Controller
@RequestMapping("/sina")
public class SinaController {

    @RequestMapping("/task/submit")
    @ResponseBody
    public boolean submitTask(){

        return true;
    }

    @RequestMapping("/task/cancel")
    @ResponseBody
    public boolean cancelTask(){

        return true;
    }
}