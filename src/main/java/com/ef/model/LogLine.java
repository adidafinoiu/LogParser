package com.ef.model;

import java.time.LocalDateTime;

public class LogLine {

    private Integer id;
    private LocalDateTime dateTime;
    private String ip;
    private String request;
    private Integer status;
    private String userAgent;

    private LogLine(Builder builder){
        this.id = builder.id;
        this.dateTime = builder.dateTime;
        this.ip = builder.ip;
        this.request = builder.request;
        this.status = builder.status;
        this.userAgent = builder.userAgent;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getIp() {
        return ip;
    }

    public String getRequest() {
        return request;
    }

    public Integer getStatus() {
        return status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public String toString() {
        return "LogLine{" +
                "Id=" + id +
                ", dateTime=" + dateTime +
                ", ip='" + ip + '\'' +
                ", request='" + request + '\'' +
                ", status=" + status +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }

    public static class Builder {
        private Integer id;
        private LocalDateTime dateTime;
        private String ip;
        private String request;
        private Integer status;
        private String userAgent;

        public Builder withId(Integer id){
            this.id = id;
            return this;
        }

        public Builder withDateTime(LocalDateTime dateTime){
            this.dateTime = dateTime;
            return this;
        }

        public Builder withIp(String ip){
            this.ip = ip;
            return this;
        }

        public Builder withRequest(String request){
            this.request = request;
            return this;
        }

        public Builder withStatus(Integer status){
            this.status = status;
            return this;
        }

        public Builder withUserAgent(String userAgent){
            this.userAgent = userAgent;
            return this;
        }

        public LogLine build(){
            return new LogLine(this);
        }
    }
}
