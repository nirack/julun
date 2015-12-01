package com.julun.utils.sp;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.julun.utils.ApplicationUtils;
import com.julun.utils.StringHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by danjp on 2015/12/1.
 */
public class SettingProperties implements SharedPreferences {
    private static String PATH = "/data/data/" + ApplicationUtils.getPackageName() + "/properties";
    private static String FILE_NAME = "data.properties";
    private static final String SPLITER = "<SP>";
    private File file = null;
    private Properties prop = null;

    private static SettingProperties sharedPreferencesUtils = null;
    private SEditor editor = null;

    private List<OnSharedPreferenceChangeListener> listeners = new ArrayList<OnSharedPreferenceChangeListener>();

    private SettingProperties() {
        File dir = new File(PATH);
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
        }
        file = new File(PATH, FILE_NAME);
    }

    public static SettingProperties getInstance() {
        if(sharedPreferencesUtils == null) {
            sharedPreferencesUtils = new SettingProperties();
        }
        return sharedPreferencesUtils;
    }

    public synchronized Properties getProperties() {
        if(prop != null){
            return prop;
        }
        prop = new Properties();
        if(!file.exists()){
            return prop;
        }

        InputStream is = null;
        try {
            is = new FileInputStream(file);
            prop.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return prop;
    }

    @Override
    public Map<String, String> getAll() {
        Map<String, String> map = new HashMap<String, String>();
        for(Object obj : getProperties().keySet()){
            String key = (String) obj;
            if(StringHelper.isEmpty(key)){
                continue;
            }
            String value = getProperties().getProperty(key);
            if(StringHelper.isEmpty(value)){
                continue;
            }
            map.put(key, value);
        }
        return null;
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        if(getProperties().containsKey(key)){
            String value = getProperties().getProperty(key);
            if(StringHelper.isEmpty(value)){
                return defValue;
            }
            return value;
        }
        return defValue;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        if(getProperties().containsKey(key)){
            String[] values = getProperties().getProperty(key).split(SPLITER);
            Set<String> set = new LinkedHashSet<String>();
            for(String value : values){
                set.add(value);
            }
            return set;
        }
        return defValues;
    }

    @Override
    public int getInt(String key, int defValue) {
        if(getProperties().containsKey(key)){
            return Integer.parseInt(getProperties().getProperty(key));
        }
        return defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        if(getProperties().containsKey(key)){
            return Long.parseLong(getProperties().getProperty(key));
        }
        return defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        if(getProperties().containsKey(key)){
            return Float.parseFloat(getProperties().getProperty(key));
        }
        return defValue;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        if(getProperties().containsKey(key)){
            return Boolean.parseBoolean( getProperties().getProperty(key) );
        }
        return defValue;
    }

    @Override
    public boolean contains(String key) {
        return getProperties().containsKey(key);
    }

    @Override
    public Editor edit() {
        if(editor == null){
            editor = new SEditor();
        }
        return editor;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        if(listener != null)
            listeners.add(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        for(OnSharedPreferenceChangeListener ol : listeners) {
            if(ol == listener){
                listeners.remove(listener);
                break;
            }
        }
    }

    class SEditor implements Editor {

        private Map<String, String> changeValues = new HashMap<String, String>();
        private List<String> removeValues = new ArrayList<String>();

        @Override
        public Editor putString(String key, String value) {
            synchronized (changeValues){
                changeValues.put(key, value);
            }
            return this;
        }

        @Override
        public Editor putStringSet(String key, Set<String> values) {
            synchronized (changeValues){
                if(values != null && values.size() > 0){
                    changeValues.put(key, getSetString(values));
                }
            }
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            synchronized (changeValues){
                changeValues.put(key, String.valueOf(value));
            }
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            synchronized (this) {
                changeValues.put(key, String.valueOf(value));
            }
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            synchronized (this) {
                changeValues.put(key, String.valueOf(value));
            }
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            synchronized (this) {
                changeValues.put(key, String.valueOf(value));
            }
            return this;
        }

        @Override
        public Editor remove(String key) {
            synchronized (removeValues) {
                removeValues.add(key);
            }
            return this;
        }

        @Override
        public Editor clear() {
            synchronized (changeValues) {
                changeValues.clear();
            }
            synchronized (removeValues) {
                removeValues.clear();
            }
            return this;
        }

        @Override
        public boolean commit() {
            return store();
        }

        @Override
        public void apply() {
            store();
        }

        private synchronized boolean store() {
            boolean flag = true;
            if(changeValues.size() == 0 && removeValues.size() == 0){
                return flag;
            }
            synchronized (changeValues) {
                for(String key : changeValues.keySet()){
                    put(key, changeValues.get(key));
                }
                changeValues.clear();
            }

            synchronized (removeValues) {
                for(String key : removeValues){
                    removeKey(key);
                }
                removeValues.clear();
            }

            OutputStream output = null;
            try {
                output = new FileOutputStream(file);
                getProperties().store(output, "save properties");
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            } finally {
                try {
                    if(output != null){
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return flag;
        }
    }

    private void put(String key, String value) {
        getProperties().setProperty(key, value);
        dispatchChangeEvent(key);
    }

    private void removeKey(String key) {
        getProperties().remove(key);
        dispatchChangeEvent(key);
    }

    private void dispatchChangeEvent(String key) {
        for (OnSharedPreferenceChangeListener ol : listeners) {
            ol.onSharedPreferenceChanged(this, key);
        }
    }

    private String getSetString(Set<String> values) {
        StringBuffer sb = new StringBuffer();
        Iterator<String> iterator = values.iterator();
        while(iterator.hasNext()){
            sb.append(iterator.next());
            if(iterator.hasNext()){
                sb.append(SPLITER);
            }
        }
        return sb.toString();
    }

}
