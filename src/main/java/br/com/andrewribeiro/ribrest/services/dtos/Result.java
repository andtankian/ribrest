package br.com.andrewribeiro.ribrest.services.dtos;

import javax.ws.rs.core.Response;
import br.com.andrewribeiro.ribrest.services.holder.Holder;

/**
 *
 * @author Andrew Ribeiro
 */
public class Result {

    public Result() {
        this.status = Response.Status.OK;
    }

    public Result(Response.Status status, String cause, Holder holder) {
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

    private Holder holder;

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

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

}
