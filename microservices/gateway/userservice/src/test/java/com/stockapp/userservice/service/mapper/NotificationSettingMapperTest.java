package com.stockapp.userservice.service.mapper;

import static com.stockapp.userservice.domain.NotificationSettingAsserts.*;
import static com.stockapp.userservice.domain.NotificationSettingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationSettingMapperTest {

    private NotificationSettingMapper notificationSettingMapper;

    @BeforeEach
    void setUp() {
        notificationSettingMapper = new NotificationSettingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificationSettingSample1();
        var actual = notificationSettingMapper.toEntity(notificationSettingMapper.toDto(expected));
        assertNotificationSettingAllPropertiesEquals(expected, actual);
    }
}
