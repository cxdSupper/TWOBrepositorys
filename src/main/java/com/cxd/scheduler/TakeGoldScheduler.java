package com.cxd.scheduler;

import cn.hutool.log.Log;
import com.cxd.cmcc.Info;
import com.cxd.cmcc.TakeGoldPro;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TakeGoldScheduler {

    @Scheduled(cron = "0 55 10,14 * * ? ")
    public void takeGold() throws IOException {
        for (Map.Entry<String, Info> entry : Info.infoMap.entrySet()) {
            Log.get().info("user【{}】捡金币开始",entry.getValue().getName());
            TakeGoldPro.phone = entry.getKey();
            TakeGoldPro.token = entry.getValue().getToken();
            TakeGoldPro.name = entry.getValue().getName();
            TakeGoldPro.start();
            TakeGoldPro.takeTotal = 0;
            TakeGoldPro.currentTotal = 0d;
            TakeGoldPro.balance = 0d;
        }

        if (TakeGoldPro.flag) {
            TakeGoldPro.flag = false;
        }else {
            TakeGoldPro.flag = true;
        }
    }


}
