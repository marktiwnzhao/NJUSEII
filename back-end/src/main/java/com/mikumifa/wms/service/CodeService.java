package com.mikumifa.wms.service;

public interface CodeService {
    public boolean sendCode(String email);
    public boolean verifyCode(String email,String code);
}
