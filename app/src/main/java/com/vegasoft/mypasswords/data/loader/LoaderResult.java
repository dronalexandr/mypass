package com.vegasoft.mypasswords.data.loader;

public class LoaderResult<T> {
    public final T data;
    public final Exception error;

    public LoaderResult(T data, Exception error) {
        this.data = data;
        this.error = error;
    }

    public LoaderResult(T data) {
        this(data, null);
    }

    public LoaderResult(Exception error) {
        this(null, error);
    }

}
