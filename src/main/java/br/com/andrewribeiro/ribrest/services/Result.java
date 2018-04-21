package br.com.andrewribeiro.ribrest.services;

import br.com.andrewribeiro.ribrest.services.holder.IHolder;
import javax.ws.rs.core.Response;

/**
 *
 * @author Andrew Ribeiro
 */
public class Result {

    public Result() {
    }

    public Result(Response.Status status, String cause, IHolder holder) {
        this.status = status;
        this.cause = cause;
        this.holder = holder;
    }

    public Result(Response.Status status, String cause) {
        this.status = status;
        this.cause = cause;
    }

    private Response.Status status;

    private String cause;

    private IHolder holder;

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public IHolder getHolder() {
        return holder;
    }

    public void setHolder(IHolder holder) {
        this.holder = holder;
    }

}
