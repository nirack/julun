package com.julun.business.beans;

import java.util.ArrayList;
import java.util.List;

public class County {
    private int countyId;
    private int pid;
    private String countyName;
    private String pinyin;
    private String pinyinShort;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinShort() {
        return pinyinShort;
    }

    public void setPinyinShort(String pinyinShort) {
        this.pinyinShort = pinyinShort;
    }

    private List<County> chindren = new ArrayList<>();

    public County(int pid, int countyId, String countyName) {
        super();
        this.pid = pid;
        this.countyId = countyId;
        setCountyName(countyName);
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
        if (countyName == null || countyName.trim().equals("")) {
            return;
        }
        String pinyin = "";
        String pinyinShort = "";
        /*
		for (int index = 0; index < countyName.length(); index++) {
			char ch = countyName.charAt(index);
			String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(ch);
			pinyin += pinyins[0];
			pinyinShort += pinyins[0].charAt(0);
		}
		*/
        setPinyin(pinyin);
        setPinyinShort(pinyinShort);

    }

    public List<County> getChindren() {
        return chindren;
    }

    public void setChindren(List<County> chindren) {
        this.chindren = chindren;
    }

}
