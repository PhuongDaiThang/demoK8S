package com.demok8s.di;

public class Cooking implements IJob{
    @Override
    public void doJob() {
        System.out.println("Cooking");
    }
}
