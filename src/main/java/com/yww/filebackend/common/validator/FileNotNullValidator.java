package com.yww.filebackend.common.validator;

import com.yww.filebackend.annotation.FileNotNull;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <p>
 *      FileNotNull注解的校验逻辑
 * </p>
 *
 * @author chenhao
 */
public class FileNotNullValidator implements ConstraintValidator<FileNotNull, MultipartFile> {
    @Override
    public void initialize(FileNotNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return !file.isEmpty() && file.getSize() >= 0;
    }

}
