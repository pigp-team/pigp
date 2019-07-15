package com.feng.pigp.fans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author feng
 * @date 2019/7/15 15:01
 * @since 1.0
 */

@Controller
@RequestMapping("/sina")
public class SinaController {

    @RequestMapping("/submitTask")
    public boolean submitTask(){

        return true;
    }

    @RequestMapping("/cancelTask")
    public boolean cancelTask(){

        return true;
    }
}