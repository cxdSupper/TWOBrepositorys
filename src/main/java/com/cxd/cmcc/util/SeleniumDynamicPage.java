package com.cxd.cmcc.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import com.cxd.cmcc.Info;
import com.cxd.cmcc.TakeGoldPro;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

//@Component
public class SeleniumDynamicPage {

    public static HashMap<String, String> autoSet = new HashMap<>();
    {
        autoSet.put("cxdtel","15170252724");
        autoSet.put("lltel", "15279772652");
        autoSet.put("cxd", "0");
        autoSet.put("ll", "0");
    }
    public void getLiulag(String name) {

        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "D:\\LenovoQMDownload\\chromedriver_win32\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*")
                //禁用自动化软件控制标识。一些网站会检测 window.navigator.webdriver 来反爬虫，不设置此项时可能会报403禁止访问
                .addArguments("--disable-blink-features=AutomationControlled")
                // 不使用沙箱模式
                .addArguments("--no-sandbox")
                // 禁用浏览器插件，可以提高执行效率
                .addArguments("--disable-plugins")
                // 无界面模式，可以提高执行效率
//                .addArguments("--headless")
                // 用户代理，headless模式下需要设置此参数，不然可能会报403禁止访问
                .addArguments("--user-agent='Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36'")
                // 禁用GPU加速
                .addArguments("--disable-gpu")
                //如果页面含有大量图片，可能因为加载图片太多导致超时报错，可以禁用加载图片
                //.addArguments("blink-settings=imagesEnabled=false")
                // http代理
                //.addArguments("--proxy-server=127.0.0.1:8888")
                // 使用无痕模式
                // .addArguments("--incognito")
                // 窗口最大化，防止网页排版不同造成元素获取、截图等方面的问题
                // .addArguments("--start-maximized")
        ;
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("https://shop.10086.cn/i/?f=home&c=12&e=791");

            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("f=home&c=12&e=791")){
                WebElement jPc = driver.findElement(By.id("J_pc"));
                jPc.click();
                Thread.sleep(50);
                WebElement sms_name = driver.findElement(By.id("sms_name"));
                sms_name.sendKeys(autoSet.get(name+"tel"));

                File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                // 保存到项目路径下
                FileUtil.copy(file, new File("img.png"), true);

                WebElement rememberMe = driver.findElement(By.id("rememberMe"));
                rememberMe.click();
                WebElement regText_bg_new = driver.findElement(By.id("regText_bg_new"));
                regText_bg_new.click();
                WebElement regText_bg_person = driver.findElement(By.id("regText_bg_person"));
                regText_bg_person.click();



                Thread.sleep(15000);

                //图像验证码
                WebElement smspwd_err = driver.findElement(By.id("inputCode2"));

                String smspwd = getLoginParam("smsImg");
                smspwd_err.sendKeys(smspwd);


                // 获取验证码按钮
                WebElement getSMSPwd1 = driver.findElement(By.id("getSMSPwd1"));
                getSMSPwd1.click();

                String captchaError2 = driver.findElement(By.id("captcha_error2")).getText();
                int i = 1;
                while ("验证码有误".equals(captchaError2) && i<4){
                    driver.findElement(By.id("captchaImg2")).click();

                    smspwd = getLoginParam("smsImg");
                    smspwd_err.sendKeys(smspwd);

                    // 获取验证码按钮
                    getSMSPwd1.click();
                }

                Thread.sleep(10000);

                String smsValue = getLoginParam("sms");

                // 输入短信验证码
                WebElement sms_pwd_l = driver.findElement(By.id("sms_pwd_l"));
                sms_pwd_l.sendKeys(smsValue);

                WebElement submitBt = driver.findElement(By.id("submit_bt"));
                submitBt.click();
                Thread.sleep(1000);

                currentUrl = driver.getCurrentUrl();
                System.out.println(currentUrl);
            }


            int i = 0;
            while (currentUrl.contains("f=home&c=12&e=791") && autoSet.get(name)!=null) {
                try {
                    WebElement liul = driver.findElement(By.xpath("//*[@id=\"stc_packramin\"]/div[2]/span[3]/span[1]"));
                    Float value = Float.valueOf(liul.getText());
                    Log.get().info("剩余流量：{}G",value);
                    if (value.compareTo(Float.valueOf(autoSet.get(name))) < 0) {
                        String result = TakeGoldPro.exchange("V200", Info.infoMap.get(name));
                        Log.get().info(result);
                    }
                    // 20分钟执行一次

                    driver.findElement(By.id("topA1")).click();
                    Thread.sleep(1000*60*1);
                } catch (Exception e) {
                    Log.get().error(e.getMessage());
                    if (i++>3){
                        driver.quit();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }
    }

    private  String getLoginParam(String key) throws IOException, InterruptedException {

        int i=1;
        do {
            String value = autoSet.get(key);
            if (value !=null) {
                autoSet.put(key, null);
                return value;
            }else{
                Thread.sleep(15000);
            }


        }while (i++<4 );

        // 失败结束
        throw new RuntimeException("填写验证码失败");
    }
}