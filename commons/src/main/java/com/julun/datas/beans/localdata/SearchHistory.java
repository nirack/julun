package com.julun.datas.beans.localdata;

import java.util.Date;

import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * 本地搜索历史.
 */
@Table("search_history")
public class SearchHistory {

    @Key
    @Column("keyword")
    private String keyword;
    @Column("record_time")
    private Date recordTime;

    public Date getRecordTime() {
        return recordTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

}
