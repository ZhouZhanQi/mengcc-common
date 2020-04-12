package com.mengcc.common.validate;


import com.mengcc.common.annotation.MobilePhone;
import com.mengcc.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhouzq
 * @date 2019/10/23
 * @desc 手机号码校验规则
 */
public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, String> {

    private static final String MOBILE_PHONE_REGEX = "^0?(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$";

    @Override
    public void initialize(MobilePhone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        Pattern pattern = Pattern.compile(MOBILE_PHONE_REGEX);
        final Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }
}
