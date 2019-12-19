package cn.boommanpro.businessexample1.service;

import cn.boommanpro.common.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wangqimeng
 * @date 2019/12/18 17:34
 */
@FeignClient("business-example2")
public interface BusinessExample2Service {

    @RequestMapping("/api/businessExample2/test")
    ResultVo<Object> test();
}
