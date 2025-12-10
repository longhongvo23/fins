package com.stockapp.userservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AppUser getAppUserSample1() {
        return new AppUser()
            .id("id1")
            .login("login1")
            .password("password1")
            .email("email1")
            .failedLoginAttempts(1)
            .passwordResetToken("passwordResetToken1")
            .emailVerificationToken("emailVerificationToken1")
            .twoFactorSecret("twoFactorSecret1")
            .lastLoginIp("lastLoginIp1")
            .language("language1")
            .timezone("timezone1");
    }

    public static AppUser getAppUserSample2() {
        return new AppUser()
            .id("id2")
            .login("login2")
            .password("password2")
            .email("email2")
            .failedLoginAttempts(2)
            .passwordResetToken("passwordResetToken2")
            .emailVerificationToken("emailVerificationToken2")
            .twoFactorSecret("twoFactorSecret2")
            .lastLoginIp("lastLoginIp2")
            .language("language2")
            .timezone("timezone2");
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser()
            .id(UUID.randomUUID().toString())
            .login(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .failedLoginAttempts(intCount.incrementAndGet())
            .passwordResetToken(UUID.randomUUID().toString())
            .emailVerificationToken(UUID.randomUUID().toString())
            .twoFactorSecret(UUID.randomUUID().toString())
            .lastLoginIp(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString())
            .timezone(UUID.randomUUID().toString());
    }
}
