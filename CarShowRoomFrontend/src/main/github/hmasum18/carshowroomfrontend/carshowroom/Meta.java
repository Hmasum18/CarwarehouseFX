/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.carshowroom;

public class Meta {
    private final Status status; //only for server
    private final RequestType requestType;
    private final ContentType contentType;
    private final long contentLength;
    private final long timeStamp;

    //request type by the client
    public enum RequestType {
        CONNECT,
        GET,
        POST,
        CREATE,
        DELETE,
        UPDATE,
        NONE; //for server
    }

    //requested or response content type that sent through network
    public enum ContentType {
        LOGIN_INFO,
        USER_INFO,
        CAR,
        CAR_LIST,
        IMAGE,
        FILE,
        NONE; //for server
    }

    //this for server only
    //server will send the status of the request made by the client
    public enum Status {
        OK,
        CREATED,
        ACCEPTED,
        NOT_FOUND,
        CONFLICT, //already exist
        NONE,//for client
        LOCAL;
    }

    //for client
    public Meta(RequestType requestType, ContentType contentType, Long timeStamp) {
        this.requestType = requestType;
        this.status = Status.NONE;
        this.contentType = contentType;
        this.contentLength = 0L;
        this.timeStamp = timeStamp;
    }

    //for client
    public Meta(RequestType requestType, ContentType contentType, long contentLength, long timeStamp) {
        this.requestType = requestType;
        this.status = Status.NONE;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.timeStamp = timeStamp;
    }


    //for server
    public Meta(Status status, ContentType contentType, long contentLength, long timeStamp) {
        this.requestType = RequestType.NONE;
        this.status = status;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.timeStamp = timeStamp;
    }

    public Meta(Status status, RequestType requestType, ContentType contentType, long contentLength, long timeStamp) {
        this.status = status;
        this.requestType = requestType;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.timeStamp = timeStamp;
    }



    public RequestType getRequestType() {
        return requestType;
    }

    public Status getStatus() {
        return status;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "requestType=" + requestType +
                ", status=" + status +
                ", contentType=" + contentType +
                ", contentLength=" + contentLength +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
