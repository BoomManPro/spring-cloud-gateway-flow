package cn.boommanpro.businessexample1.controller;

import cn.boommanpro.common.ResultVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangqimeng
 * @date 2019/12/4 20:45
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping
    public ResultVo test(){
        return ResultVo.ofSuccess();
    }
}
