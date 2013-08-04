package com.android.demo.aidl;

interface IRemoteService{
    void start();
    void stop();
    void pause();
    long getResult();
}