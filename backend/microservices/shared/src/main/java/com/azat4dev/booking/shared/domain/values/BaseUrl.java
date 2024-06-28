package com.azat4dev.booking.shared.domain.values;

import com.azat4dev.booking.shared.domain.DomainException;
import lombok.EqualsAndHashCode;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

@EqualsAndHashCode
public final class BaseUrl {

    private final URL value;

    private BaseUrl(URL value) {
        this.value = value;
    }

    public static BaseUrl checkAndMakeFrom(URL value) throws Exception.WrongFormatException {
        if (!value.toString().endsWith("/")) {
            try {
                value = URI.create(value + "/").toURL();
            } catch (MalformedURLException e) {
                throw new Exception.WrongFormatException();
            }
        }

        return new BaseUrl(value);
    }

    public static BaseUrl checkAndMakeFrom(String value) throws Exception.WrongFormatException {
        if (!value.endsWith("/")) {
            value += "/";
        }

        try {
            final var url = URI.create(value).toURL();
            return new BaseUrl(url);
        } catch (MalformedURLException e) {
            throw new Exception.WrongFormatException();
        }
    }

    public static BaseUrl makeWithoutChecksFrom(URL value) {
        return new BaseUrl(value);
    }

    public URL value() {
        return value;
    }

    public String toString() {
        return value.toString();
    }

    public URL urlWithPath(String path) throws MalformedURLException {
        return URI.create(value.toString() + path).toURL();
    }

    public URL stringWithPath(String path) throws MalformedURLException {
        return urlWithPath(path);
    }

    // Exceptions

    public abstract static class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static final class WrongFormatException extends Exception {
            public WrongFormatException() {
                super("Wrong format of base URL");
            }
        }
    }
}
