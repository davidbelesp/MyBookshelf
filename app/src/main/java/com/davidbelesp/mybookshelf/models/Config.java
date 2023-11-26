package com.davidbelesp.mybookshelf.models;

public class Config {

    boolean nsfw;
    boolean censor;


    public Config(ConfigBuilder builder){
        this.nsfw = builder.nsfw;
        this.censor = builder.censor;
    }

    public static class ConfigBuilder {

        private boolean nsfw;
        private boolean censor;

        public ConfigBuilder(){

        }

        public ConfigBuilder setNSFW(boolean nsfw){
            this.nsfw = nsfw;
            return this;
        }

        public ConfigBuilder setCensor(boolean censor){
            this.censor = censor;
            return this;
        }

        public Config build(){
            return new Config(this);
        }

    }

    public boolean getNSFW(){
        return this.nsfw;
    }
    public boolean getCensor(){
        return this.censor;
    }

    @Override
    public String toString() {
        return "Config{" +
                "nsfw=" + nsfw +
                '}';
    }
}
