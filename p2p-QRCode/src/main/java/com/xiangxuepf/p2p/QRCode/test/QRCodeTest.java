package com.xiangxuepf.p2p.QRCode.test;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mhw
 */
public class QRCodeTest {
    @Test
    public void gererateQRCode() throws WriterException, IOException {
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
        //创建一个矩阵对象；
        Map<com.google.zxing.EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(com.google.zxing.EncodeHintType.CHARACTER_SET,"UTF-8");
        String contents = "weixin://wxpay/bizpayurl?pr=45nohCj";
        BitMatrix bitMatrix = new
                MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE,200,200,hintMap);
        //将矩阵对象转换为图片
        String filePath = "D://";
        String fileName = "qrCode.jpg";
        Path path = FileSystems.getDefault().getPath(filePath,fileName);
        MatrixToImageWriter.writeToPath(bitMatrix,"jpg",path);
    }

}
