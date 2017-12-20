package com.jtool.codegenbuilderplugin.util;

import com.jtool.annotation.AvailableValues;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class AnnotationHelper {

    public static boolean hasAnnotation(AnnotatedElement annotatedElement, Class annotationClass) {
        return annotatedElement.getAnnotation(annotationClass) != null;
    }

    public static List<String> constrainAnnotationToStr(Annotation[] annotations) {

        List<String> result = new ArrayList<>();

        for(Annotation annotation : annotations) {
            if (annotation instanceof AssertTrue) {
                result.add("必须为True");
            } else if (annotation instanceof AssertFalse) {
                result.add("必须为False");
            } else if(annotation instanceof NotEmpty) {
                result.add("必须有值，长度大于0");
            } else if(annotation instanceof NotBlank) {
                result.add("必须有值，不能是空白字符，长度大于0");
            } else if (annotation instanceof Digits) {
                Digits digits = (Digits) annotation;
                if (digits.fraction() == 0) {
                    result.add("必须为一个整型数字");
                } else if (digits.fraction() > 0) {
                    result.add("必须为一个" + digits.fraction() + "位小数");
                } else {
                    throw new RuntimeException("Digits的fraction必须为一个非负数");
                }
            } else if (annotation instanceof Min) {
                result.add("必须大于或等于" + ((Min) annotation).value());
            } else if (annotation instanceof DecimalMin) {
                result.add("必须大于或等于" + ((DecimalMin) annotation).value());
            } else if (annotation instanceof Max) {
                result.add("必须小于或等于" + ((Max) annotation).value());
            } else if (annotation instanceof DecimalMax) {
                result.add("必须小于或等于" + ((DecimalMax) annotation).value());
            } else if (annotation instanceof Size) {
                Size size = (Size) annotation;
                result.add("长度边界[" + size.min() + " : " + size.max() + "]");
            } else if (annotation instanceof Past) {
                result.add("必须为过去的某个时间");
            } else if (annotation instanceof Future) {
                result.add("必须为将来的某个时间");
            } else if (annotation instanceof Pattern) {
                Pattern pattern = (Pattern) annotation;
                result.add("必须匹配正则表达式:" + pattern.regexp());
            }
            //自定义限制注解
            else if(annotation instanceof AvailableValues) {
                AvailableValues availableValues = (AvailableValues)annotation;
                String[] values = availableValues.values();
                result.add("必须为以下可用值之一: [ \"" + StringUtils.join(values, "\", \"") + "\" ]");
            }
        }

        return result;
    }

}
