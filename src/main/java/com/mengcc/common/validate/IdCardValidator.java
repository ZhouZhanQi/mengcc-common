package com.mengcc.common.validate;

import com.mengcc.common.annotation.IdCard;
import com.mengcc.common.utils.IdCardUtil;
import com.mengcc.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zhouzq
 * @date 2019/10/23
 * @desc 身份证号码校验规则
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {
    @Override
    public void initialize(IdCard constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        return IdCardUtil.isValidatedAllIdcard(value);
    }
}
