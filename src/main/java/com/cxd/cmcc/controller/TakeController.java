//package com.cxd.cmcc.controller;
//
//import cn.hutool.log.Log;
//import com.cxd.cmcc.Info;
//import com.cxd.cmcc.TakeGoldPro;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
///**
// * @author caixd
// * @date 2023/9/23
// * @desc
// */
//@RestController
//public class TakeController {
//
//    @RequestMapping("/take/{user}")
//    public String takeGold(@PathVariable String user) throws IOException {
//        switch (user){
//            case "cxd": {
//                TakeGoldPro.phone = Info.cxd.getPhone();
//                TakeGoldPro.token = Info.cxd.getToken();
//                TakeGoldPro.name = "辛迪";
//                break;
//            }
//            case "ll" :{
//                TakeGoldPro.phone = Info.ll.getPhone();
//                TakeGoldPro.token = Info.ll.getToken();
//                TakeGoldPro.name = "盲海.";
//                break;
//            }
//            case "yjw" :{
//                TakeGoldPro.phone = Info.yjw.getPhone();
//                TakeGoldPro.token = Info.yjw.getToken();
//                TakeGoldPro.name = "～～～";
//                break;
//            }
//        }
//        TakeGoldPro.start();
//
//        StringBuilder builder = new StringBuilder();
//        String s ;
//
//        if ((s=TakeGoldPro.getMsg()) !=null){
//            builder.append(TakeGoldPro.name+":【"+s+"】\n");
//        }
//        builder.append(TakeGoldPro.name+":捡了【"+TakeGoldPro.takeTotal+"】次\n");
//        builder.append("本次共捡金币【"+TakeGoldPro.currentTotal+"】\n");
//        builder.append("当前总金币【"+TakeGoldPro.balance+"】");
//
//        TakeGoldPro.sendWeChar(TakeGoldPro.name,builder.toString());
//
//        TakeGoldPro.takeTotal = 0;
//        TakeGoldPro.currentTotal = 0d;
//        TakeGoldPro.balance = 0d;
//        return builder.toString();
//    }
//
//
//}
