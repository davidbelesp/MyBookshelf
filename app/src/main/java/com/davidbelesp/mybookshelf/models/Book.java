package com.davidbelesp.mybookshelf.models;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Random;

public class Book implements Serializable {

    private Long ID;
    private String title;
    private BookType type;
    private String description;
    private Integer score;
    private Integer chapters;
    private Integer volumes;
    private String image;
    private BookStatus status;
    private Boolean nsfw;

    public Book(BookBuilder builder){
        this.ID = builder.ID;
        this.title = builder.title;
        this.type = builder.type;
        this.description = builder.description;
        this.score = builder.score;
        this.chapters = builder.chapters;
        this.volumes = builder.volumes;
        this.image = builder.image;
        this.status = builder.status;
        this.nsfw = builder.nsfw;
    }

    public Long getID(){return ID;}

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type.name();
    }

    public BookType getTypeObject() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getChapters() {
        return chapters;
    }

    public Integer getVolumes() {
        return volumes;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status.name();
    }

    public BookStatus getStatusObject() {
        return status;
    }

    public boolean getNsfw(){
        return nsfw.booleanValue();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(BookType type) {
        this.type = type;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public void setNsfw(Boolean nsfw) {this.nsfw = nsfw;}

    public static class BookBuilder{

        private Long ID;
        private String title;
        private BookType type;
        private String description;
        private Integer score;
        private Integer chapters;
        private Integer volumes;
        private String image;
        private BookStatus status;
        private Boolean nsfw;

        public BookBuilder() {

        }

        public BookBuilder id(Long ID) {
            this.ID = ID;
            return this;
        }

        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder type(BookType type) {
            this.type = type;
            return this;
        }

        public BookBuilder description(String description) {
            this.description = description;
            return this;
        }

        public BookBuilder volumes(Integer volumes) {
            this.volumes = volumes;
            return this;
        }

        public BookBuilder chapters(Integer chapters) {
            this.chapters = chapters;
            return this;
        }

        public BookBuilder image(String image) {
            this.image = image;
            return this;
        }

        public BookBuilder status(BookStatus status) {
            this.status = status;
            return this;
        }

        public BookBuilder nsfw(Boolean nsfw) {
            this.nsfw = nsfw;
            return this;
        }

        public BookBuilder score(Integer score) {
            this.score = score;
            return this;
        }

        public Book build(){
            return new Book(this);
        }

    }

    @Override
    public String toString() {
        return "Book{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", score=" + score +
                ", chapters=" + chapters +
                ", volumes=" + volumes +
                ", image='" + image + '\'' +
                ", status=" + status +
                ", nsfw=" + nsfw +
                '}';
    }
}
