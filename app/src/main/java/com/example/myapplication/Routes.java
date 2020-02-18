package com.example.myapplication;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

//注解表名
@Table(name = "Tb_Routes")
public class Routes {
    @Column(name = "ID")
    private String ID;
    @Column(name = "PointID")
    private String PointID;
    @Column(name = "PointX")
    private float PointX;
    @Column(name = "PointY")
    private float PointY;
    @Column(name = "PointLength")
    private String PointLength;
    @Column(name = "RouteType")
    private String RouteType;
    @Column(name = "CreateDate")
    private String CreateDate;
    @Column(name = "UpdateDate")
    private String UpdateDate;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPointID() {
        return PointID;
    }

    public void setPointID(String pointID) {
        PointID = pointID;
    }

    public float getPointX() {
        return PointX;
    }

    public void setPointX(float pointX) {
        PointX = pointX;
    }

    public float getPointY() {
        return PointY;
    }

    public void setPointY(float pointY) {
        PointY = pointY;
    }

    public String getPointLength() {
        return PointLength;
    }

    public void setPointLength(String pointLength) {
        PointLength = pointLength;
    }

    public String getRouteType() {
        return RouteType;
    }

    public void setRouteType(String routeType) {
        RouteType = routeType;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }
}
