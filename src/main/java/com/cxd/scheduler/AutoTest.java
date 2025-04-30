package com.cxd.scheduler;

import cn.hutool.http.HttpUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author caixd
 * @date 2025/4/30
 * @desc
 */
@Component
public class AutoTest {

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void test() {
        int millis = 1000 * 60;
        int i = 1;

        while (i<10) {
            String s = HttpUtil.get("www.baidu.com");

            System.out.println(s);
            i++;
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
