package com.cxd.scheduler;

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
        for (Map.Entry<String, Info> entry : Info.infoMap.entrySet()) {
            Log.get().info("user【{}】捡金币开始",entry.getValue().getName());
            TakeGoldPro.phone = entry.getValue().getPhone();
            TakeGoldPro.token = entry.getValue().getToken();
            TakeGoldPro.name = entry.getValue().getName();
            TakeGoldPro.start(number);
            TakeGoldPro.takeTotal = 0;
            TakeGoldPro.currentTotal = 0d;
            TakeGoldPro.balance = 0d;
        }

    }

    public static void main(String[] args) throws IOException {
        for (Map.Entry<String, Info> entry : Info.infoMap.entrySet()) {
            Log.get().info("user【{}】捡金币开始",entry.getValue().getName());
            TakeGoldPro.phone = entry.getValue().getPhone();
            TakeGoldPro.token = entry.getValue().getToken();
            TakeGoldPro.name = entry.getValue().getName();
            TakeGoldPro.start(2d);
            TakeGoldPro.takeTotal = 0;
            TakeGoldPro.currentTotal = 0d;
            TakeGoldPro.balance = 0d;
        }

    }

//    @Override
//    public void run(String... args) throws Exception {
//        takeGold();
//    }
}
