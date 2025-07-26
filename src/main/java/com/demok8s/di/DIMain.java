package com.demok8s.di;

public class DIMain {
    public static void main(String[] args) {
        IJob currJob = new Serving();
        IJob fuJob = new Cooking();
        Staff staff = new Staff();
        staff.inject(currJob);
        staff.work();
        staff.inject(fuJob);
        staff.work();
    }
}
