package cn.boommanpro.businessexample2.controller;

import cn.boommanpro.common.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangqimeng
 * @date 2019/12/4 20:45
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping
    public ResultVo test(){
        log.info("businessExample2:{}","test");
        return ResultVo.ofSuccess();
    }
}
