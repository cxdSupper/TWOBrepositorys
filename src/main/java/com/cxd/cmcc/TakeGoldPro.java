package com.cxd.cmcc;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;

import java.io.*;
import java.util.*;

/**
 * @author caixd
 * @date 2023/9/22
 * @desc
 */
public class TakeGoldPro {
	public static List<String> allPhone= new ArrayList();
	// 本次捡金币数
	public static Double currentTotal = 0d;
	public static Integer takeTotal = 0;
	// 当前总金币数
	public static Double balance = 0d;

	public static String getMsg() {
		String s =msg;
		msg = null;
		return s;
	}

	private static String msg;

	public static String token = Info.cxd.getToken();
	public static String phone = Info.cxd.getPhone();
	public static String name = "辛迪";




	static {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("enable.txt");

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";

		while(true){
			try {
				if (((line=reader.readLine())==null)) break;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (line.length()==11) {
				allPhone.add(line);
			}
		}
		try {
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void start() throws IOException {
		openBox();

		ArrayList<String> itemList = new ArrayList<>();
		try {

			for (String item : allPhone) {
				itemList.add(item);
				if (itemList.size() == 6) {
					scanGold(itemList);
					itemList.clear();
				}
			}
		}catch (Exception e){
			msg = e.getMessage();
			Log.get().error(e.getMessage());
		}
		if (balance==0){
			queryGold();
		}
		log();

	}

	private static void log() {




			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append(":\n捡了【"+takeTotal+"】次\n");
			sb.append("本次共捡金币【"+currentTotal+"】\n");
			sb.append("当前总金币【"+balance+"】\n");

			Log.get().info(sb.toString());

//			sendWeChar(sb);


	}

	public static void sendWeChar(StringBuilder sb) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("to", "捡金币");
		map.put("isRoom", true);
		map.put("type", "text");
		map.put("content", sb.toString());
		try {
			HttpUtil.post("http://localhost:3001/webhook/msg", JSONUtil.toJsonStr(map));
		}catch (Exception e){
			Log.get().error(e.getMessage());
		}
	}
	public static void sendWeChar(String to,String sb) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("to", to);
		map.put("isRoom", false);
		map.put("type", "text");
		map.put("content", sb);
		try {
			HttpUtil.post("http://localhost:3001/webhook/msg", JSONUtil.toJsonStr(map));
		}catch (Exception e){
			Log.get().error(e.getMessage());
		}
	}

	public static String takeGold(String target) {
		JSONObject json = new JSONObject();
		json.set("msgType", "2.104.1");
		json.set("version", "1|138");
		json.set("createTime", DateUtil.now());
		JSONObject content = new JSONObject();
		JSONObject content2 = new JSONObject();
		content2.set("accessToken", token);
		content2.set("cellphone", phone);
		content2.set("target", target);
		content.set("content", content2);
		json.set("content", content);
		HttpRequest post = HttpUtil.createPost("https://woxin2.jx139.com/interface/MsgPort");
		post.header("interfaceCode", "2.104.1");
		post.body(json.toJSONString(0));

		HttpResponse execute = post.execute();
		String body = execute.body();
//		Log.get().info(body);
		JSONObject con = JSONUtil.parseObj(body).getJSONObject("content");
		if(con.getStr("resultCode").equals("0") && con.getDouble("balance") > balance ) {
			takeTotal++;
			balance = con.getDouble("balance");

			Double getedFlow = con.getDouble("getedFlow");
//			Log.get().info("捡到金币：{}", getedFlow);
			currentTotal += getedFlow;
		}
		return con.getStr("resultCode");
	}

	public static String openBox() {
		JSONObject json = new JSONObject();
		json.set("msgType", "2.105.1");
		json.set("version", "1|138");
		json.set("createTime", DateUtil.now());
		JSONObject content = new JSONObject();
		JSONObject content2 = new JSONObject();
		content2.set("accessToken", token);
		content2.set("cellphone", phone);
		content.set("content", content2);
		json.set("content", content);
		HttpRequest post = HttpUtil.createPost("https://woxin2.jx139.com/interface/MsgPort");
		post.header("interfaceCode", "2.105.1");
		post.body(json.toJSONString(0));

		HttpResponse execute = post.execute();
		String body = execute.body();
//		Log.get().info(body);
		JSONObject con = JSONUtil.parseObj(body).getJSONObject("content");
		if(con.getStr("resultCode").equals("0")) {
			JSONArray itemList = con.getJSONArray("itemList");
			JSONObject item = (JSONObject) itemList.get(0);
			Double boxGold = item.getDouble("count");

			Log.get().info("开启宝箱获得【{}】金币", boxGold);
			currentTotal += boxGold;
		}
		return con.getStr("resultCode");
	}
	public static String queryGold() {
		JSONObject json = new JSONObject();
		json.set("msgType", "14.51.1");
		json.set("version", "1|138");
		json.set("createTime", DateUtil.now());
		JSONObject content = new JSONObject();
		JSONObject content2 = new JSONObject();
		content2.set("accessToken", token);
		content2.set("cellphone", phone);
		content.set("content", content2);
		json.set("content", content);
		HttpRequest post = HttpUtil.createPost("https://woxin2.jx139.com/interface/MsgPort");
		post.header("interfaceCode", "14.51.1");
		post.body(json.toJSONString(0));

		HttpResponse execute = post.execute();
		String body = execute.body();
//		Log.get().info(body);
		JSONObject con = JSONUtil.parseObj(body).getJSONObject("content");
		if(con.getStr("resultCode").equals("0")) {
			JSONObject userInfo = con.getJSONObject("userInfo");

			Double coinNum = userInfo.getDouble("coinNum");
			Log.get().info("查询总金币【{}】个", coinNum);
			balance = coinNum;
		}
		return con.getStr("resultCode");
	}

	public static void scanGold(List itemList) {
		JSONObject json = new JSONObject();
		json.set("msgType", "2.103.1");
		json.set("version", "1|138");
		json.set("createTime", DateUtil.now());
		JSONObject content = new JSONObject();
		JSONObject content2 = new JSONObject();
		content2.set("accessToken", token);
		content2.set("cellphone", phone);
		content2.set("itemList", itemList);
		content.set("content", content2);
		json.set("content", content);
		HttpRequest post = HttpUtil.createPost("https://woxin2.jx139.com/interface/MsgPort");
		post.header("interfaceCode", "2.103.1");
		post.body(json.toJSONString(0));

		HttpResponse execute = post.execute();
		String body = execute.body();

		JSONObject res = JSONUtil.parseObj(body);
		JSONObject rcontent = res.getJSONObject("content");
		JSONArray list = rcontent.getJSONArray("itemList");
		if (!"0".equals(rcontent.getStr("resultCode")) ) {
			throw new RuntimeException(rcontent.getStr("resultMsg"));
		}
		for (Object o : list) {
			JSONObject item = (JSONObject) o;
			Double availableFlowCoin = item.getDouble("availableFlowCoin");

			String cellphone = item.getStr("cellphone");

			if (availableFlowCoin>3 && !cellphone.equals(phone)){

				String code = takeGold(cellphone);
//				if (!"0".equals(code)){
//					code = takeGold(cellphone);
//				}
			}
		}

	}
}
