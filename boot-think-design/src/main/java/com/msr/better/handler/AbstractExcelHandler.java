package com.msr.better.handler;

/**
 * Excel处理的抽象基类
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021/12/28
 */
public abstract class AbstractExcelHandler {

    /**
     * 是否定时执行
     * true：定时执行
     * false：不定时执行
     *
     * @return
     */
    public boolean isTimingExecute() {
        return false;
    }

}
