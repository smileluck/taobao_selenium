package com.zsmile.selenium;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zsmile.entity.CheckHomeGoodEntity;
import com.zsmile.lib.OkHttpUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class TBHomeSearchS implements Runnable {
    private SeleniumConfig seleniumConfig;
    private WebDriver webDriver;

    public TBHomeSearchS(SeleniumConfig seleniumConfig) {
        this.seleniumConfig = seleniumConfig;
        this.webDriver = new SeleniumUtil().runBrower(seleniumConfig);
    }

    private String searchHomeUrl = "https://s.taobao.com/search?imgfile=&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=a21bo.2017.201856-taobao-item.1&ie=utf8&initiative_id=tbindexz_20170306&bcoffset=3&ntoffset=3&p4ppushleft=1%2C48&sort=sale-desc";

    //    @Transactional
    public void request() {
        try {
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

            JavascriptExecutor driverJs = ((JavascriptExecutor) webDriver);
            //利用js代码键入搜索关键字
            driverJs.executeScript("() =>{ Object.defineProperties(navigator,{ webdriver:{ get: () => false } }) }");
            driverJs.executeScript("() =>{ window.navigator.chrome = { runtime: {},  }; }");
            driverJs.executeScript("() =>{ Object.defineProperty(navigator, 'languages', { get: () => ['en-US', 'en'] }); }");
            driverJs.executeScript(" () =>{ Object.defineProperty(navigator, 'plugins', { get: () => [1, 2, 3, 4, 5,6], }); }");


            webDriver.get("https://login.taobao.com/member/login.jhtml?f=top&redirectURL=https://www.taobao.com");
            Thread.sleep(5000);
            WebElement usernameElement = webDriver.findElement(By.id("fm-login-id"));
            usernameElement.sendKeys(seleniumConfig.getTaobaoAccount());

            WebElement passwordElement = webDriver.findElement(By.id("fm-login-password"));
            passwordElement.sendKeys(seleniumConfig.getTaobaoPwd());
            webDriver.findElement(By.className("fm-submit")).click();
            Thread.sleep(5000);
            if (checkElement(webDriver, By.id("nc_1_n1z"))) {
                Actions action = new Actions(webDriver);
                WebElement hk = webDriver.findElement(By.id("nc_1_n1z"));
                action.moveToElement(hk).clickAndHold(hk);
//                action.moveByOffset(200, 0).perform();
                action.dragAndDropBy(hk
                        , 258,
                        0).pause(2000).perform();
                webDriver.findElement(By.className("fm-submit")).click();
            }
            Thread.sleep(5000);

            do {
                if (webDriver.getCurrentUrl().contains("login.taobao.com/member/login.jhtml")) {
                    Thread.sleep(10000);
                } else if (webDriver.getCurrentUrl().contains("login.taobao.com/member/login_unusual.htm")) {
                    Thread.sleep(5000);
                    if (checkElement(webDriver, By.xpath("//*[@id=\"content\"]/div/div[1]/iframe"))) {
//                            Thread.sleep(2000);
                        WebElement iframe = webDriver.findElement(By.xpath("//*[@id=\"content\"]/div/div[1]/iframe"));
                        webDriver.switchTo().frame(iframe);
                        WebElement otherValidator = webDriver.findElement(By.id("otherValidator"));
                        otherValidator.click();
                        Thread.sleep(2000);
                        List<WebElement> liList = webDriver.findElements(By.xpath("//*[@id=\"content\"]/div/ol/li"));
                        for (WebElement webElement : liList) {
                            String text = webElement.findElement(By.className("text")).getText();
                            if (text.contains("手机验证码")) {
                                WebElement a = webElement.findElement(By.tagName("a"));
                                a.click();
                                break;
                            }
                        }
                        Thread.sleep(2000);
                        WebElement jGetCode = webDriver.findElement(By.id("J_GetCode"));
                        jGetCode.click();
                    } else {
                        if (checkElement(webDriver, By.id("J_Phone_Checkcode"))) {
                            WebElement jPhoneCheckcode = webDriver.findElement(By.id("J_Phone_Checkcode"));
                            jPhoneCheckcode.click();
                            WebElement submitBtn = webDriver.findElement(By.id("submitBtn"));
                            submitBtn.click();
                        }
                    }
                    Thread.sleep(10000);
                } else if (webDriver.getCurrentUrl().contains("www.taobao.com")) {
                    break;
                } else {
                    break;
                }
            } while (true);

            get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
            webDriver = null;

        }
    }

    private void get() {
        while (true) {
            String resString = OkHttpUtil.get(this.seleniumConfig.getApiGetPath(), "");
            if ("".equalsIgnoreCase(resString)) {
                break;
            }
            JSONObject parseObject = JSON.parseObject(resString);
            if (parseObject.getInteger("code") != 0) {
                break;
            }
            JSONObject info = parseObject.getJSONObject("info");
            if (info == null) {
                break;
            }
            search(info);
        }
    }

    private void search(JSONObject info) {
        try {
            webDriver.get(searchHomeUrl + "&q=" + info.getString("name"));
            Thread.sleep(10000);
            // 没有找到与xxxx相关的宝贝
            //
            Boolean cboolean = checkElement(webDriver, By.className("combobar-noquery"));
            Boolean aBoolean = checkElement(webDriver, By.className("item-not-found"));
            if (aBoolean || cboolean) {
                String res = OkHttpUtil.postJson(seleniumConfig.getApiSubmitPath(), info.toJSONString());
                if ("".equalsIgnoreCase(res)) {
                    return;
                }
                JSONObject jsonObject = JSON.parseObject(res);
                if (jsonObject.getInteger("code") == 0) {
                    return;
                }
                return;
            }

            List<CheckHomeGoodEntity> checkHomeGoodEntities = new ArrayList<>();
            WebElement items = webDriver.findElement(By.xpath("//div[@id=\"mainsrp-itemlist\"]/div[1]/div[1]/div[1]"));
            List<WebElement> mouserOnverReq = items.findElements(By.className("J_MouserOnverReq"));
            for (WebElement webElement : mouserOnverReq) {
                CheckHomeGoodEntity checkHomeGoodEntity = new CheckHomeGoodEntity();

                WebElement picLink = webElement.findElement(By.className("pic-link"));
                String detailUrl = picLink.getAttribute("data-href");
                WebElement img = picLink.findElement(By.tagName("img"));
                String picUrl = img.getAttribute("src");
                String title = img.getAttribute("alt");

                String viewPrice = webElement.findElement(By.className("price")).findElement(By.tagName("strong")).getText();
                String viewSales = webElement.findElement(By.className("deal-cnt")).getText();
                if (viewSales.indexOf("+") > -1) {
                    String substring = viewSales.substring(0, viewSales.indexOf("+"));
//                    Matcher matcher = pattern.matcher(substring);
                    if (substring.indexOf("万") > -1) {
                        checkHomeGoodEntity.setViewSales2((int) (Double.parseDouble(substring.substring(0, substring.length() - 1)) * 10000));
                    } else {
                        checkHomeGoodEntity.setViewSales2(Integer.parseInt(substring.substring(0, substring.length() - 1)));
                    }
                } else if (viewSales.equalsIgnoreCase("")) {
                    checkHomeGoodEntity.setViewSales2(0);
                } else {
                    checkHomeGoodEntity.setViewSales2(Integer.parseInt(viewSales.replaceAll("\\D", "")));
                }
                String itemLoc = webElement.findElement(By.className("location")).getText();
//                String rawTitle = webElement.findElement(By.xpath("//*[@class=\"J_ClickStat\"]")).getText();
                String shopLink = webElement.findElement(By.className("shop")).findElement(By.tagName("a")).getAttribute("href");
                String nick = webElement.findElement(By.className("ww-small")).getAttribute("data-nick");
                checkHomeGoodEntity.setRawTitle(title);
                checkHomeGoodEntity.setViewPrice(new BigDecimal(viewPrice));
                checkHomeGoodEntity.setItemLoc(itemLoc);
                checkHomeGoodEntity.setViewSales(viewSales);
                checkHomeGoodEntity.setDetailUrl(detailUrl);
                if (detailUrl.indexOf("#detail") > -1) {
                    detailUrl = detailUrl.replaceAll("#detail", "");
                }
                checkHomeGoodEntity.setCommentUrl(detailUrl + "&on_comment=1");
                checkHomeGoodEntity.setNick(nick);
                checkHomeGoodEntity.setTitle(title);
                checkHomeGoodEntity.setShopLink(shopLink);
                checkHomeGoodEntity.setPicUrl(picUrl);
                checkHomeGoodEntities.add(checkHomeGoodEntity);
            }
            info.put("checkHomeGoodEntities", checkHomeGoodEntities);
            String res = OkHttpUtil.postJson(seleniumConfig.getApiSubmitPath(), info.toJSONString());
            if ("".equalsIgnoreCase(res)) {
                return;
            }
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getInteger("code") == 0) {
                return;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public void search(WebDriver webDriver, LexiconEntity lexiconEntity, LexiconVersionEntity lexiconVersionEntity) {
//        try {
//            webDriver.get(searchHomeUrl + "&q=" + lexiconEntity.getName());
////        WebElement sortSaleDesc = webDriver.findElement(By.xpath("//*[@id=\"J_relative\"]/div[1]/div/ul/li[2]/a"));
////        sortSaleDesc.click();
////            webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
//            Thread.sleep(6000);
////        if (checkElement(webDriver, By.xpath("//div[@id=\"J_sufei\"]/iframe"))) {
////            webDriver.switchTo().frame(webDriver.findElement(By.xpath("//div[@id=\"J_sufei\"]/iframe")));
////            WebElement yzbutton = webDriver.findElement(By.id("nc_1_n1z"));
////            Actions actions = new Actions(webDriver);
////            actions.clickAndHold(yzbutton).perform();
////            actions.moveByOffset(100, 0);
////
////        }//*[@id="mainsrp-itemlist"]/div/div/div[1]
////            WebElement element = webDriver.findElement(By.xpath("//*[@id=\"J_relative\"]/div[1]/div/ul/li[2]/a"));
//            WebElement items = webDriver.findElement(By.xpath("//div[@id=\"mainsrp-itemlist\"]/div[1]/div[1]/div[1]"));
//            List<WebElement> mouserOnverReq = items.findElements(By.className("J_MouserOnverReq"));
//            Date date = new Date();
////            if (mouserOnverReq.size() >= 40) {
//            CheckEntity checkEntity = new CheckEntity();
//            checkEntity.setCreateTime(date);
//            checkEntity.setLexiconId(lexiconEntity.getId());
//            checkEntity.setLexiconVersionId(lexiconVersionEntity.getId());
////        checkEntity.setLexiconName(lexiconEntity.getName());
//            checkService.save(checkEntity);
//            for (WebElement webElement : mouserOnverReq) {
//                CheckHomeGoodEntity checkHomeGoodEntity = new CheckHomeGoodEntity();
//                checkHomeGoodEntity.setCheckId(checkEntity.getId());
//
//                WebElement picLink = webElement.findElement(By.className("pic-link"));
//                String detailUrl = picLink.getAttribute("data-href");
//                WebElement img = picLink.findElement(By.tagName("img"));
//                String picUrl = img.getAttribute("src");
//                String title = img.getAttribute("alt");
//
//                String viewPrice = webElement.findElement(By.className("price")).findElement(By.tagName("strong")).getText();
//                String viewSales = webElement.findElement(By.className("deal-cnt")).getText();
//                if (viewSales.indexOf("+") > -1) {
//                    String substring = viewSales.substring(0, viewSales.indexOf("+"));
////                    Matcher matcher = pattern.matcher(substring);
//                    if (substring.indexOf("万") > -1) {
//                        checkHomeGoodEntity.setViewSales2((int) (Double.parseDouble(substring.substring(0, substring.length() - 1)) * 10000));
//                    } else {
//                        checkHomeGoodEntity.setViewSales2(Integer.parseInt(substring.substring(0, substring.length() - 1)));
//                    }
//                } else if (viewSales.equalsIgnoreCase("")) {
//                    checkHomeGoodEntity.setViewSales2(0);
//                } else {
//                    checkHomeGoodEntity.setViewSales2(Integer.parseInt(viewSales.replaceAll("\\D", "")));
//                }
//                String itemLoc = webElement.findElement(By.className("location")).getText();
////                String rawTitle = webElement.findElement(By.xpath("//*[@class=\"J_ClickStat\"]")).getText();
//                String shopLink = webElement.findElement(By.className("shop")).findElement(By.tagName("a")).getAttribute("href");
//                String nick = webElement.findElement(By.className("ww-small")).getAttribute("data-nick");
//                checkHomeGoodEntity.setCheckId(checkEntity.getId());
//                checkHomeGoodEntity.setRawTitle(title);
//                checkHomeGoodEntity.setViewPrice(new BigDecimal(viewPrice));
//                checkHomeGoodEntity.setItemLoc(itemLoc);
//                checkHomeGoodEntity.setViewSales(viewSales);
//                checkHomeGoodEntity.setDetailUrl(detailUrl);
//                if (detailUrl.indexOf("#detail") > -1) {
//                    detailUrl = detailUrl.replaceAll("#detail", "");
//                }
//                checkHomeGoodEntity.setCommentUrl(detailUrl + "&on_comment=1");
//
//
//                checkHomeGoodEntity.setNick(nick);
//                checkHomeGoodEntity.setTitle(title);
//                checkHomeGoodEntity.setShopLink(shopLink);
//                checkHomeGoodEntity.setPicUrl(picUrl);
//                checkHomeGoodService.save(checkHomeGoodEntity);
//            }
//            lexiconEntity.setCheckNum(lexiconEntity.getCheckNum() + 1);
//            lexiconEntity.setCheckLastId(checkEntity.getId());
//            lexiconEntity.setCheckLastTime(date);
//            lexiconEntity.setVersionId(lexiconVersionEntity.getId());
//            lexiconService.updateById(lexiconEntity);
////            }
//            //*[@id="J_sufei"]/iframe
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//
//        }
//    }


    private Boolean checkElement(WebDriver webDriver, By seletor) {
        try {
            webDriver.findElement(seletor);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    @Override
    public void run() {
        request();
    }
}
