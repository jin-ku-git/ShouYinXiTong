package com.youwu.shouyinxitong.presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.RemoteException;

import com.bumptech.glide.Glide;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.youwu.shouyinxitong.app.AppApplication;
import com.youwu.shouyinxitong.app.StoreInfo;
import com.youwu.shouyinxitong.bean_used.MealGoodsBean;
import com.youwu.shouyinxitong.bean_used.MealsItemBean;
import com.youwu.shouyinxitong.bean.YWGoodBean;
import com.youwu.shouyinxitong.presenter.bean.ApplyOrderPrintBean;
import com.youwu.shouyinxitong.presenter.bean.CheckNumPrintBean;
import com.youwu.shouyinxitong.presenter.bean.HandoverDayEndPrintBean;
import com.youwu.shouyinxitong.presenter.bean.HandoverPrintBean;
import com.youwu.shouyinxitong.presenter.bean.InventoryPrintBean;
import com.youwu.shouyinxitong.presenter.bean.PrintBean;
import com.youwu.shouyinxitong.presenter.bean.SaleCountBean;
import com.youwu.shouyinxitong.ui.main.MainActivity;
import com.youwu.shouyinxitong.utils_tool.BeanCloneUtil;
import com.youwu.shouyinxitong.utils_tool.BigDecimalUtils;
import com.youwu.shouyinxitong.utils_tool.ImageUtils;
import com.youwu.shouyinxitong.utils_tool.YWStringUtils;


import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.utils.BitmapProcess;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by zhicheng.liu on 2018/4/4
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class PrinterPresenter {
    private Context context;
    public PrintBean printBean;
    public ApplyOrderPrintBean applyOrderPrintBean;
    public HandoverPrintBean handoverPrintBean;
    public HandoverDayEndPrintBean handoverDayEndPrintBean;
    public InventoryPrintBean inventoryPrintBean;
    public CheckNumPrintBean checkNumPrintBean;
    public SaleCountBean saleCountPrintBean;

    private static final String TAG = "PrinterPresenter";
    public SunmiPrinterService printerService;
    private List<byte[]> list;

    public PrinterPresenter(Context context, SunmiPrinterService printerService) {
        this.context = context;
        this.printerService = printerService;
    }

    //???????????? ????????????
    public void saleCountPrint(SaleCountBean print) {
        this.saleCountPrintBean = BeanCloneUtil.cloneTo(print);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.myBinder == null) {
                    return;
                }
                MainActivity.myBinder.WriteSendData(new TaskCallback() {
                    @Override
                    public void OnSucceed() {

                    }

                    @Override
                    public void OnFailed() {

                    }
                }, new ProcessData() {
                    @Override
                    public List<byte[]> processDataBeforeSend() {
                        String divide = "--------------------------------------------\n";
                        int width = divide.length();
                        list = new ArrayList<>();
                        list.add(DataForSendToPrinterPos80.initializePrinter());
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));//??????????????????

                        try {
                            Bitmap bitmap = Bitmap.createBitmap(800, 100, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            Paint paint = new Paint();
                            paint.setTextSize(40);//??????????????????
                            paint.setColor(Color.parseColor("#000000"));
                            canvas.drawColor(Color.parseColor("#ffffff"));
                            canvas.drawText("??????????????????", 150, 60, paint);//????????????paint
                            List<Bitmap> blist = new ArrayList<>();
                            blist = BitmapProcess.cutBitmap(100, bitmap);
                            for (int i = 0; i < blist.size(); i++) {
                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 576));
                            }
                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 00));
                            list.add(StringUtils.strTobytes("" + "\n"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        list.add(StringUtils.strTobytes("        " + saleCountPrintBean.getSale_time() + "\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(StringUtils.strTobytes("???????????????" + saleCountPrintBean.getPrint_time() + "\n"));
                        list.add(StringUtils.strTobytes("????????????" + saleCountPrintBean.getAdmin_name() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + saleCountPrintBean.getStore_name() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + saleCountPrintBean.getStore_address() + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
                        list.add(StringUtils.strTobytes("??????????????????        ?????????         ?????????\n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getAll_count_total_price() + "              " + saleCountPrintBean.getAll_count_total_num() + "???              " + saleCountPrintBean.getAll_count_avg_price() + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("????????????            ?????????         ?????????\n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getTake_count_total_price() + "              " + saleCountPrintBean.getTake_count_total_num() + "???              " + saleCountPrintBean.getTake_count_avg_price() + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("????????????            ?????????         ?????????\n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getStore_count_total_price() + "              " + saleCountPrintBean.getStore_count_total_num() + "???              " + saleCountPrintBean.getStore_count_avg_price() + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("???????????????                       ????????????      \n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getAll_refund_total_price() + "                              " + saleCountPrintBean.getAll_refund_total_num() + "???" + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("?????????????????????                   ????????????      \n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getTake_refund_total_price() + "                                 " + saleCountPrintBean.getTake_refund_total_num() + "???" + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("?????????????????????                   ????????????      \n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getStore_refund_total_price() + "                                 " + saleCountPrintBean.getStore_refund_total_num() + "???" + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("????????????                         ????????????      \n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getRecharge_total_price() + "                             " + saleCountPrintBean.getRecharge_total_num() + "???" + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("???????????????                       ????????????      \n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getCoupon_total_price() + "                             " + saleCountPrintBean.getCoupon_total_num() + "???" + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("????????????                         ??????      \n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getAll_discount_total_price() + "                             " + saleCountPrintBean.getAll_discount_total_num() + "???" + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("??????????????????                     ??????      \n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getTake_discount_total_price() + "                             " + saleCountPrintBean.getTake_discount_total_num() + "???" + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("??????????????????                     ??????      \n"));
                        list.add(StringUtils.strTobytes(saleCountPrintBean.getStore_discount_total_price() + "                             " + saleCountPrintBean.getStore_discount_total_num() + "???" + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));

                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());

                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                        return list;
                    }
                });
            }
        }).start();
    }

    //?????????????????????
    public void handOverDayEndPrint(HandoverDayEndPrintBean print) {
        this.handoverDayEndPrintBean = BeanCloneUtil.cloneTo(print);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.myBinder == null) {
                    return;
                }
                MainActivity.myBinder.WriteSendData(new TaskCallback() {
                    @Override
                    public void OnSucceed() {

                    }

                    @Override
                    public void OnFailed() {

                    }
                }, new ProcessData() {
                    @Override
                    public List<byte[]> processDataBeforeSend() {
                        String divide = "--------------------------------------------\n";
                        int width = divide.length();
                        list = new ArrayList<>();
                        list.add(DataForSendToPrinterPos80.initializePrinter());
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));//??????????????????

                        try {
                            Bitmap bitmap = Bitmap.createBitmap(800, 100, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            Paint paint = new Paint();
                            paint.setTextSize(40);//??????????????????
                            paint.setColor(Color.parseColor("#000000"));
                            canvas.drawColor(Color.parseColor("#ffffff"));
                            canvas.drawText("?????????????????????", 150, 60, paint);//????????????paint
                            List<Bitmap> blist = new ArrayList<>();
                            blist = BitmapProcess.cutBitmap(100, bitmap);
                            for (int i = 0; i < blist.size(); i++) {
                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 576));
                            }
                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 00));
                            list.add(StringUtils.strTobytes("" + "\n"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        list.add(StringUtils.strTobytes("?????????" + handoverDayEndPrintBean.getStore_name() + "\n"));
                        list.add(StringUtils.strTobytes("????????????" + handoverDayEndPrintBean.getAdmin_name() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + handoverDayEndPrintBean.getHandoverStartTime() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + handoverDayEndPrintBean.getHandoverEndTime() + "\n"));

                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));

                        list.add(StringUtils.strTobytes("????????????               " + handoverDayEndPrintBean.getHandoverDayendSum() + "\n"));
                        list.add(StringUtils.strTobytes("????????????               " + handoverDayEndPrintBean.getHandoverDayendReal() + "\n"));
                        list.add(StringUtils.strTobytes("????????????               " + "\n"));
                        list.add(StringUtils.strTobytes("?????????" + handoverDayEndPrintBean.getWx() + "  ????????????" + handoverDayEndPrintBean.getZfb() +
                                "  ?????????" + handoverDayEndPrintBean.getCashi() + "  ?????????" + handoverDayEndPrintBean.getYue() + "  ???????????????" + handoverDayEndPrintBean.getMt() + "\n"));

                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));

                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                        return list;
                    }
                });
            }
        }).start();
    }

    //???????????????????????????
    public void checkNumPrint(CheckNumPrintBean print) {
        this.checkNumPrintBean = BeanCloneUtil.cloneTo(print);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.myBinder == null) {
                    return;
                }
                MainActivity.myBinder.WriteSendData(new TaskCallback() {
                    @Override
                    public void OnSucceed() {

                    }

                    @Override
                    public void OnFailed() {

                    }
                }, new ProcessData() {
                    @Override
                    public List<byte[]> processDataBeforeSend() {
                        String divide = "--------------------------------------------\n";
                        int width = divide.length();
                        list = new ArrayList<>();
                        list.add(DataForSendToPrinterPos80.initializePrinter());
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));//??????????????????

                        try {
                            Bitmap bitmap = Bitmap.createBitmap(800, 100, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            Paint paint = new Paint();
                            paint.setTextSize(40);//??????????????????
                            paint.setColor(Color.parseColor("#000000"));
                            canvas.drawColor(Color.parseColor("#ffffff"));
                            canvas.drawText("????????????", 150, 60, paint);//????????????paint
                            List<Bitmap> blist = new ArrayList<>();
                            blist = BitmapProcess.cutBitmap(100, bitmap);
                            for (int i = 0; i < blist.size(); i++) {
                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 576));
                            }
                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 00));
                            list.add(StringUtils.strTobytes("" + "\n"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        list.add(StringUtils.strTobytes("???????????????" + checkNumPrintBean.getCheckNumTime() + "\n"));
                        list.add(StringUtils.strTobytes("????????????" + checkNumPrintBean.getAdmin_name() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + checkNumPrintBean.getStore_name() + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
                        list.add(StringUtils.strTobytes("?????????                   ????????????      \n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));

                        width = divide.length();
                        if (checkNumPrintBean.getCheckNumData().size() == 0) {
                            list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                            return list;
                        }
                        for (int i = 0; i < checkNumPrintBean.getCheckNumData().size(); i++) {
                            if (checkNumPrintBean.getCheckNumData().get(i) instanceof YWGoodBean) {
                                printCheckNumGoods((YWGoodBean) checkNumPrintBean.getCheckNumData().get(i), width);//?????????????????????????????????
                            } else {
                                printCheckNumGoods((YWGoodBean) checkNumPrintBean.getCheckNumData().get(i), width);//?????????????????????????????????
                            }

                        }
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());

                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                        return list;
                    }
                });
            }
        }).start();
    }

    //???????????????????????????
    public void inventoryPrint(InventoryPrintBean print) {
        this.inventoryPrintBean = BeanCloneUtil.cloneTo(print);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.myBinder == null) {
                    return;
                }
                MainActivity.myBinder.WriteSendData(new TaskCallback() {
                    @Override
                    public void OnSucceed() {

                    }

                    @Override
                    public void OnFailed() {

                    }
                }, new ProcessData() {
                    @Override
                    public List<byte[]> processDataBeforeSend() {
                        String divide = "--------------------------------------------\n";
                        int width = divide.length();
                        list = new ArrayList<>();
                        list.add(DataForSendToPrinterPos80.initializePrinter());
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));//??????????????????

                        try {
                            Bitmap bitmap = Bitmap.createBitmap(800, 100, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            Paint paint = new Paint();
                            paint.setTextSize(40);//??????????????????
                            paint.setColor(Color.parseColor("#000000"));
                            canvas.drawColor(Color.parseColor("#ffffff"));
                            canvas.drawText("????????????", 150, 60, paint);//????????????paint
                            List<Bitmap> blist = new ArrayList<>();
                            blist = BitmapProcess.cutBitmap(100, bitmap);
                            for (int i = 0; i < blist.size(); i++) {
                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 576));
                            }
                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 00));
                            list.add(StringUtils.strTobytes("" + "\n"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        list.add(StringUtils.strTobytes("???????????????" + inventoryPrintBean.getInventoryTime() + "\n"));
                        list.add(StringUtils.strTobytes("????????????" + inventoryPrintBean.getAdmin_name() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + inventoryPrintBean.getStore_name() + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
                        list.add(StringUtils.strTobytes("?????????               ?????????          ??????\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));

                        width = divide.length();
                        if (inventoryPrintBean.getInventoryData().size() == 0) {
                            list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                            return list;
                        }
                        for (int i = 0; i < inventoryPrintBean.getInventoryData().size(); i++) {
                            if (inventoryPrintBean.getInventoryData().get(i) instanceof YWGoodBean) {
                                printInventoryGoods((YWGoodBean) inventoryPrintBean.getInventoryData().get(i), width);//?????????????????????????????????????????????
                            } else {
                                printInventoryGoods((YWGoodBean) inventoryPrintBean.getInventoryData().get(i), width);//?????????????????????????????????????????????
                            }

                        }
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());

                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                        return list;
                    }
                });
            }
        }).start();
    }

    public void handOverPrint(HandoverPrintBean print) {
        this.handoverPrintBean = BeanCloneUtil.cloneTo(print);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.myBinder == null) {
                    return;
                }
                MainActivity.myBinder.WriteSendData(new TaskCallback() {
                    @Override
                    public void OnSucceed() {

                    }

                    @Override
                    public void OnFailed() {

                    }
                }, new ProcessData() {
                    @Override
                    public List<byte[]> processDataBeforeSend() {
                        String divide = "--------------------------------------------\n";
                        int width = divide.length();
                        list = new ArrayList<>();
                        list.add(DataForSendToPrinterPos80.initializePrinter());
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));//??????????????????

                        try {
                            Bitmap bitmap = Bitmap.createBitmap(800, 100, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            Paint paint = new Paint();
                            paint.setTextSize(40);//??????????????????
                            paint.setColor(Color.parseColor("#000000"));
                            canvas.drawColor(Color.parseColor("#ffffff"));
                            canvas.drawText("???????????????", 150, 60, paint);//????????????paint
                            List<Bitmap> blist = new ArrayList<>();
                            blist = BitmapProcess.cutBitmap(100, bitmap);
                            for (int i = 0; i < blist.size(); i++) {
                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 576));
                            }
                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 00));
                            list.add(StringUtils.strTobytes("" + "\n"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        list.add(StringUtils.strTobytes("?????????" + handoverPrintBean.getStoreName() + "\n"));
                        list.add(StringUtils.strTobytes("?????????" + handoverPrintBean.getStoreAddress() + "\n"));
                        list.add(StringUtils.strTobytes("????????????" + handoverPrintBean.getCashierName() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + handoverPrintBean.getLogintime() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + handoverPrintBean.getPrintTime() + "\n"));

                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
                        list.add(StringUtils.strTobytes("????????????        ????????????         ????????????\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));

                        list.add(StringUtils.strTobytes("??????               " + handoverPrintBean.getWxNumberSales() + "                " + handoverPrintBean.getWxPayPrice() + "\n"));
                        list.add(StringUtils.strTobytes("?????????             " + handoverPrintBean.getZfbNumberSales() + "                " + handoverPrintBean.getZfbPayPrice() + "\n"));
                        list.add(StringUtils.strTobytes("??????               " + handoverPrintBean.getYueNumberSales() + "                " + handoverPrintBean.getYuePayPrice() + "\n"));
                        list.add(StringUtils.strTobytes("??????               " + handoverPrintBean.getCashNumberSales() + "                " + handoverPrintBean.getCashPayPrice() + "\n"));
                        list.add(StringUtils.strTobytes("??????               " + handoverPrintBean.getMeituanNumberSales() + "                " + handoverPrintBean.getMeituanPayPrice() + "\n"));

                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("\n"));
                        Double allNumber = Double.parseDouble(handoverPrintBean.getWxNumberSales()) + Double.parseDouble(handoverPrintBean.getZfbNumberSales()) + Double.parseDouble(handoverPrintBean.getYueNumberSales())
                                + Double.parseDouble(handoverPrintBean.getCashNumberSales()) + Double.parseDouble(handoverPrintBean.getMeituanNumberSales());
                        Double allPrice = Double.parseDouble(handoverPrintBean.getWxPayPrice()) + Double.parseDouble(handoverPrintBean.getZfbPayPrice()) + Double.parseDouble(handoverPrintBean.getYuePayPrice())
                                + Double.parseDouble(handoverPrintBean.getCashPayPrice()) + Double.parseDouble(handoverPrintBean.getMeituanPayPrice());
                        list.add(StringUtils.strTobytes("??????               " + new Double(allNumber).intValue() + "                " + allPrice + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + handoverPrintBean.getTotalNumberSales() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + handoverPrintBean.getTotalSales() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + handoverPrintBean.getTotalPayPrice() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + handoverPrintBean.getTotalRecharge() + "\n"));
                        list.add(StringUtils.strTobytes("" + "\n"));
                        list.add(StringUtils.strTobytes("" + "\n"));

                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                        return list;
                    }
                });
            }
        }).start();
    }

    public void applyPrint(ApplyOrderPrintBean print) {
        this.applyOrderPrintBean = BeanCloneUtil.cloneTo(print);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.myBinder == null) {
                    return;
                }
                MainActivity.myBinder.WriteSendData(new TaskCallback() {
                    @Override
                    public void OnSucceed() {

                    }

                    @Override
                    public void OnFailed() {

                    }
                }, new ProcessData() {
                    @Override
                    public List<byte[]> processDataBeforeSend() {
                        String divide = "--------------------------------------------\n";
                        int width = divide.length();
                        list = new ArrayList<>();
                        list.add(DataForSendToPrinterPos80.initializePrinter());
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));//??????????????????

                        try {
                            Bitmap bitmap = Bitmap.createBitmap(800, 100, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            Paint paint = new Paint();
                            paint.setTextSize(40);//??????????????????
                            paint.setColor(Color.parseColor("#000000"));
                            canvas.drawColor(Color.parseColor("#ffffff"));
                            canvas.drawText("???????????????", 150, 60, paint);//????????????paint
                            List<Bitmap> blist = new ArrayList<>();
                            blist = BitmapProcess.cutBitmap(100, bitmap);
                            for (int i = 0; i < blist.size(); i++) {
                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 576));
                            }
                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 00));
                            list.add(StringUtils.strTobytes("" + "\n"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        list.add(StringUtils.strTobytes("?????????" + applyOrderPrintBean.getOrder_sn() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + applyOrderPrintBean.getAdd_time() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + applyOrderPrintBean.getUpdate_time() + "\n"));
                        list.add(StringUtils.strTobytes("????????????" + applyOrderPrintBean.getAdmin_name() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + applyOrderPrintBean.getStore_name() + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("????????????" + applyOrderPrintBean.getTotal_money() + "\n"));
                        list.add(StringUtils.strTobytes("????????????" + applyOrderPrintBean.getTotal_cost_money() + "\n"));
                        list.add(StringUtils.strTobytes("?????????" + applyOrderPrintBean.getStatus_name() + "\n"));
                        list.add(StringUtils.strTobytes("?????????" + applyOrderPrintBean.getMark() + "\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
                        list.add(StringUtils.strTobytes("?????????               ??????          ??????\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        int count = 0;
                        float total = 0f;

                        width = divide.length();
                        if (applyOrderPrintBean.getCarYWGoodBeans().size() == 0) {
                            list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                            return list;
                        }
                        for (int i = 0; i < applyOrderPrintBean.getCarYWGoodBeans().size(); i++) {
                            if (applyOrderPrintBean.getCarYWGoodBeans().get(i) instanceof YWGoodBean) {
                                count += ((YWGoodBean) applyOrderPrintBean.getCarYWGoodBeans().get(i)).getBuynum();
//                                total += ((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getBuynum() * Double.parseDouble(((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getPrice());
                                printApplyGoods((YWGoodBean) applyOrderPrintBean.getCarYWGoodBeans().get(i), width);//????????????????????????????????????
                            } else {
                                count += ((YWGoodBean) applyOrderPrintBean.getCarYWGoodBeans().get(i)).getBuynum();
                                printApplyGoods((YWGoodBean) applyOrderPrintBean.getCarYWGoodBeans().get(i), width);//????????????????????????????????????
                            }

                        }
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(StringUtils.strTobytes("???" + applyOrderPrintBean.getAllSum() + "??? \n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
//                        Bitmap bitmap = null;
//                        try {
//                            bitmap = Glide.with(context)
//                                    .asBitmap()
//                                    .load(StoreInfo.getStore().getCode_url())
//                                    .submit(300, 300).get();
//                            if (null != bitmap && bitmap.getWidth() > 310) {
//                                int newHeight = (int) (1.0 * bitmap.getHeight() * 310 / bitmap.getWidth());
//                                bitmap = ImageUtils.scale(bitmap, 300, 300);
//                            }
//                            list.add(DataForSendToPrinterPos80.initializePrinter());
//                            List<Bitmap> blist = BitmapProcess.cutBitmap(150, bitmap);
//                            for (int i = 0; i < blist.size(); i++) {
//                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 576));
//                            }
//                            list.add(DataForSendToPrinterPos80.printAndFeedLine());
//                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 00));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
//                        list.add(StringUtils.strTobytes("\n"));
//                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
//                        list.add(StringUtils.strTobytes("???????????????????????????????????????????????????????????????????????????????????????48???????????????????????????????????????????????????????????????\n"));
//
//                        Bitmap fpbitmap = null;
//                        try {
//                            fpbitmap = BitmapUtil.createQRImage(StoreInfo.getStore().getElectronicInvoices_url()+printBean.getOrder_sn()+"",300,300);
//
//                            list.add(DataForSendToPrinterPos80.initializePrinter());
//                            List<Bitmap> fplist = BitmapProcess.cutBitmap(150, fpbitmap);
//                            for (int i = 0; i < fplist.size(); i++) {
//                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, fplist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 576));
//                            }
//                            list.add(DataForSendToPrinterPos80.printAndFeedLine());
//
//                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 00));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                        return list;
                    }
                });
            }
        }).start();
    }


    public void print(PrintBean print) {
        this.printBean = BeanCloneUtil.cloneTo(print);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int fontsizeContent = 22;

                String divide = "---------------------------------\n";
                int width = divide.length();
                try {
                    if (printerService.updatePrinterState() != 1) {
                        return;
                    }
                    printerService.setAlignment(1, null);//0?????????   1???  2???
                    if (printBean.getOnlion()) {
                        printerService.printTextWithFont("?????????:" + printBean.getFoodCode() + "??????????????????", "", 30, null);
                        printerService.lineWrap(1, null);
                        printerService.setAlignment(0, null);//0?????????   1???  2???
                        if (printBean.getDeliveryType().equals("1")) {
                            printerService.printTextWithFont("???????????????????????????" + "\n", "", fontsizeContent, null);
                        } else if (printBean.getDeliveryType().equals("2")) {
                            printerService.printTextWithFont("???????????????????????????" + "\n", "", fontsizeContent, null);
                        } else if (printBean.getDeliveryType().equals("3")) {
                            printerService.printTextWithFont("?????????????????????" + "\n", "", fontsizeContent, null);
                            printerService.printTextWithFont(divide, "", fontsizeContent, null);
                            printerService.printTextWithFont("???????????????" + printBean.getOrderAddress() + "\n", "", fontsizeContent,
                                    null);
                        }
                    } else {
                        printerService.printTextWithFont("?????????" + AppApplication.orderType + printBean.getOrderNumberBean().getOrderNumberStr() + "\n", "", 33, null);
                    }

                    printerService.printTextWithFont(divide, "", fontsizeContent, null);

                    printerService.printTextWithFont("?????????" + StoreInfo.getStore().getStore_name() + "\n", "", fontsizeContent, null);
                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    printerService.printTextWithFont("????????????" + printBean.getCashier() + "\n", "", fontsizeContent,
                            null);
                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    printerService.printTextWithFont("???????????????" + printBean.getOrder_sn() + "\n", "", fontsizeContent, null);
                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    printerService.printTextWithFont("???????????????" + printBean.getCreatTime() + "\n", "", fontsizeContent, null);
                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    printerService.printTextWithFont("?????????      ??????    ??????     ??????\n", "", fontsizeContent, null);
                    printerService.printTextWithFont(divide, "", fontsizeContent, null);

                    int count = 0;
                    float total = 0f;
                    for (int i = 0; i < printBean.getShopCarYWGoodBeans().size(); i++) {
                        if (printBean.getShopCarYWGoodBeans().get(i) instanceof YWGoodBean) {
                            count += ((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getBuynum();
                            total += ((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getBuynum() * Double.parseDouble(((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getPrice());
                            printGoods((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i), width);//????????????????????????????????????????????????????????????
                        } else {
                            MealsItemBean itemsBean = (MealsItemBean) printBean.getShopCarYWGoodBeans().get(i);
                            count += itemsBean.getBuynum();
                            total += itemsBean.getBuynum() * Double.parseDouble(itemsBean.getMeal_goods_price());
                            printGoods(itemsBean, width);//????????????????????????????????????????????????????????????

                        }


                    }

                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    printerService.printTextWithFont("???" + count + "??????????????????" + YWStringUtils.getStanMoney((float) total) + "???" + "\n", "", fontsizeContent, null);
                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    if (printBean.getBean() != null) {
                        printerService.printTextWithFont("??????" + printBean.getBean().getName() + "  ?????? " + YWStringUtils.getStanMoney(((total + Float.parseFloat(printBean.getFreight())) - Float.parseFloat(printBean.getPaymoney()))) + "??? \n", "", fontsizeContent, null);
                        printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    }
//
                    if (printBean.getCouponBean() != null) {
                        printerService.printTextWithFont(printBean.getCouponBean().getName() + "\n", "", fontsizeContent, null);
                        printerService.printTextWithFont(divide, "", fontsizeContent, null);
                        if (printBean.getCouponBean().getType().equals("5")) {//?????????
                            printerService.printTextWithFont("??????" + printBean.getCouponPrice() + "???" + "\n", "", fontsizeContent, null);
                        } else {
//                            printerService.printTextWithFont("??????" + YWStringUtils.getStanMoney((total - Float.parseFloat(printBean.getPaymoney()))) + "???" + "\n", "", fontsizeContent, null);
                        }

                    } else {
//                        printerService.printTextWithFont("??????" + YWStringUtils.getStanMoney((total - Float.parseFloat(printBean.getPaymoney()))) + "???" + "\n", "", fontsizeContent, null);
                    }
                    if (printBean.getOnlion() && printBean.getDeliveryType().equals("3")) {
                        printerService.printTextWithFont("?????????" + printBean.getFreight() + "???" + "\n", "", fontsizeContent, null);
                        printerService.printTextWithFont(divide, "", fontsizeContent, null);
                    }

                    printerService.printTextWithFont("??????" + YWStringUtils.getStanMoney(Float.parseFloat(printBean.getPaymoney())) + "?????????????????????" + printBean.getPaytype() + "\n", "", fontsizeContent, null);
                    printerService.printTextWithFont(divide, "", fontsizeContent, null);

                    printerService.setAlignment(1, null);

                    printerService.printTextWithFont("????????????  ????????????\n", "", 20, null);
                    printerService.printTextWithFont("???????????????????????????????????????????????????\n", "", 20, null);

                    printerService.lineWrap(1, null);


//                    Bitmap logoBtm = getImageFromAssetsFile("wechat_img.jpg");
//                    if (null != logoBtm && logoBtm.getWidth() > 310) {
//                        int newHeight = (int) (1.0 * logoBtm.getHeight() * 310 / logoBtm.getWidth());
//                        logoBtm = ImageUtils.scale(logoBtm, 300, newHeight);
//                    }
//                    printerService.printBitmap(logoBtm, null);
//                    printerService.printText("\n", null);

                    String content = StoreInfo.getStore().getCode_url();

                    Bitmap bitmap = null;
                    try {

                        bitmap = Glide.with(context)
                                .asBitmap()
                                .load(StoreInfo.getStore().getCode_url())
                                .submit(300, 300).get();


//                        bitmap = UrlUtils.create2DCode(content);
                        if (null != bitmap && bitmap.getWidth() > 310) {
                            int newHeight = (int) (1.0 * bitmap.getHeight() * 310 / bitmap.getWidth());
                            bitmap = ImageUtils.scale(bitmap, 300, 300);
                        }
                        printerService.printBitmap(bitmap, null);
                        printerService.printText("\n", null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    printerService.setAlignment(0, null);

//                    printerService.printTextWithFont("???????????????????????????????????????????????????????????????????????????????????????48??????????????????????????????????????????????????????????????????\n", "", 20, null);
//                    Bitmap fpbitmap = null;
//                    try {
//
//                        fpbitmap = BitmapUtil.createQRImage(StoreInfo.getStore().getElectronicInvoices_url()+printBean.getOrder_sn()+"",300,300);
//
//                        printerService.printBitmap(fpbitmap, null);
//                        printerService.printText("\n", null);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    printerService.lineWrap(1, null);


                    printerService.cutPaper(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void print2(PrintBean print, int sign) {
        //sign=1?????????     sign=2???????????????
        this.printBean = BeanCloneUtil.cloneTo(print);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.myBinder == null) {
                    return;
                }
                MainActivity.myBinder.WriteSendData(new TaskCallback() {
                    @Override
                    public void OnSucceed() {


                    }

                    @Override
                    public void OnFailed() {

                    }
                }, new ProcessData() {
                    @Override
                    public List<byte[]> processDataBeforeSend() {
                        String divide = "--------------------------------------------\n";
                        int width = divide.length();
                        list = new ArrayList<>();
                        list.add(DataForSendToPrinterPos80.initializePrinter());
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));//??????????????????

                        try {
                            Bitmap bitmap = Bitmap.createBitmap(560, 40, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            Paint paint = new Paint();
                            paint.setTextSize(40);//??????????????????
                            paint.setColor(Color.parseColor("#000000"));
                            canvas.drawColor(Color.parseColor("#ffffff"));
                            canvas.drawText("?????????" + AppApplication.orderType + printBean.getOrderNumberBean().getOrderNumberStr(), 150, 30, paint);//????????????paint
                            List<Bitmap> blist = new ArrayList<>();
                            blist = BitmapProcess.cutBitmap(30, bitmap);
                            for (int i = 0; i < blist.size(); i++) {
                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 560));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        list.add(StringUtils.strTobytes("?????????" + StoreInfo.getStore().getStore_name() + "\n"));
                        list.add(StringUtils.strTobytes("?????????" + StoreInfo.getStore().getStore_address() + "\n"));
                        list.add(StringUtils.strTobytes("????????????" + printBean.getCashier() + "\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + printBean.getOrder_sn() + "\n"));
                        list.add(StringUtils.strTobytes("???????????????" + printBean.getCreatTime() + "\n"));
                        list.add(DataForSendToPrinterPos80.printAndFeedLine());
                        list.add(StringUtils.strTobytes("?????????          ??????     ??????          ??????\n"));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        int count = 0;
                        float total = 0f;

                        width = divide.length();
                        if (printBean.getShopCarYWGoodBeans().size() == 0) {
                            list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                            return list;
                        }
                        for (int i = 0; i < printBean.getShopCarYWGoodBeans().size(); i++) {
                            if (printBean.getShopCarYWGoodBeans().get(i) instanceof YWGoodBean) {
                                count += ((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getBuynum();
                                total += ((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getBuynum() * Double.parseDouble(((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getPrice());
                                printGoods2((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i), width, sign);//????????????????????????????????????????????????????????????

                            } else {
                                MealsItemBean itemsBean = (MealsItemBean) printBean.getShopCarYWGoodBeans().get(i);
                                count += itemsBean.getBuynum();
                                total += itemsBean.getBuynum() * Double.parseDouble(itemsBean.getMeal_goods_price());
                                printGoods2(itemsBean, width);

                            }

                        }
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(StringUtils.strTobytes("???" + count + "?????????????????? " + YWStringUtils.getStanMoney((float) total) + "???" + "??????????????? " + YWStringUtils.getStanMoney(((total + Float.parseFloat(printBean.getFreight())) - Float.parseFloat(printBean.getPaymoney()))) + "??? \n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        if (printBean.getBean() != null) {
//                            list.add(StringUtils.strTobytes("?????????" + printBean.getBean().getName() + "     ?????? " + YWStringUtils.getStanMoney(((total+Float.parseFloat(printBean.getFreight())) - Float.parseFloat(printBean.getPaymoney()))) + "??? \n") );
                            list.add(StringUtils.strTobytes("?????????" + printBean.getBean().getName() + " \n"));
                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                            list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        }
                        if (printBean.getCouponBean() != null) {   //todo  ???????????????????????????
                            list.add(StringUtils.strTobytes("??????????????????" + printBean.getCouponBean().getName() + "\n"));
                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                            list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                            if (printBean.getCouponBean().getType().equals("5")) {
                                list.add(StringUtils.strTobytes("??????" + printBean.getCouponPrice() + "???" + "\n"));
                            } else {
//                                list.add(StringUtils.strTobytes("??????" + YWStringUtils.getStanMoney(((total+Float.parseFloat(printBean.getFreight())) - Float.parseFloat(printBean.getPaymoney()))) + "???" + "     ???????????? " +printBean.getFreight()+"???"+ "\n"));
                            }
                        } else {
//                            list.add(StringUtils.strTobytes("??????" + YWStringUtils.getStanMoney(((total+Float.parseFloat(printBean.getFreight())) - Float.parseFloat(printBean.getPaymoney()))) + "???" + "     ???????????? " +printBean.getFreight()+"???"+ "\n"));

                        }
                        if (printBean.getOnlion() && printBean.getDeliveryType().equals("3")) {
                            list.add(StringUtils.strTobytes("???????????? " + printBean.getFreight() + "???" + "\n"));
                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                            list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        }

                        list.add(StringUtils.strTobytes("??????" + YWStringUtils.getStanMoney(Float.parseFloat(printBean.getPaymoney())) + "?????????????????????" + printBean.getPaytype() + "\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                        list.add(StringUtils.strTobytes("-----------------------------------------------" + "\n"));
                        list.add(StringUtils.strTobytes("??????:" + printBean.getRemarks() + "" + "\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(0, 00));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(160, 00));
                        list.add(StringUtils.strTobytes("????????????  ????????????\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(90, 00));
                        list.add(StringUtils.strTobytes("???????????????????????????????????????????????????\n"));


                        list.add(DataForSendToPrinterPos80.printAndFeedLine());


                        Bitmap bitmap = null;
                        try {
                            bitmap = Glide.with(context)
                                    .asBitmap()
                                    .load(StoreInfo.getStore().getCode_url())
                                    .submit(300, 300).get();

                            if (null != bitmap && bitmap.getWidth() > 310) {
                                int newHeight = (int) (1.0 * bitmap.getHeight() * 310 / bitmap.getWidth());
                                bitmap = ImageUtils.scale(bitmap, 300, 300);
                            }

                            list.add(DataForSendToPrinterPos80.initializePrinter());
                            List<Bitmap> blist = BitmapProcess.cutBitmap(150, bitmap);
                            for (int i = 0; i < blist.size(); i++) {
                                list.add(DataForSendToPrinterPos80.printRasterBmp(0, blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center, 576));
                            }
                            list.add(DataForSendToPrinterPos80.printAndFeedLine());

                            list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 00));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100, 01));
                        list.add(StringUtils.strTobytes("\n"));
                        list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(0));
                        return list;
                    }
                });
            }
        }).start();
    }

//    public void print3(PrintBean print) {
//
//        this.printBean = BeanCloneUtil.cloneTo(print);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int fontsizeContent = 24;
//
//                String divide = "-------------------------------\n";
//                int width = divide.length();
//                try {
//                    if (printerService.updatePrinterState() != 1) {
//                        return;
//                    }
//                    printerService.setAlignment(1, null);//0?????????   1???  2???
//                    if (printBean.getOnlion()) {
//                        printerService.printTextWithFont("?????????:" + printBean.getFoodCode() + "??????????????????", "", 33, null);
//                        printerService.lineWrap(1, null);
//                        printerService.setAlignment(0, null);//0?????????   1???  2???
//                        if (printBean.getDeliveryType().equals("1")) {
//                            printerService.printTextWithFont("???????????????????????????" + "\n", "", fontsizeContent, null);
//                        } else if (printBean.getDeliveryType().equals("2")) {
//                            printerService.printTextWithFont("???????????????????????????" + "\n", "", fontsizeContent, null);
//                        } else if (printBean.getDeliveryType().equals("3")) {
//                            printerService.printTextWithFont("?????????????????????" + "\n", "", fontsizeContent, null);
//                            printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                            printerService.printTextWithFont("???????????????" + printBean.getOrderAddress() + "\n", "", fontsizeContent,
//                                    null);
//                        }
//                    } else {
//                        printerService.printTextWithFont("?????????" + AppApplication.orderType + printBean.getOrderNumberBean().getOrderNumberStr() + "\n", "", 33, null);
//                    }
//
//                    printerService.setAlignment(0, null);//0?????????   1???  2???
//
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//
//                    printerService.printTextWithFont("?????????" + StoreInfo.getStore().getStore_name() + "\n", "", fontsizeContent, null);
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                    printerService.printTextWithFont("????????????" + printBean.getCashier() + "\n", "", fontsizeContent,
//                            null);
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                    printerService.printTextWithFont("???????????????" + printBean.getOrder_sn() + "\n", "", fontsizeContent, null);
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                    printerService.printTextWithFont("???????????????" + printBean.getCreatTime() + "\n", "", fontsizeContent, null);
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                    printerService.printTextWithFont("?????????                   ??????\n", "", fontsizeContent, null);
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//
//                    int count = 0;
//                    float total = 0f;
//                    for (int i = 0; i < printBean.getShopCarYWGoodBeans().size(); i++) {
//                        if (printBean.getShopCarYWGoodBeans().get(i) instanceof YWGoodBean) {
//                            count += ((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getBuynum();
//                            total += ((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getBuynum() * Double.parseDouble(((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i)).getPrice());
//                            printGoods3((YWGoodBean) printBean.getShopCarYWGoodBeans().get(i), width);//????????????????????????????????????????????????????????????
//                        } else {
//                            printGoods3((MealListBean.ItemsBean) printBean.getShopCarYWGoodBeans().get(i), width);
//                            MealListBean.ItemsBean itemsBean = (MealListBean.ItemsBean) printBean.getShopCarYWGoodBeans().get(i);
//                            total += Double.parseDouble(itemsBean.getRealprice());
//
//                            for (MealListBean.ItemsBean.GroupItemBean groupItemBean : itemsBean.getGroupItem()) {
//                                if (Integer.parseInt(groupItemBean.getIsFixed()) == 1) {
//                                    for (MealListBean.ItemsBean.GroupItemBean.GroupGoodsBean groupGood : groupItemBean.getGroupGoods()) {
//                                        count += Integer.parseInt(groupGood.getNumber());
//                                    }
//                                } else {
//                                    count += Integer.parseInt(groupItemBean.getGroupGoods().get(groupItemBean.getCurrentSelect()).getNumber());
//                                }
//                            }
//                        }
//
//
//                    }
//
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                    printerService.printTextWithFont("???" + count + "??????????????????" + YWStringUtils.getStanMoney((float) total) + "???" + "\n", "", fontsizeContent, null);
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                    if (printBean.getBean() != null) {
//                        printerService.printTextWithFont("??????" + printBean.getBean().getName() + "    ?????? " + YWStringUtils.getStanMoney(((total + Float.parseFloat(printBean.getFreight())) - Float.parseFloat(printBean.getPaymoney()))) + "??? \n", "", fontsizeContent, null);
//                        printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                    }
//                    if (printBean.getCouponBean() != null) {
//                        printerService.printTextWithFont(printBean.getCouponBean().getName() + "\n", "", fontsizeContent, null);
//                        printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                        if (printBean.getCouponBean().getType().equals("5")) {//?????????
//                            printerService.printTextWithFont("??????" + printBean.getCouponPrice() + "???" + "\n", "", fontsizeContent, null);
//                        } else {
////                            printerService.printTextWithFont("??????" + YWStringUtils.getStanMoney((total - Float.parseFloat(printBean.getPaymoney()))) + "???" + "\n", "", fontsizeContent, null);
//                        }
//
//                    } else {
////                        printerService.printTextWithFont("??????" + YWStringUtils.getStanMoney((total - Float.parseFloat(printBean.getPaymoney()))) + "???" + "\n", "", fontsizeContent, null);
//                    }
//
//                    if (printBean.getOnlion() && printBean.getDeliveryType().equals("3")) {
//                        printerService.printTextWithFont("?????????" + printBean.getFreight() + "???" + "\n", "", fontsizeContent, null);
//                        printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                    }
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//                    printerService.printTextWithFont("??????" + YWStringUtils.getStanMoney(Float.parseFloat(printBean.getPaymoney())) + "?????????????????????" + printBean.getPaytype() + "\n", "", fontsizeContent, null);
//                    printerService.printTextWithFont(divide, "", fontsizeContent, null);
//
//                    printerService.setAlignment(1, null);
//
//                    printerService.printTextWithFont("????????????  ????????????\n", "", 20, null);
//                    printerService.printTextWithFont("???????????????????????????????????????????????????\n", "", 20, null);
//
//                    printerService.lineWrap(1, null);
//
//
////                    Bitmap logoBtm = getImageFromAssetsFile("wechat_img.jpg");
////                    if (null != logoBtm && logoBtm.getWidth() > 310) {
////                        int newHeight = (int) (1.0 * logoBtm.getHeight() * 310 / logoBtm.getWidth());
////                        logoBtm = ImageUtils.scale(logoBtm, 300, newHeight);
////                    }
////                    printerService.printBitmap(logoBtm, null);
////                    printerService.printText("\n", null);
//
////                    printerService.printTextWithFont("?????????????????????????????????????????????\n", "", 20, null);
//                    String content = StoreInfo.getStore().getCode_url();
//
//                    Bitmap bitmap = null;
//                    try {
//
//                        bitmap = Glide.with(context)
//                                .asBitmap()
//                                .load(StoreInfo.getStore().getCode_url())
//                                .submit(300, 300).get();
////                        bitmap = UrlUtils.create2DCode(content);
//                        if (null != bitmap && bitmap.getWidth() > 310) {
//                            int newHeight = (int) (1.0 * bitmap.getHeight() * 310 / bitmap.getWidth());
//                            bitmap = ImageUtils.scale(bitmap, 300, 300);
//                        }
//                        printerService.printBitmap(bitmap, null);
//                        printerService.printText("\n", null);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    printerService.setAlignment(0, null);
////                    printerService.printTextWithFont("???????????????????????????????????????????????????????????????????????????????????????48??????????????????????????????????????????????????????????????????\n", "", 20, null);
////                    Bitmap fpbitmap = null;
////                    try {
////
////                        fpbitmap = BitmapUtil.createQRImage(StoreInfo.getStore().getElectronicInvoices_url()+printBean.getOrder_sn()+"",300,300);
////
////                        printerService.printBitmap(fpbitmap, null);
////                        printerService.printText("\n", null);
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//
//                    printerService.lineWrap(1, null);
//
//                    printerService.cutPaper(null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }

    /**
     * ???Assets???????????????
     */
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    //????????????
    private void printGoods(YWGoodBean ywGoodBean, int width) {

        int blank1;
        int blank2;
        int blank3;
//        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);
        int maxNameWidth = 5;

        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        String name = "";


        if (ywGoodBean.getAttribute() != null && !ywGoodBean.getAttribute().equals(null)) {
            if (!ywGoodBean.getAttribute().equals("null")) {
                if (ywGoodBean.getAttribute().length() < 6) {
                    name = ywGoodBean.getProduct_name() + "(" + ywGoodBean.getAttribute() + ")";
                } else {
                    name = ywGoodBean.getProduct_name() + "(" + ywGoodBean.getAttribute().substring(0, 6) + ")";
                }
            } else {
                name = ywGoodBean.getProduct_name();
            }
        } else {
            name = ywGoodBean.getProduct_name();
        }

        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

        blank1 = width * 1 / 3 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;

        sb.append(name.length() > maxNameWidth ? name1 : name);

        sb.append(addblank(blank1));
        sb.append(ywGoodBean.getPrice());
        blank2 = width * 1 / 4 - String.valueOf(ywGoodBean.getPrice()).length();
        sb.append(addblank(blank2));


        sb.append("" + ywGoodBean.getBuynum());
        blank3 = width * 1 / 4 - String.valueOf(ywGoodBean.getBuynum()).length();
        sb.append(addblank(blank3));

        sb.append(YWStringUtils.getStanMoney(
                ywGoodBean.getBuynum() * Float.parseFloat(ywGoodBean.getPrice())));


        try {
            printerService.printTextWithFont(sb.toString() + "\n", "", 22, null);
            sb.setLength(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //????????????
    private void printGoods2(YWGoodBean ywGoodBean, int width, int sign) {

        int blank1;
        int blank2;
        int blank3;
        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);

        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        String name = "";

        name = ywGoodBean.getProduct_name();

        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

        blank1 = width * 1 / 3 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;

        sb.append(name.length() > maxNameWidth ? name1 : name);
        sb.append(addblank(blank1));

        sb.append(ywGoodBean.getPrice());
        blank2 = width * 1 / 4 - String.valueOf(ywGoodBean.getPrice()).length();
        sb.append(addblank(blank2));


        sb.append("" + ywGoodBean.getBuynum());
        blank3 = width * 1 / 4 - String.valueOf(ywGoodBean.getBuynum()).length();
        sb.append(addblank(blank3));
        if (sign == 1) {
            Double realMoney = Double.parseDouble(ywGoodBean.getDiscount()) * Double.parseDouble(ywGoodBean.getPrice()) * ywGoodBean.getBuynum() / 100; //????????????????????????
            sb.append("" + BigDecimalUtils.formatRoundUp(realMoney, 2));
        } else {
            Double preMoney = BigDecimalUtils.multiply(ywGoodBean.getPrice(), ywGoodBean.getBuynum() + "");//?????????????????????
            Double realMoney = BigDecimalUtils.subtract(preMoney + "", ywGoodBean.getDiscount_price());//??????-?????????????????????????????????
            sb.append("" + realMoney);
        }
//        sb.append(YWStringUtils.getStanMoney(
//                ywGoodBean.getBuynum() * Float.parseFloat(ywGoodBean.getPrice())));
        try {
            list.add(StringUtils.strTobytes(sb.toString() + "\n"));
            sb.setLength(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //??????????????????
    private void printGoods2(MealGoodsBean ywGoodBean, int width) {

        int blank1;
        int blank2;
        int blank3;
        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);

        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        String name = "";

        name = "  " + ywGoodBean.getGoods_name();

        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

        blank1 = width * 1 / 3 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;

        sb.append(name.length() > maxNameWidth ? name1 : name);
        sb.append(addblank(blank1));

        sb.append(ywGoodBean.getSell_price());
        blank2 = width * 1 / 4 - String.valueOf(ywGoodBean.getSell_price()).length();
        sb.append(addblank(blank2));

        sb.append("" + ywGoodBean.getGoods_number());
        blank3 = width * 1 / 4 - String.valueOf(ywGoodBean.getGoods_number()).length();
        sb.append(addblank(blank3));

        try {
            list.add(StringUtils.strTobytes(sb.toString() + "\n"));
            sb.setLength(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //????????????
    private void printGoods3(YWGoodBean ywGoodBean, int width) {

        int blank1;
        int blank2;
        int blank3;

//        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);
        int maxNameWidth = 10;
//        Log.d(TAG, "printGoods3===maxNameWidth: "+maxNameWidth);
//        Log.d(TAG, "printGoods3===width: "+width);
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        String name = "";


        if (ywGoodBean.getAttribute() != null && !ywGoodBean.getAttribute().equals(null)) {
            if (!ywGoodBean.getAttribute().equals("null")) {
                if (ywGoodBean.getAttribute().length() < 6) {
                    name = ywGoodBean.getProduct_name() + "(" + ywGoodBean.getAttribute() + ")";
                } else {
                    name = ywGoodBean.getProduct_name() + "(" + ywGoodBean.getAttribute().substring(0, 6) + ")";
                }
            } else {
                name = ywGoodBean.getProduct_name();
            }
        } else {
            name = ywGoodBean.getProduct_name();
        }

        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

        blank1 = width * 1 / 3 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;

        sb.append(name.length() > maxNameWidth ? name1 : name);

        sb.append(addblank(blank1));
        sb.append("  ");
        blank2 = width * 1 / 4 - String.valueOf(ywGoodBean.getPrice()).length();
        sb.append(addblank(blank2));


        sb.append("  ");
        blank3 = width * 1 / 4 - String.valueOf(ywGoodBean.getBuynum()).length();
        sb.append(addblank(blank3));

        sb.append("" + ywGoodBean.getBuynum());


        try {
            printerService.printTextWithFont(sb.toString() + "\n", "", 24, null);
            sb.setLength(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //????????????
    private void printGoods(MealsItemBean itemsBean, int width) {

        int blank1;
        int blank2;
        int blank3;
        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);

        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        String name = "";

        name = "[???]" + itemsBean.getMeal_goods_name();

        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

        blank1 = width * 1 / 3 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;

        sb.append(name.length() > maxNameWidth ? name1 : name);
        sb.append(addblank(blank1));

        sb.append(itemsBean.getMeal_goods_price());
        blank2 = width * 1 / 4 - String.valueOf(itemsBean.getMeal_goods_price()).length();
        sb.append(addblank(blank2));


        sb.append("1");
        blank3 = width * 1 / 4 - String.valueOf(1).length();
        sb.append(addblank(blank3));

        sb.append(YWStringUtils.getStanMoney(
                1 * Float.parseFloat(itemsBean.getMeal_goods_price())));

        try {
            printerService.printTextWithFont(sb.toString() + "\n", "", 24, null);
            sb.setLength(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //????????????
    private void printGoods2(MealsItemBean itemsBean, int width) {

        int blank1;
        int blank2;
        int blank3;
        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);

        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        String name = "";

        name = "[???]" + itemsBean.getMeal_goods_name();

        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

        blank1 = width * 1 / 3 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;

        sb.append(name.length() > maxNameWidth ? name1 : name);
        sb.append(addblank(blank1));

        sb.append(itemsBean.getMeal_goods_price());
        blank2 = width * 1 / 4 - String.valueOf(itemsBean.getMeal_goods_price()).length();
        sb.append(addblank(blank2));


        sb.append("1");
        blank3 = width * 1 / 4 - String.valueOf(1).length();
        sb.append(addblank(blank3));

        sb.append(YWStringUtils.getStanMoney(
                1 * Float.parseFloat(itemsBean.getMeal_goods_price())));

        try {
            list.add(StringUtils.strTobytes(sb.toString() + "\n"));
            for (int i = 0; i < itemsBean.getMeal_store_goods_detail().size(); i++) {
                printGoods2(itemsBean.getMeal_store_goods_detail().get(i), width);
            }
            sb.setLength(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //????????????
//    private void printGoods3(MealListBean.ItemsBean itemsBean, int width) {
//
//        int blank1;
//        int blank2;
//        int blank3;
//        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);
//
//        StringBuffer sb = new StringBuffer();
//        sb.setLength(0);
//        String name = "";
//
//        name = itemsBean.getName();
//
//        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";
//
//        blank1 = width * 1 / 3 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;
//
//        sb.append(name.length() > maxNameWidth ? name1 : name);
//        sb.append(addblank(blank1));
//
//        sb.append("  ");
//        blank2 = width * 1 / 4 - String.valueOf(itemsBean.getRealprice()).length();
//        sb.append(addblank(blank2));
//
//
//        sb.append(" ");
//        blank3 = width * 1 / 4 - String.valueOf(1).length();
//        sb.append(addblank(blank3));
//
//        sb.append("1");
//
//        try {
//            printerService.printTextWithFont(sb.toString() + "\n", "", 24, null);
//
//            sb.setLength(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //???????????????????????????????????????????????????
    private void printCheckNumGoods(YWGoodBean ywGoodBean, int width) {

        int blank1;
        int blank2;
        int blank3;
        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);

        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        String name = "";

        name = ywGoodBean.getProduct_name();

        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

        blank1 = width * 2 / 3 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;

        sb.append(name.length() > maxNameWidth ? name1 : name);
        sb.append(addblank(blank1));

        sb.append("" + ywGoodBean.getNum());
        blank2 = width * 1 / 3 - String.valueOf(ywGoodBean.getInventory()).length();
        sb.append(addblank(blank2));

        try {
            list.add(StringUtils.strTobytes(sb.toString() + "\n"));
            sb.setLength(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //???????????????????????????????????????????????????
    private void printInventoryGoods(YWGoodBean ywGoodBean, int width) {

        int blank1;
        int blank2;
        int blank3;
        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);

        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        String name = "";

        name = ywGoodBean.getProduct_name();

        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

        blank1 = width * 1 / 2 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;

        sb.append(name.length() > maxNameWidth ? name1 : name);
        sb.append(addblank(blank1));

        sb.append("" + ywGoodBean.getInventory());
        blank2 = width * 1 / 3 - String.valueOf(ywGoodBean.getInventory()).length();
        sb.append(addblank(blank2));


        sb.append("" + ywGoodBean.getNum());
        blank3 = width * 1 / 3 - String.valueOf(ywGoodBean.getNum()).length();
        sb.append(addblank(blank3));

        try {
            list.add(StringUtils.strTobytes(sb.toString() + "\n"));
            sb.setLength(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //???????????? ??????????????????????????????
    private void printApplyGoods(YWGoodBean ywGoodBean, int width) {

        int blank1;
        int blank2;
        int blank3;
        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);

        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        String name = "";

        name = ywGoodBean.getProduct_name();

        String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

        blank1 = width * 1 / 2 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;

        sb.append(name.length() > maxNameWidth ? name1 : name);
        sb.append(addblank(blank1));

        sb.append(ywGoodBean.getNum());
        blank2 = width * 1 / 3 - String.valueOf(ywGoodBean.getPrice()).length();
        sb.append(addblank(blank2));


        sb.append("" + ywGoodBean.getUnit());
        blank3 = width * 1 / 3 - String.valueOf(ywGoodBean.getBuynum()).length();
        sb.append(addblank(blank3));

        try {
            list.add(StringUtils.strTobytes(sb.toString() + "\n"));
            sb.setLength(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private String formatTitle(int width) {
//        Log.e("@@@@@", width + "=======");
//
//        String[] title = {
//                context.getString(R.string.shop_car_goods_name),
//                context.getString(R.string.menus_unit_price),
//                context.getString(R.string.menus_unit_num),
//                context.getString(R.string.shop_car_unit_money),
//        };
//        StringBuffer sb = new StringBuffer();
//        int blank1 = width * 1 / 3 - String_length(title[0]);
//        int blank2 = width * 1 / 4 - String_length(title[1]);
//        int blank3 = width * 1 / 4 - String_length(title[2]);
//
//        sb.append(title[0]);
//        sb.append(addblank(blank1));
//
//        sb.append(title[1]);
//        sb.append(addblank(blank2));
//
//        sb.append(title[2]);
//        sb.append(addblank(blank3));
//
//        sb.append(title[3]);
//
//        return sb.toString();
//    }

//    private int printGoods(boolean isVip, List<YWGoodBean> goodBeanList, int fontsizeContent, String divide2, int width) throws RemoteException {
//        int totalNum = 0;
//        int blank1;
//        int blank2;
//        int blank3;
//        int maxNameWidth = isZh() ? (width * 1 / 3 - 2) / 2 : (width * 1 / 3 - 2);
//
//        StringBuffer sb = new StringBuffer();
//        for (YWGoodBean goodBean : goodBeanList) {
//            sb.setLength(0);
//
//            String name = goodBean.getProduct_name();
//            String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";
//
//            blank1 = width * 1 / 3 - String_length(name.length() > maxNameWidth ? name1 : name) + 1;
//
//            sb.append(name.length() > maxNameWidth ? name1 : name);
//            sb.append(addblank(blank1));
//
//            if (!TextUtils.isEmpty(goodBean.getReset_price_time()) && !TextUtils.isEmpty(goodBean.getReset_price()) && DateUtils.isToday(Long.parseLong(goodBean.getReset_price_time()))) {
//
//                sb.append(goodBean.getReset_price());
//                blank2 = width * 1 / 4 - String.valueOf(goodBean.getReset_price()).length();
//            } else {
//                if (isVip) {
//                    sb.append(goodBean.getVip_price());
//                    blank2 = width * 1 / 4 - String.valueOf(goodBean.getVip_price()).length();
//                } else {
//                    sb.append(goodBean.getPrice());
//                    blank2 = width * 1 / 4 - String.valueOf(goodBean.getPrice()).length();
//                }
//            }
//            sb.append(addblank(blank2));
//
//
//            int goodType = STANDARD_GOOD;
//            if (!TextUtils.isEmpty(goodBean.getIs_standard()) && goodBean.getIs_standard().equals(
//                    "" + ATYPID_GOOD)) {
//                goodType = ATYPID_GOOD;
//            }
//            if (goodType == ATYPID_GOOD) {
//                sb.append("" + goodBean.getAtypidNum());
//                blank3 = width * 1 / 4 - String.valueOf(goodBean.getAtypidNum()).length();
//                totalNum++;
//            } else {
//                sb.append("" + goodBean.getOptNum());
//                blank3 = width * 1 / 4 - goodBean.getOptNum();
//                totalNum += goodBean.getOptNum();
//            }
//
//            sb.append(addblank(blank3));
//
//            if (goodType == ATYPID_GOOD) {
//
//                if (!TextUtils.isEmpty(goodBean.getReset_price_time()) && !TextUtils.isEmpty(goodBean.getReset_price()) && DateUtils.isToday(Long.parseLong(goodBean.getReset_price_time()))) {
//
//                    sb.append(YWStringUtils.getStanMoney(
//                            goodBean.getAtypidNum() * Float.parseFloat(goodBean.getReset_price())));
//                } else {
//                    if (isVip) {
//                        sb.append(YWStringUtils.getStanMoney(
//                                goodBean.getAtypidNum() * Float.parseFloat(goodBean.getVip_price())));
//                    } else {
//                        sb.append(YWStringUtils.getStanMoney(
//                                goodBean.getAtypidNum() * Float.parseFloat(goodBean.getPrice())));
//                    }
//                }
//            } else {
//                if (isVip) {
//                    sb.append(YWStringUtils.getStanMoney(
//                            goodBean.getOptNum() * Float.parseFloat(goodBean.getVip_price())));
//                } else {
//                    sb.append(YWStringUtils.getStanMoney(
//                            goodBean.getOptNum() * Float.parseFloat(goodBean.getPrice())));
//                }
//            }
//
//            printerService.printTextWithFont(sb.toString() + "\n", "", fontsizeContent, null);
//        }
//        sb.setLength(0);
//        return totalNum;
//    }

    private String formatData(long nowTime) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return time.format(nowTime);
    }

    private String addblank(int count) {
        String st = "";
        if (count < 0) {
            count = 0;
        }
        for (int i = 0; i < count; i++) {
            st = st + " ";
        }
        return st;
    }

    private static final byte ESC = 0x1B;// Escape

    /**
     * ????????????
     */
    private byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * ??????????????????
     */
    private byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

    private boolean isZh() {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    private int String_length(String rawString) {
        return rawString.replaceAll("[\\u4e00-\\u9fa5]", "SH").length();
    }
}
