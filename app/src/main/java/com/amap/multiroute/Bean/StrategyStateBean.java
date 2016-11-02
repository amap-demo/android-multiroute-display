package com.amap.multiroute.Bean;

/**
 * Created by ligen on 16/10/31.
 */
public class StrategyStateBean {
    private int mStrategyCode;
    private boolean mOpen;

    public StrategyStateBean(int strategyCode, boolean open) {
        setStrategyCode(strategyCode);
        setOpen(open);
    }

    public int getStrategyCode() {
        return mStrategyCode;
    }

    public void setStrategyCode(int mStrategyCode) {
        this.mStrategyCode = mStrategyCode;
    }

    public boolean isOpen() {
        return mOpen;
    }

    public void setOpen(boolean mOpen) {
        this.mOpen = mOpen;
    }
}
