open module edu.project.polytechproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires spring.web;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires java.sql;
    requires org.apache.tomcat.embed.core;
    requires spring.jdbc;
    requires static lombok;
    requires spring.tx;
    requires spring.webflux;
    requires reactor.core;
    requires reactor.netty.http;
    requires java.base;
    requires spring.aop;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;
    requires org.apache.logging.log4j;
    requires org.json;
    requires jakarta.validation;
    requires jpro.webapi;
    requires javafx.base;
    requires com.udojava.evalex;

    exports edu.project;
}