package com.cxd.scheduler;

import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.cxd.cmcc.Info;
import com.cxd.cmcc.TakeGoldPro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author caixd
 * @date 2023/9/22
 * @desc
 */
@Component
public class TakeGoldScheduler  {

    @Value("${takeNum}")
    private Double number;

    @Value("${llToken}")
    public void setllToken(String token){
        Info.ll.setToken(token);
    }
    @Value("${cxdToken}")
    public void setcxdToken(String token){
        Info.cxd.setToken(token);
    }


    @Scheduled(cron = "${job.cron}")
    public void takeGold() throws IOException {
        HttpUtil.get("https://mytool.koyeb.app/take/ll");
        HttpUtil.get("https://mytool.koyeb.app/take/cxd");

    }
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void testJob() throws IOException {
        System.out.println("测试");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        for (Map.Entry<String, Info> entry : Info.infoMap.entrySet()) {
            Log.get().info("【{}】捡金币开始",entry.getValue().getName());
            TakeGoldPro.phone = entry.getValue().getPhone();
            TakeGoldPro.token = entry.getValue().getToken();
            TakeGoldPro.name = entry.getValue().getName();
            TakeGoldPro.start(1d);
            TakeGoldPro.takeTotal = 0;
            TakeGoldPro.currentTotal = 0d;
            TakeGoldPro.balance = 0d;
        }
        /*for (int i = 0; i < 1; i++) {
            System.out.println(TakeGoldPro.exchange("V200", Info.ll));
            Thread.sleep(1000);

        }*/
    }

}
