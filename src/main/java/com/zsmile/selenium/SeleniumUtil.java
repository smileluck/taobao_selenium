package com.zsmile.selenium;

import com.google.common.collect.Lists;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SeleniumUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public WebDriver runBrower(SeleniumConfig seleniumConfig) {
        WebDriver webDriver = null;
        if ("chrome".equals(seleniumConfig.getBrowserType())) {
            webDriver = runChrome(seleniumConfig);
        } else if ("firefox".equals(seleniumConfig.getBrowserType())) {
            webDriver = runFirefox(seleniumConfig);
        }
        return webDriver;
    }

    //启动火狐
    private WebDriver runFirefox(SeleniumConfig seleniumConfig) {
        try {
            System.setProperty("webdriver.firefox.bin", seleniumConfig.getBrowserPath());
            System.setProperty("webdriver.gecko.driver", seleniumConfig.getDriverPath());
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addPreference("excludeSwitches", "enable-automation");
            firefoxOptions.addPreference("permissions.default.image", 2);
            firefoxOptions.addArguments("--disable-infobars");

            return new FirefoxDriver(firefoxOptions);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 启动谷歌浏览器
     *
     * @return
     */
    private WebDriver runChrome(SeleniumConfig seleniumConfig) {
        try {
            System.setProperty("webdriver.chrome.driver", seleniumConfig.getDriverPath());

            String osName = System.getProperty("os.name").toLowerCase();
            ChromeOptions chromeOptions = new ChromeOptions();
            if (osName.indexOf("linux") > -1) {
                //设置chrome浏览器无界面模式
                // 浏览器不提供可视化页面. linux下如果系统不支持可视化不加这条会启动失败

//                System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
                chromeOptions.setHeadless(Boolean.TRUE);

//                chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
//                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--disable-dev-shm-usage");
//            chromeOptions.addArguments("window-size=1920x3000");
//                chromeOptions.addArguments("--disable-infobars");
//                chromeOptions.addArguments("--disable-browser-side-navigation");
//                chromeOptions.addArguments("start-maximized");
//                chromeOptions.addArguments("enable-automation");


//            chromeOptions.addArguments("--disable-web-security");

                List<String> excludeSwitches = Lists.newArrayList("enable-automation");
                chromeOptions.setExperimentalOption("excludeSwitches", excludeSwitches);
            } else if (osName.indexOf("win") > -1) {

                chromeOptions.setBinary(seleniumConfig.getBrowserPath());
//                chromeOptions.setHeadless(Boolean.TRUE);
//                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("lang=zh_CN.UTF-8");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                List<String> excludeSwitches = Lists.newArrayList("enable-automation");
                chromeOptions.setExperimentalOption("excludeSwitches", excludeSwitches);

                chromeOptions.addArguments("--disable-web-security");

            }

            chromeOptions.addArguments("blink-settings=imagesEnabled=false");
            return new ChromeDriver(chromeOptions);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
