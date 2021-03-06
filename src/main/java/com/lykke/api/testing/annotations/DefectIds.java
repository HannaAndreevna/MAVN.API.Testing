package com.lykke.api.testing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefectIds {

    /**
     * The value contains the userStory id test is developed for
     */
    int[] defectIds() default 0;

    int[] value() default 0;
}
