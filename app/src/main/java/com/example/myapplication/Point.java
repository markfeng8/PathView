package com.example.myapplication;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

//注解表名
@Table(name = "Tb_Point")
public class Point {
    @Column(name = "ID")
    private String ID;
    @Column(name = "PointName")
    private String PointName;
    @Column(name = "Floor")
    private String Floor;
    @Column(name = "PointType")
    private String PointType;
    @Column(name = "Index_S")
    private int Index_S;
    @Column(name = "RouteText")
    private String RouteText;
    @Column(name = "AreaType")
    private String AreaType;
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

    public String getPointName() {
        return PointName;
    }

    public void setPointName(String pointName) {
        PointName = pointName;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getPointType() {
        return PointType;
    }

    public void setPointType(String pointType) {
        PointType = pointType;
    }

    public int getIndex_S() {
        return Index_S;
    }

    public void setIndex_S(int index_S) {
        Index_S = index_S;
    }

    public String getRouteText() {
        return RouteText;
    }

    public void setRouteText(String routeText) {
        RouteText = routeText;
    }

    public String getAreaType() {
        return AreaType;
    }

    public void setAreaType(String areaType) {
        AreaType = areaType;
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
