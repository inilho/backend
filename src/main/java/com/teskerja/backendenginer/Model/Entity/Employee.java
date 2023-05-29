package com.teskerja.backendenginer.Model.Entity;

public class Employee {

    private String name;
    private String sex;
    private String maritalStatus;
    private int childs;
    private String country;

    public Employee(String name, String sex, String maritalStatus, int childs, String country) {
        this.name = name;
        this.sex = sex;
        this.maritalStatus = maritalStatus;
        this.childs = childs;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public int getChilds() {
        return childs;
    }

    public void setChilds(int childs) {
        this.childs = childs;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
