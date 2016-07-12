package by.training.philosopers.domain;

import by.training.philosopers.util.Philosopher;

/**
 * Created by vladislav on 12.07.16.
 */
public class Request {
    private RequestType requestType;
    private Philosopher philosopher;


    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Philosopher getPhilosopher() {
        return philosopher;
    }

    public void setPhilosopher(Philosopher philosopher) {
        this.philosopher = philosopher;
    }

    public Request() {

    }

    public Request(RequestType requestType, Philosopher philosopher) {

        this.requestType = requestType;
        this.philosopher = philosopher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (requestType != request.requestType) return false;
        return philosopher != null ? philosopher.equals(request.philosopher) : request.philosopher == null;

    }

    @Override
    public int hashCode() {
        int result = requestType != null ? requestType.hashCode() : 0;
        result = 31 * result + (philosopher != null ? philosopher.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestType=" + requestType +
                ", philosopher=" + philosopher +
                '}';
    }
}
