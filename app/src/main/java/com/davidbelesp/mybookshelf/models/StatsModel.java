package com.davidbelesp.mybookshelf.models;

import com.davidbelesp.mybookshelf.utils.Constants;
import com.davidbelesp.mybookshelf.utils.IncrementableNumber;

import java.util.List;

public class StatsModel {

    private Integer entries;
    private Integer denominatorAverage;
    private IncrementableNumber acumulativeAverage;
    private IncrementableNumber volumesNumber;
    private IncrementableNumber chaptersNumber;

    //types stats
    private IncrementableNumber mangaNumber;
    private IncrementableNumber manhwaNumber;
    private IncrementableNumber manhuaNumber;
    private IncrementableNumber novelNumber;
    private IncrementableNumber lightNovelNumber;

    //status stats
    private IncrementableNumber plantoreadNumber;
    private IncrementableNumber readingNumber;
    private IncrementableNumber onholdNumber;
    private IncrementableNumber completedNumber;
    private IncrementableNumber droppedNumber;

    public Double getAverage(){
        return Math.floor(acumulativeAverage.getNumber()/Double.valueOf(denominatorAverage)*100) /100;
    }

    public Integer getPagesAproximation(){
        return (int) Math.floor(this.chaptersNumber.getNumber() * Constants.pagesCnt);
    }

    public void sumAverageTotal(Integer sum){
        if(this.acumulativeAverage == null) this.acumulativeAverage = new IncrementableNumber();
        this.acumulativeAverage.incrementNumber(sum);
    }

    public void sumPlantoreadNumber(Integer sum){
        if(this.plantoreadNumber == null) this.plantoreadNumber = new IncrementableNumber();
        this.plantoreadNumber.incrementNumber(sum);
    }

    public void sumCompletedNumber(Integer sum){
        if(this.completedNumber == null) this.completedNumber = new IncrementableNumber();
        this.completedNumber.incrementNumber(sum);
    }

    public void sumReadingNumber(Integer sum){
        if(this.readingNumber == null) this.readingNumber = new IncrementableNumber();
        this.readingNumber.incrementNumber(sum);
    }

    public void sumOnholdNumber(Integer sum){
        if(this.onholdNumber == null) this.onholdNumber = new IncrementableNumber();
        this.onholdNumber.incrementNumber(sum);
    }

    public void sumDroppedNumber(Integer sum){
        if(this.droppedNumber == null) this.droppedNumber = new IncrementableNumber();
        this.droppedNumber.incrementNumber(sum);
    }

    public void sumMangaNumber(Integer sum){
        if(this.mangaNumber == null) this.mangaNumber = new IncrementableNumber();
        this.mangaNumber.incrementNumber(sum);
    }

    public void sumManhwaNumber(Integer sum){
        if(this.manhwaNumber == null) this.manhwaNumber = new IncrementableNumber();
        this.manhwaNumber.incrementNumber(sum);
    }

    public void sumManhuaNumber(Integer sum){
        if(this.manhuaNumber == null) this.manhuaNumber = new IncrementableNumber();
        this.manhuaNumber.incrementNumber(sum);
    }

    public void sumNovelNumber(Integer sum){
        if(this.novelNumber == null) this.novelNumber = new IncrementableNumber();
        this.novelNumber.incrementNumber(sum);
    }

    public void sumLightNovelNumber(Integer sum){
        if(this.lightNovelNumber == null) this.lightNovelNumber = new IncrementableNumber();
        this.lightNovelNumber.incrementNumber(sum);
    }

    public Integer getMangaNumber() {
        if(this.mangaNumber == null) this.mangaNumber = new IncrementableNumber();
        return mangaNumber.getNumber();
    }

    public Integer getManhwaNumber() {
        if(this.manhwaNumber == null) this.manhwaNumber = new IncrementableNumber();
        return manhwaNumber.getNumber();
    }

    public Integer getManhuaNumber() {
        if(this.manhuaNumber == null) this.manhuaNumber = new IncrementableNumber();
        return manhuaNumber.getNumber();
    }

    public Integer getNovelNumber() {
        if(this.novelNumber == null) this.novelNumber = new IncrementableNumber();
        return novelNumber.getNumber();
    }

    public Integer getCompletedNumber() {
        if(this.completedNumber == null) this.completedNumber = new IncrementableNumber();
        return completedNumber.getNumber();
    }

    public Integer getReadingNumber() {
        if(this.readingNumber == null) this.readingNumber = new IncrementableNumber();
        return readingNumber.getNumber();
    }

    public Integer getOnholdNumber() {
        if(this.onholdNumber == null) this.onholdNumber = new IncrementableNumber();
        return onholdNumber.getNumber();
    }

    public Integer getDroppedNumber() {
        if(this.droppedNumber == null) this.droppedNumber = new IncrementableNumber();
        return droppedNumber.getNumber();
    }

    public Integer getPlantoreadNumber() {
        if(this.plantoreadNumber == null) this.plantoreadNumber = new IncrementableNumber();
        return plantoreadNumber.getNumber();
    }

    public Integer getLightNovelNumber() {
        if(this.lightNovelNumber == null) this.lightNovelNumber = new IncrementableNumber();
        return lightNovelNumber.getNumber();
    }

    public Integer getVolumesNumber() {
        if(this.volumesNumber == null) this.volumesNumber = new IncrementableNumber();
        return volumesNumber.getNumber();
    }

    public void sumVolumesNumber(Integer volumesNumber) {
        if(this.volumesNumber == null) this.volumesNumber = new IncrementableNumber();
        this.volumesNumber.incrementNumber(volumesNumber);
    }

    public Integer getChaptersNumber() {
        return chaptersNumber.getNumber();
    }

    public void sumChaptersNumber(Integer chaptersNumber) {
        if(this.chaptersNumber == null) this.chaptersNumber = new IncrementableNumber();
        this.chaptersNumber.incrementNumber(chaptersNumber);
    }

    public void setEntries(int size) {
        this.entries = size;
    }
    public void setDenominatorAverage(int size) {
        this.denominatorAverage = size;
    }

    public Integer getEntries() {
        return this.entries;
    }
}
