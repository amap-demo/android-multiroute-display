package com.amap.multiroute.util;

import android.text.Html;
import android.text.Spanned;

import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStep;
import com.amap.multiroute.Bean.StrategyBean;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class Utils {
    private static DecimalFormat fnum = new DecimalFormat("##0.0");
    public static final int AVOID_CONGESTION = 4;  // 躲避拥堵
    public static final int AVOID_COST = 5;  // 避免收费
    public static final int AVOID_HIGHSPEED = 6; //不走高速
    public static final int PRIORITY_HIGHSPEED = 7; //高速优先

    public static final int START_ACTIVITY_REQUEST_CODE = 1;
    public static final int ACTIVITY_RESULT_CODE = 2;

    public static final String INTENT_NAME_AVOID_CONGESTION = "AVOID_CONGESTION";
    public static final String INTENT_NAME_AVOID_COST = "AVOID_COST";
    public static final String INTENT_NAME_AVOID_HIGHSPEED = "AVOID_HIGHSPEED";
    public static final String INTENT_NAME_PRIORITY_HIGHSPEED = "PRIORITY_HIGHSPEED";


    public static String getFriendlyTime(int s) {
        String timeDes = "";
        int h = s / 3600;
        if (h > 0) {
            timeDes += h + "小时";
        }
        int min = (int) (s % 3600) / 60;
        if (min > 0) {
            timeDes += min + "分";
        }
        return timeDes;
    }

    public static String getFriendlyDistance(int m) {
        float dis = m / 1000f;
        String disDes = fnum.format(dis) + "公里";
        return disDes;
    }

    /**
     * 计算path对应的标签
     *
     * @param paths        多路径规划回调的所有路径
     * @param ints         多路径线路ID
     * @param pathIndex    当前路径索引
     * @param strategyBean 封装策略bean
     * @return path对应标签描述
     */
    public static String getStrategyDes(HashMap<Integer, AMapNaviPath> paths, int[] ints, int pathIndex, StrategyBean strategyBean) {
        int StrategyTAGIndex = pathIndex + 1;
        String StrategyTAG = "方案" + StrategyTAGIndex;

        int minTime = Integer.MAX_VALUE;
        int minDistance = Integer.MAX_VALUE;
        int minTrafficLightNumber = Integer.MAX_VALUE;
        int minCost = Integer.MAX_VALUE;
        for (int i = 0; i < ints.length; i++) {
            if (pathIndex == i) {
                continue;
            }
            AMapNaviPath path = paths.get(ints[i]);
            if (path == null) {
                continue;
            }
            int trafficListNumber = getTrafficNumber(path);
            if (trafficListNumber < minTrafficLightNumber) {
                minTrafficLightNumber = trafficListNumber;
            }

            if (path.getTollCost() < minCost) {
                minCost = path.getTollCost();
            }

            if (path.getAllTime() < minTime) {
                minTime = path.getAllTime();
            }
            if (path.getAllLength() < minDistance) {
                minDistance = path.getAllLength();
            }
        }
        AMapNaviPath indexPath = paths.get(ints[pathIndex]);
        int indexTrafficLightNumber = getTrafficNumber(indexPath);
        int indexCost = indexPath.getTollCost();
        int indexTime = indexPath.getAllTime();
        int indexDistance = indexPath.getAllLength();
        if (indexTrafficLightNumber < minTrafficLightNumber) {
            StrategyTAG = "红绿灯少";
        }
        if (indexCost < minCost) {
            StrategyTAG = "收费较少";
        }

        if (Math.round(indexDistance / 100f) < Math.round(minDistance / 100f)) {   // 展示距离精确到千米保留一位小数，比较时按照展示数据处理
            StrategyTAG = "距离最短";
        }
        if (indexTime / 60 < minTime / 60) {    //展示时间精确到分钟，比较时按照展示数据处理
            StrategyTAG = "时间最短";
        }
        boolean isMulti = isMultiStrategy(strategyBean);
        if (strategyBean.isCongestion() && pathIndex == 0 && !isMulti) {
            StrategyTAG = "躲避拥堵";
        }
        if (strategyBean.isAvoidhightspeed() && pathIndex == 0 && !isMulti) {
            StrategyTAG = "不走高速";
        }
        if (strategyBean.isCost() && pathIndex == 0 && !isMulti) {
            StrategyTAG = "避免收费";
        }
        if (pathIndex == 0 && StrategyTAG.startsWith("方案")) {
            StrategyTAG = "推荐";
        }
        return StrategyTAG;
    }


    /**
     * 判断驾车偏好设置是否同时选中多个策略
     *
     * @param strategyBean 驾车策略bean
     * @return
     */
    public static boolean isMultiStrategy(StrategyBean strategyBean) {
        boolean isMultiStrategy = false;
        if (strategyBean.isCongestion()) {
            if (strategyBean.isAvoidhightspeed() || strategyBean.isCost() || strategyBean.isHightspeed()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        if (strategyBean.isCost()) {
            if (strategyBean.isCongestion() || strategyBean.isAvoidhightspeed()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        if (strategyBean.isAvoidhightspeed()) {
            if (strategyBean.isCongestion() || strategyBean.isCost()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        if (strategyBean.isHightspeed()) {
            if (strategyBean.isCongestion()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        return isMultiStrategy;
    }

    public static Spanned getRouteOverView(AMapNaviPath path) {
        String routeOverView = "";
        if (path == null) {
            Html.fromHtml(routeOverView);
        }

        int cost = path.getTollCost();
        if (cost > 0) {
            routeOverView += "过路费约<font color=\"red\" >" + cost + "</font>元";
        }
        int trafficLightNumber = getTrafficNumber(path);
        if (trafficLightNumber > 0) {
            routeOverView += "红绿灯" + trafficLightNumber + "个";
        }
        return Html.fromHtml(routeOverView);
    }

    public static int getTrafficNumber(AMapNaviPath path) {
        int trafficLightNumber = 0;
        if (path == null) {
            return trafficLightNumber;
        }
        List<AMapNaviStep> steps = path.getSteps();
        for (AMapNaviStep step : steps) {
            trafficLightNumber += step.getTrafficLightNumber();
        }
        return trafficLightNumber;
    }
}
