package com.xiangxuepf.p2p.QRCode.test;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * @author mhw
 */
public class ToJsonStringTest {
    @Test
    public void toJsonStringTest(){
        //用fastjson生成jsonString  //@
        //{"country":"China","Province":"河北省","city":"雄安","address":{"社区":"幸福社区","街道":"解放路","门号":"888号"}}
        //创建一个json对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("country","China");
        jsonObject.put("Province","河北省");
        jsonObject.put("City","雄安");
        JSONObject addressJsonObject = new JSONObject();
        addressJsonObject.put("社区","幸福社区");
        addressJsonObject.put("街道","解放路");
        addressJsonObject.put("门号","888号");
        jsonObject.put("address",addressJsonObject);
        //将json对象转换为json格式的字符串；
        String jsonString = jsonObject.toJSONString();
    }
}
