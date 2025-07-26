package com.demok8s.di;

public class Serving implements IJob{
    @Override
    public void doJob() {
        System.out.println("Serving");
    }
}
