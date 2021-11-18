package com.youwu.shouyinxitong.base;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.serialport.SerialPort;
import android.util.Log;

import com.youwu.lz.intelface.utils.PreferencesUtils;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.app.Constants;
import com.youwu.shouyinxitong.event.GoodUpdateEvent;
import com.youwu.shouyinxitong.event.SerialPostEvent;
import com.youwu.shouyinxitong.utils_tool.ToastUtils;
import com.youwu.shouyinxitong.utils_tool.YWStringUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author LzCode@qq.com
 * @date on 2018-8-17 09:56:58
 * @describe
 **/
public class Common {
    public static final String SERAIL_RATE = "serial_rate";//波特率
    public static final String SERAIL_DOOR = "serial_door";//串口

    public static final String BAG_SERIAL_PATH = "";
    public static final int BAG_SERIAL_WAVE = 9600;

    //SerialPort   RS485

    public static String WEIGHT_SERIAL_PATH = PreferencesUtils.getString(AppApplication.application, SERAIL_DOOR, "/dev/ttyXRUSB1");
    public static String WEIGHT_SERIAL_PATH2 = PreferencesUtils.getString(AppApplication.application, SERAIL_DOOR, "/dev/ttyACM2");


    public static int WEIGHT_SERIAL_WAVE = PreferencesUtils.getInt(AppApplication.application, SERAIL_RATE, 115200);
    protected SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;
    private Thread mReceiveThread;
    private Thread sendThread;
    int i;
    boolean isStart;

    public String WEIGHT_START = "02070203020006";//启动称重
    public String WEIGHT_CLOSE = "02070203030007";//停止称重
    public String WEIGHT_REMOVE = "02070203050001";//去皮指令
    public String WEIGHT_SEARCH_REMOVE = "02070203060002";//查询去皮指令
    public String WEIGHT_SEARCH_WEIGHT = "0207020309000D";//查询重量指令
    public String WEIGHT_ALLOW_DIF = "020702030B01050B";//允许重量偏差指令  5g
    public String WEIGHT_OPEN_ERASER = "02070203070003";//打开消磁指令
    public String WEIGHT_CLOSE_ERASER = "0207020308000C";//关闭消磁指令
    public String WEIGHT_MORE = "020702030C0008";//称重值大于商品标定重量
    public String WEIGHT_LITTLE = "020702030D0009";//称重值小于商品标定重量
    public String PAY_ERROR = "020702030E000A";//支付失败

    public String RESULT_WEIGHT_HEAD = "02070203CA02";

    public String BAG_BIG = "aabb090502010000ff";
    public String BAG_SMALL = "aabb090501010000ff";

    public String CANCEL_TRANSACTION = "02070203200024";//取消交易
    SerialReadThread mReadThread;

    //=====================视觉生鲜版版收银============================

    public static String WEIGHT_SERIAL_PATH_3 = PreferencesUtils.getString(AppApplication.application, SERAIL_DOOR, "dev/ttyUSB0");
    public static int WEIGHT_SERIAL_WAVE_2 = PreferencesUtils.getInt(AppApplication.application, SERAIL_RATE, 9600);


    public String WEIGHT_SEARCH_WEIGHT_0 = "0110001D0001020001641D";//校零点
    public String WEIGHT_SEARCH_WEIGHT_1 = "0110001E000204000007D07083";//输入砝码重量
    public String WEIGHT_SEARCH_WEIGHT_2 = "0110001D0001020002241C";//校增益点
    public String WEIGHT_SEARCH_WEIGHT_3 = "01030001000295CB";//查询重量指令
    public String RESULT_WEIGHT_HEAD_2 = "01";//


    //=======================SerialPort================================Start
    public void initSerial(String path, int rave) {
        if (mSerialPort != null) {
            closeSerialPort();
        }

        try {
            mSerialPort = new SerialPort(new File(path), rave, 0);
            mReadThread = new SerialReadThread(mSerialPort.getInputStream());
            mReadThread.start();
            mOutputStream = mSerialPort.getOutputStream();

            Log.d("serialportlog", "打开串口成功");
        } catch (Throwable tr) {
            Log.d("serialportlog", "打开串口失败", tr);
            closeSerialPort();
        }
    }

    public void initSerial(String path, int rave, String ty) {
        if (mSerialPort != null) {
            closeSerialPort();
        }

        try {
            mSerialPort = new SerialPort(new File(path), rave, 0);
            mReadThread = new SerialReadThread(mSerialPort.getInputStream());
            mReadThread.start();
            mOutputStream = mSerialPort.getOutputStream();

            Log.d("serialportlog", "打开串口成功");
            //  checkWeight("0");
        } catch (Throwable tr) {
            Log.d("serialportlog", "打开串口失败", tr);
            closeSerialPort();
        }
    }


    public void releasegood() {
        Log.d("serialportlog", "消磁");
        sendSerialPort(WEIGHT_OPEN_ERASER);
    }

    public void startWeight() {
        sendSerialPort(WEIGHT_START);
    }

    public void stopWeidht() {
        Log.d("serialportlog", "停止称重");
        sendSerialPort(WEIGHT_CLOSE);
    }

    public void xiaoci() {
        Log.d("serialportlog", "消磁");
        sendSerialPort("7F060115b20101aa");

        ToastUtils.show(AppApplication.application.getApplicationContext(), "消磁桶工作正常");
    }


    public void removeWeight() {
        Log.d("serialportlog", "去皮");
        sendSerialPort(WEIGHT_REMOVE);
    }


    public void checkWeight() {
        Log.d("serialportlog", "查询重量");
        sendSerialPort(WEIGHT_SEARCH_WEIGHT);
    }

    //2000:01 10 00 1E 00 02 04 00 00 07 D0 70 83
    //1000:01 10 00 1E 00 02 04 00 00 03 E8 73 91

    //2000:01 10 00 1D 00 01 02 00 02 24 1C
    //1000:01 10 00 1D 00 01 02 00 02 24 1C

    public void checkWeight(String type) {
        Log.d("serialportlog", "查询重量222" + type);
        if (type.equals("0")) {
            sendSerialPort(WEIGHT_SEARCH_WEIGHT_0);
        } else if (type.equals("1")) {
//            sendSerialPort(WEIGHT_SEARCH_WEIGHT_3);
            sendSerialPort("3162303130");
        } else if (type.equals("2")) {
            sendSerialPort(WEIGHT_SEARCH_WEIGHT_1);
        } else if (type.equals("3")) {
            sendSerialPort(WEIGHT_SEARCH_WEIGHT_2);
        }

    }

    public void weightMore() {
        Log.d("serialportlog", "称重值大于商品标定值");
        sendSerialPort(WEIGHT_MORE);
    }

    public void weightLittle() {
        Log.i("serialportlog", "称重值小于商品标定值");
        sendSerialPort(WEIGHT_LITTLE);
    }

    public void payFailure() {
        Log.d("serialportlog", "支付失败");
        sendSerialPort(PAY_ERROR);
    }

    //取消交易指令
    public void cancelTransaction() {
        Log.d("serialportlog", "取消交易");
        sendSerialPort(CANCEL_TRANSACTION);
    }

    /**
     * 关闭串口
     * 关闭串口中的输入输出流
     */
    public void closeSerialPort() {
        Log.d("serialportlog", "close  serialport  ");
        if (mReadThread != null) {
            mReadThread.close();
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    /**
     * 发送数据
     * 通过串口，发送数据到单片机
     *
     * @param data 要发送的数据
     */
    public void sendSerialPort(String data) {
//        try {
//            Log.d("serialportlog", "sendserialport    " + data);
//            byte[] bytes = YWStringUtils.hexStr2bytes(data);
//            mOutputStream.write(bytes);
//            mOutputStream.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 读串口线程
     */
    public class SerialReadThread extends Thread {

        private static final String TAG = "SerialReadThread";

        private BufferedInputStream mInputStream;

        public SerialReadThread(InputStream is) {
            mInputStream = new BufferedInputStream(is);
        }

        @Override
        public void run() {
            byte[] received = new byte[1024];
            int size;

            Log.d("serialportlog", "开始读线程");

            while (true) {

                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                try {

                    int available = mInputStream.available();

                    if (available > 0) {
                        size = mInputStream.read(received);
                        if (size > 0) {
                            onDataReceive(received, size);
                        }
                    } else {
                        // 暂停一点时间，免得一直循环造成CPU占用率过高
                        SystemClock.sleep(50);
                    }
                } catch (Exception e) {
                    Log.d("serialportlog", "读取数据失败", e);
                }
                //Thread.yield();
            }

            Log.d("serialportlog", "结束读进程");
        }

        /**
         * 处理获取到的数据
         *
         * @param received
         * @param size
         */

        String sub = "";

        private void onDataReceive(byte[] received, int size) {
            String hexStr = YWStringUtils.bytes2HexStr(received, 0, size);

            Log.d("serialportlog", "onDataReceive:" + hexStr);
            if (hexStr.startsWith("0207020309000D")) {
                hexStr = hexStr.replace("0207020309000D", "");
            }
            if (hexStr.startsWith(RESULT_WEIGHT_HEAD)) {
                String weightStr = hexStr.substring(12, 16);
//                if (isToShowWeight){
                EventBus.getDefault().post(
                        new SerialPostEvent(YWStringUtils.SixteenToTeen(weightStr)));
//                    isToShowWeight = false;
//                }
            } else if (hexStr.startsWith(RESULT_WEIGHT_HEAD_2)) {


                String weightStr = "";
                int length = hexStr.length();
                if (length > 14) {
                    weightStr = hexStr.substring(6, 14);
                } else {
                    if (hexStr.length() > 6) {
                        weightStr = hexStr.substring(6, length);
                    } else {
                        weightStr = hexStr.substring(0, length);
                    }
                }


                if (weightStr.length() == 8) {
                    int weight = Integer.parseInt(weightStr, 16);
                    Log.d("serialportlog", "substring:" + weight);
                    if (weight>0){
                        EventBus.getDefault().post(new SerialPostEvent(YWStringUtils.SixteenToTeen(weightStr)));
                    }
                } else {
                    checkWeight("1");
                }
            } else {

            }
        }


        /**
         * 停止读线程
         */
        public void close() {

            try {
                mInputStream.close();
            } catch (Exception e) {
                Log.d("serialportlog", "" + e);
            } finally {
                super.interrupt();
            }
        }
    }


    //========================SerialPort================================End


    //===========================MQTT===================================Start

    Handler mHandler = new Handler();
    MqttConnectOptions mqttConnectOptions;
    IMqttActionListener mListener;
    String subscriptionTopic = Constants.MQTT_SUBSCRIPTIONTOPIC;
    //MQTT
    MqttAndroidClient mqttAndroidClient;
    String serverUri = Constants.MQTT_SERVER_URI;
    String publishTopic = Constants.MQTT_PUBLISHTOPIC;
    //    String publishMessage = "Hello World!";
    String userName = Constants.MQTT_USERNAME;
    String password = Constants.MQTT_PASSWORD;
    String MQTT_TAG = "MQTT===";

    public void initMqttClient(final Context context) {

        setClientId(context);
        mListener = new IMqttActionListener() {
            //发布消息成功
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                disconnectedBufferOptions.setBufferEnabled(true);
                disconnectedBufferOptions.setBufferSize(100);
                disconnectedBufferOptions.setPersistBuffer(false);
                disconnectedBufferOptions.setDeleteOldestMessages(false);
                mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
            }

            //发布消息失败
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(MQTT_TAG, "Failed to connect to: " + serverUri);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setClientId(context);
                        try {
                            mqttAndroidClient.connect(mqttConnectOptions, null, mListener);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        };
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.d(MQTT_TAG, "Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                } else {
                    Log.d(MQTT_TAG, "Connected to : " + serverURI);
                }
                subscribeToTopic();
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.e(MQTT_TAG, "连接丢失异常！");
                //连接丢失异常
                initMqttClient(AppApplication.application);

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //收到服务器的信息
                Log.d(MQTT_TAG, "接到消息成功 topic：" + topic + "\n接到消息成功信息：" + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(MQTT_TAG, "deliveryComplete");
            }
        });
        mqttConnectOptions = new MqttConnectOptions();
        //自动重连  调试建议  关闭自动重连
        // 在mqttCallbackExtened和MqttActionListener里做重连处理
        mqttConnectOptions.setAutomaticReconnect(false);
        // 清除缓存
        mqttConnectOptions.setCleanSession(true);
        // 设置超时时间，单位：秒
        mqttConnectOptions.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        mqttConnectOptions.setKeepAliveInterval(60);
        // 用户名
        mqttConnectOptions.setUserName(userName);
        // 密码
        mqttConnectOptions.setPassword(password.toCharArray());

        try {
            //开始连接
            mqttAndroidClient.connect(mqttConnectOptions, null, mListener);


        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    //订阅主题
    private void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(MQTT_TAG, "subscribeToTopic=====Subscribed!  " + subscriptionTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(MQTT_TAG, "subscribeToTopic=====Failed to subscribe!");
                }
            });

            mqttAndroidClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //收到服务器的信息
                    Log.d(MQTT_TAG, "订阅主题：" + topic + "    MQTT_Message：" + new String(message.getPayload()));
                    if (null == message) {
                        return;
                    }
                    String resString = message.toString();
                    if (null == resString || "".equals(resString)) {
                        return;
                    }

                    EventBus.getDefault().post(new GoodUpdateEvent(resString));

                }
            });

        } catch (MqttException ex) {
            Log.e(MQTT_TAG, "ERROR:" + "Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    //取消订阅
    public void unSubscribeToTopic() {
        try {
            mqttAndroidClient.unsubscribe(subscriptionTopic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(MQTT_TAG, "unsubscribe：" + subscriptionTopic + "  success");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(MQTT_TAG, "unsubscribe：" + subscriptionTopic + "   failure");

                }
            });

        } catch (MqttException ex) {
            Log.d(MQTT_TAG, "unsubscribe：" + subscriptionTopic + "   exception  " + ex);
        }
    }

    //释放资源
    public void closeClient() {
        Log.e(MQTT_TAG, "close  client");
        mqttAndroidClient.close();
    }

    public void unregisterResources() {
        Log.e(MQTT_TAG, "close  unregistresourse");
        mqttAndroidClient.unregisterResources();
    }

    public void closeMQtt() {
        try {
            Log.e(MQTT_TAG, "close  mqtt");
            if (null != mqttAndroidClient) {
                unSubscribeToTopic();
                unregisterResources();
                closeClient();
            }
        } catch (Exception e) {

        }
    }

    public void setClientId(Context context) {
        String clientId = Constants.MQTT_CLIENTID + System.currentTimeMillis();
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
    }
    //===========================MQTT===================================End


}
