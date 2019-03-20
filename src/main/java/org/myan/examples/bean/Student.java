package org.myan.examples.bean;

/**
 * Created by myan on 2019/3/11.
 */
public class Student {
    private String cnName;
    private int age;
    private String className;
    private double totalScore;
    private double avgScore;

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public boolean isNotPassed(){
        return this.avgScore < 60d;
    }
}
