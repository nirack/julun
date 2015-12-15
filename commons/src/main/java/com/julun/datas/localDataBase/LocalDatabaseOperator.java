package com.julun.datas.localDataBase;

import android.app.Application;

import se.emilsjolander.sprinkles.Sprinkles;

/**
 */
public class LocalDatabaseOperator {
    private static Sprinkles sprinkles;
    /**
     * 是否已经初始化过.
     */
    private static boolean inited  = false;

    // TODO: 2015-12-03  存放DB还是 ShardPreference ?????????????

    /**
     * 初始化数据库工具.
     * @param application
     * @param databaseName
     * @param initialDatabaseVersion
     */
    public static void init(Application application, String databaseName, int initialDatabaseVersion) {
        if(!inited){
            sprinkles = Sprinkles.init(application, databaseName, initialDatabaseVersion);
            inited = true;
        }



    }

}
