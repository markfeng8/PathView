package com.example.myapplication;


import android.database.Cursor;

import org.xutils.DbManager;
import org.xutils.db.table.DbModel;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 * 数据库操作工具类
 */
public class DButils {

    private static String DB_PATH = "data/data/com.example.myapplication/databases/";
    private static String DB_NAME = "PathDatas.db";

    static DbManager.DaoConfig daoConfig;
    public static DbManager dbManager = getDBmanager();

    public static DbManager.DaoConfig getDaoConfig() {
        File file = new File(DB_PATH);//设置数据库路径
        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig()
                    .setDbName(DB_NAME)//设置数据库名称
                    .setDbDir(file)
                    .setDbVersion(1)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    }).setTableCreateListener(new DbManager.TableCreateListener() {
                        @Override
                        public void onTableCreated(DbManager db, TableEntity<?> table) {

                        }
                    });
        }
        return daoConfig;
    }

    public static DbManager getDBmanager() {
        DbManager manager = x.getDb(getDaoConfig());
        return manager;
    }

    /**
     * 获取起点楼层
     *
     * @return
     */
    public static int getStartFloor() {
        try {
            Cursor cursor = dbManager.execQuery("select * from Tb_Point where PointType=\"起点\"");
            while (cursor.moveToFirst()) {
                String flootStr = cursor.getString(cursor.getColumnIndex("Floor"));
                return Integer.parseInt(flootStr.replaceAll("F", ""));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int getPointFloor(String pointName) {
        try {
            Cursor cursor = dbManager.execQuery("select * from Tb_Point where PointName=\"" + pointName + "\"");
            while (cursor.moveToFirst()) {
                String flootStr = cursor.getString(cursor.getColumnIndex("Floor"));
                return Integer.parseInt(flootStr.replaceAll("F", ""));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static List<Routes> getRoutePoints(String pointName) {
        List<Routes> list = new ArrayList<>();
        Routes routes;
        try {
            Cursor cursor = dbManager.execQuery("select * from Tb_Point where PointName=\"" + pointName + "\"");
            while (cursor.moveToFirst()) {
                String ID = cursor.getString(cursor.getColumnIndex("ID"));
                Cursor rCursor = dbManager.execQuery("select * from Tb_Routes where PointID=\"" + ID + "\"");
                while (rCursor.moveToNext()) {
                    routes = new Routes();
                    routes.setPointX(rCursor.getFloat(rCursor.getColumnIndex("PointX")));
                    routes.setPointY(rCursor.getFloat(rCursor.getColumnIndex("PointY")));
                    list.add(routes);
                }
                return list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getRightLouTi(String pointName) {
        //该点坐标数据
        List<Routes> list = DButils.getRoutePoints(pointName);
        List<Routes> lPointList = new ArrayList<>();
        Routes routes;
        try {
            List<DbModel> lList = dbManager.selector(Point.class).select("ID")
                    .where("PointType", "=", "楼梯").findAll();
            for (DbModel dbModel : lList) {
                String ID = dbModel.getString("ID");
                List<DbModel> pList = dbManager.selector(Routes.class)
                        .select("PointX", "PointY")
                        .where("PointID", "=", ID).findAll();
                float PointX = pList.get(pList.size() - 1).getFloat("PointX");
                float PointY = pList.get(pList.size() - 1).getFloat("PointY");
                routes = new Routes();
                routes.setPointX(PointX);
                routes.setPointY(PointY);
                routes.setPointID(ID);
                lPointList.add(routes);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        float animX = list.get(0).getPointX();
        float animY = list.get(0).getPointY();


        List<StairsBean> stairsBeanList = new ArrayList<>();
        StairsBean bean;
        for (Routes routes1 : lPointList) {
            double distance = Math.sqrt(Math.abs(
                    (animX - routes1.getPointX())
                            * (animY - routes1.getPointX())
                            + (animY - routes1.getPointY())
                            * (animY - routes1.getPointY())));
            bean = new StairsBean();
            bean.setID(routes1.getPointID());
            bean.setDistance(distance);
            stairsBeanList.add(bean);
        }
        Collections.sort(stairsBeanList, new Comparator<StairsBean>() {

            @Override
            public int compare(StairsBean o1, StairsBean o2) {
                double distance1 = o1.getDistance();
                double distance2 = o2.getDistance();
                if (distance1 == distance2) {
                    return 0;
                } else {
                    // 从小到大
                    return distance1 > distance2 ? 1 : -1;
                    // 如果需要从大到小，可以将return的值反过来即可
                }
            }

        });

        try {
            String ID = stairsBeanList.get(0).getID();
            List<DbModel> lList = dbManager.selector(Point.class).select("PointName")
                    .where("ID", "=", ID).findAll();
            return lList.get(0).getString("PointName");
        } catch (DbException e) {
            e.printStackTrace();
        }
        return "";
    }
}