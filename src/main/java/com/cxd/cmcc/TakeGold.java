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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author caixd
 * @date 2023/9/22
 * @desc
 */
public class TakeGold {
	public static Set enable= new HashSet();
	// 本次捡金币数
	public static Double currentTotal = 0d;
	public static Integer takeTotal = 0;
	// 当前总金币数
	public static Double balance = 0d;


	public static String token = Info.ll.getToken();
	public static String phone = Info.ll.getPhone();


	public static void main(String[] args) throws IOException {
		int num = 0;

		File file = new File("D:\\File\\tt.txt");

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		ArrayList<String> itemList = new ArrayList<>();
		try {

			while ((line = reader.readLine()) != null) {
				String[] split = line.split("\"");
				for (String s : split) {
					if (s.length() == 11) {
						num++;

						itemList.add(s);
						if (itemList.size() == 6) {
							scanGold(itemList);
							itemList.clear();
						}
					}
				}
			}
			reader.close();
		}catch (Exception e){
			Log.get().error(e.getMessage());
		}
		rewrite();
		Log.get().info("本次读取了【{}】个号码",num);
		Log.get().info("有效【{}】个号码",enable.size());

		Log.get().info("捡了【{}】次",takeTotal);
		Log.get().info("本次共捡金币【{}】", currentTotal);
		Log.get().info("当前总金币【{}】",balance);

	}

	private static void rewrite() throws IOException {
		String line;
		File file2 = new File("D:\\File\\enable.txt");
		BufferedReader readerEnable = new BufferedReader(new FileReader(file2));

		HashSet<String> set = new HashSet<>();

		while((line=readerEnable.readLine())!=null){
			set.add(line);
		}
		set.addAll(enable);

		if (set.size()==0){
			return;
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file2,false));
		set.forEach(e-> {
			try {
				writer.write(e.toString());
				writer.newLine();
				writer.flush();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
		writer.close();
		readerEnable.close();
	}

	private static String takeGold(String target) {
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
		Log.get().info(body);
		JSONObject con = JSONUtil.parseObj(body).getJSONObject("content");
		if ("0".equals(con.getStr("resultCode")) ) {
			if (con.getDouble("balance") > balance) {
				takeTotal++;
				balance = con.getDouble("balance");

				Double getedFlow = con.getDouble("getedFlow");
				Log.get().info("捡到金币：{}", getedFlow);
				currentTotal += getedFlow;
			}
		}
		return con.getStr("resultCode");
	}

	private static void scanGold(List itemList) {
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
			Log.get().info("捡了【{}】次",takeTotal);
			throw new RuntimeException(rcontent.getStr("resultMsg"));
		}
		for (Object o : list) {
			JSONObject item = (JSONObject) o;
			Double availableFlowCoin = item.getDouble("availableFlowCoin");

			String cellphone = item.getStr("cellphone");

			if (availableFlowCoin>5){
				String code = takeGold(cellphone);
				if (!"0".equals(code)){
					code = takeGold(cellphone);
				}
			}
			if (item.getInt("register")==0){
				enable.add(cellphone);
			}
		}

	}
}
