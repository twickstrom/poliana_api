package com.poliana.core.legislation.entities.bills;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;

import java.util.List;

/**
 * @author David Gilmore
 * @date 11/22/13
 */
@Embedded
public class Action {
    @Property("acted_ts")
    private int actedTs;
    @Property("acted_at")
    private int actedAt;
    @Embedded("bill_ids")
    private List<String> billIds;
    @Embedded
    private List<Reference> references;
    private String calendar;
    private String text;
    private String type;
    private String roll;
    private String under;
    private int number;
    private String how;
    private String result;
    private String status;
    private String suspension;
    private String where;
    @Property("vote_type")
    private String voteType;

    public int getActedTs() {
        return actedTs;
    }

    public void setActedTs(int actedTs) {
        this.actedTs = actedTs;
    }

    public int getActedAt() {
        return actedAt;
    }

    public void setActedAt(int actedAt) {
        this.actedAt = actedAt;
    }

    public List<String> getBillIds() {
        return billIds;
    }

    public void setBillIds(List<String> billIds) {
        this.billIds = billIds;
    }

    public List<Reference> getReferences() {
        return references;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getUnder() {
        return under;
    }

    public void setUnder(String under) {
        this.under = under;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuspension() {
        return suspension;
    }

    public void setSuspension(String suspension) {
        this.suspension = suspension;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }
}
