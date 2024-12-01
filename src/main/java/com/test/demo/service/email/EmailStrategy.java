package com.test.demo.service.email;

import org.thymeleaf.context.Context;


public interface EmailStrategy {
    String getSubject();
    String getTemplateLocation();
    void prepareContext(Context context);
}
