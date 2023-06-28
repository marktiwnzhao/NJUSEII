package com.mikumifa.wms.service.impl;

import com.mikumifa.wms.service.CodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 23:29
 **/
@Service
public class CodeServiceImpl implements CodeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeServiceImpl.class);
    @Override
    public boolean sendCode(String email) {
        //目前假设都是000000
        Integer code = 0;
        LOGGER.info("成功发送验证码 "+code+" 到 "+email);
        return true;
    }

    @Override
    public boolean verifyCode(String email, String code) {
        //假设都是对的
        LOGGER.info("发送到 "+email+" 的验证码 "+code+" 正确");
        return true;
    }
}
