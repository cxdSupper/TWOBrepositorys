package com.cxd.scheduler;

import com.cxd.cmcc.Constant;
import com.cxd.cmcc.Info;
import com.cxd.cmcc.TakeGoldPro;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;

/**
 * @author caixd
 * @date 2025/8/18
 * @desc
 */
@Component
public class AutoRedeemJob {

    @PostConstruct
    private void init() {
        HashSet<String> list = new HashSet<>();
        list.add("cxd");
        Constant.setValue("autoList", list);
    }

    @Scheduled(cron = "0 20 7 * * ?")
    public void RedeemGold() {
        HashSet<String> autoList = (HashSet<String>) Constant.getValue("autoList");
        for (String name : autoList) {
            System.out.println("开始执行自动兑换");
            TakeGoldPro.exchange("V200", Info.infoMap.get(name));
        }
    }

}
