package com.cxd.cmcc.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import com.cxd.cmcc.Info;
import com.cxd.cmcc.TakeGoldPro;
import com.cxd.cmcc.util.SeleniumDynamicPage;
import com.cxd.cmcc.vo.MsgResponse;
import com.cxd.cmcc.vo.RecMsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author caixd
 * @date 2023/9/23
 * @desc
 */
@RestController
public class TakeController {
    Log log = Log.get();

    @Value("${takeNum}")
    Double takeNum;



    @Autowired
    SeleniumDynamicPage seleniumDynamicPage;

    @RequestMapping("/take/{user}")
    public String takeGold(@PathVariable String user) throws IOException {
        switch (user){
            case "cxd": {
                TakeGoldPro.phone = Info.cxd.getPhone();
                TakeGoldPro.token = Info.cxd.getToken();
                TakeGoldPro.name = "辛迪";
                break;
            }
            case "ll" :{
                TakeGoldPro.phone = Info.ll.getPhone();
                TakeGoldPro.token = Info.ll.getToken();
                TakeGoldPro.name = "盲海.";
                break;
            }

        }
        TakeGoldPro.start(takeNum);

        StringBuilder builder = new StringBuilder();
        String s ;

        if ((s=TakeGoldPro.getMsg()) !=null){
            builder.append(TakeGoldPro.name+":【"+s+"】\n");
        }
        builder.append(TakeGoldPro.name+":捡了【"+TakeGoldPro.takeTotal.intValue()+"】次\n");
        builder.append("本次共捡金币【"+ TakeGoldPro.currentTotal.intValue() +"】\n");
        builder.append("当前总金币【"+TakeGoldPro.balance.intValue()+"】");

//        TakeGoldPro.sendWeChar(TakeGoldPro.name,builder.toString());

        TakeGoldPro.takeTotal = 0;
        TakeGoldPro.currentTotal = 0d;
        TakeGoldPro.balance = 0d;
        return builder.toString();
    }

    List<String> list = Arrays.asList("V30", "V70","V150","V500","V200","V100");

    @RequestMapping("recMsg")
    public Object recMsg(RecMsgVo recMsg) throws IOException {

        if (recMsg.getType().equals("text") ){
            String content = recMsg.getContent();
            log.info(content);
            String[] split = content.split(" ");
            if (list.contains(split[0])){
                log.info("兑换{}金币",content);
                String result = TakeGoldPro.exchange(split[0], Info.infoMap.get(split[1]));
                Log.get().info(result);

                return MsgResponse.resp(true,result);
            }

            if(content.contains("捡金币")){
                String result = takeGold(content.split("捡金币")[1]);
                return MsgResponse.resp(true,result);
            }

            if (content.equals("帮助")){
                StringBuilder sb = new StringBuilder();
                sb.append("输入【捡金币y】,y为名字拼音\n");
                sb.append("输入【Vx y】,x为兑换金币数量,y为名字拼音\n");
                String string = sb.toString();
                log.info(string);
                return MsgResponse.resp(true, string);

            }

        }


        MsgResponse msgResponse = new MsgResponse();
//        return MsgResponse.resp(true, recMsg.getContent());
        return msgResponse;
    }

    @RequestMapping("setToken")
    public String setToken(String name, String value) throws IOException {
        if (name.equals(Info.ll.name())){
            Info.ll.setToken(value);
            return Info.ll.token;
        }
        if (name.equals(Info.cxd.name())){
            Info.cxd.setToken(value);
            return Info.cxd.token;
        }
        return "设置失败";

    }

    @RequestMapping("doSelenium")
    public String doSelenium(String name) throws IOException {
        ThreadUtil.execute(() -> {
            seleniumDynamicPage.getLiulag(name);
        });
        return "ok";
    }

    @RequestMapping("changAuto")
    public String changAuto(String name, String value){
        SeleniumDynamicPage.autoSet.put(name, value);
        return "ok";
    }

    @RequestMapping("getSmsImg")
    public void getSmsImg(HttpServletResponse response) throws IOException {

        FileInputStream fis = new FileInputStream("img.png");
        byte data[] = new byte[ fis.available()];
        fis.read(data); // 读数据
        fis.close();

        response.setContentType("image/jpeg");  // 设置返回的文件类型
        OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
        toClient.write(data); // 输出数据
        toClient.close();
    }


}
