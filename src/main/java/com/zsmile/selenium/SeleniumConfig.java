package com.zsmile.selenium;

import lombok.Data;

@Data
public class SeleniumConfig {
    private String browserType;
    private String browserPath;
    private String driverPath;
    private String taobaoAccount;
    private String taobaoPwd;
    private String apiGetPath;
    private String apiSubmitPath;
}
