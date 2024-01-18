package kauproject.kaunotifier.validation;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kauproject.kaunotifier.controller.MemberForm;
import kauproject.kaunotifier.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class MemberValidationTest {
    @Test
    void memberValidation() {
        System.out.println("hi");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        MemberForm member = new MemberForm();
        member.setName(" ");
        member.setEmail("test@123.com");

        Set<ConstraintViolation<MemberForm>> violations = validator.validate(member);
        Assertions.assertThat(violations.size()).isEqualTo(1);
    }
}
