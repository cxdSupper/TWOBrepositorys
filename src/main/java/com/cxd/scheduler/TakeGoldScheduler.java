package com.cxd.scheduler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import com.cxd.cmcc.Info;
import com.cxd.cmcc.TakeGoldPro;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Map;

/**
 * @author caixd
 * @date 2023/9/22
 * @desc
 */
//@Component
public class TakeGoldScheduler  {

    @Scheduled(cron = "0 8 9,14 * * ? ")
    public static void takeGold() throws IOException {
        for (Map.Entry<String, Info> entry : Info.infoMap.entrySet()) {
            Log.get().info("user【{}】捡金币开始",entry.getValue().getName());
            TakeGoldPro.phone = entry.getValue().getPhone();
            TakeGoldPro.token = entry.getValue().getToken();
            TakeGoldPro.name = entry.getValue().getName();
            TakeGoldPro.start();
            TakeGoldPro.takeTotal = 0;
            TakeGoldPro.currentTotal = 0d;
            TakeGoldPro.balance = 0d;
        }

    }

    public static void main(String[] args) throws IOException {
//        takeGold();
        JSONObject json = new JSONObject();
        json.set("msgType", "2.78.1");
        json.set("version", "1|138");
        json.set("createTime", DateUtil.now());
        JSONObject content = new JSONObject();
        JSONObject content2 = new JSONObject();
        content2.set("accessToken", Info.cxd.getToken());
        content2.set("cellphone", Info.cxd.getPhone());
//        content2.set("channel", "woxin");
//        content2.set("smsCode", "769806");
        content2.set("exchangeCount", "V100");
        content2.set("veriSmsTransId", "79010050631326");
        content.set("content", content2);
        json.set("content", content);
        HttpRequest post = HttpUtil.createPost("https://woxin2.jx139.com/interface/MsgPort");
        post.header("interfaceCode", "2.78.1");
        post.body(json.toJSONString(0));

        HttpResponse execute = post.execute();
        String body = execute.body();

    }

//    @Override
//    public void run(String... args) throws Exception {
//        takeGold();
//    }
}
