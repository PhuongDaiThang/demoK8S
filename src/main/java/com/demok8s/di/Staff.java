package com.demok8s.di;

public class Staff implements IJobSetter {
    IJob iJob;

    @Override
    public void inject(IJob job) {
        this.iJob = job;
    }

    public void work() {
        this.iJob.doJob();
    }
}
