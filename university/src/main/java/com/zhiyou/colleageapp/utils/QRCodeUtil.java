package com.zhiyou.colleageapp.utils;

/**
 * Created by wujiaolong on 16/5/27.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 */
public class QRCodeUtil {
    /**
     * 生成二维码Bitmap
     *
     * @param content   内容
     * @param widthPix  图片宽度
     * @param heightPix 图片高度
     * @param logoBm    二维码中心的Logo图标（可以为null）
     * @param filePath  用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createQRImage(String content, int widthPix, int heightPix, Bitmap logoBm, String filePath) {
        try {
            if (content == null || "".equals(content)) {
                return false;
            }

            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }
//用法说明我注释一下 用的时候拷贝到你对应的地方

    /**
     *
     * 按钮点击，调用该方法生成二维码
     *
     *     public void btnClick(View view){
     final String filePath = getFileRoot(MainActivity.this) + File.separator
     + "qr_" + System.currentTimeMillis() + ".jpg";

     //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
     new Thread(new Runnable() {
    @Override
    public void run() {
    boolean success = QRCodeUtil.createQRImage("helloworld", 800, 800,
    true ? BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher) : null,
    filePath);
    if (success) {
    runOnUiThread(new Runnable() {
    @Override
    public void run() {
    imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
    }
    });
    }
    }
    }).start();
     }
     *
     * //文件存储根目录
     private String getFileRoot(Context context) {
     if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
     File external = context.getExternalFilesDir(null);
     if (external != null) {
     return external.getAbsolutePath();
     }
     }
     return context.getFilesDir().getAbsolutePath();
     }
     * **/

}
