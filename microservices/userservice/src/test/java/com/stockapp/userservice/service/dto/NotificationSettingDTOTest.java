package com.stockapp.userservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.stockapp.userservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationSettingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationSettingDTO.class);
        NotificationSettingDTO notificationSettingDTO1 = new NotificationSettingDTO();
        notificationSettingDTO1.setId("id1");
        NotificationSettingDTO notificationSettingDTO2 = new NotificationSettingDTO();
        assertThat(notificationSettingDTO1).isNotEqualTo(notificationSettingDTO2);
        notificationSettingDTO2.setId(notificationSettingDTO1.getId());
        assertThat(notificationSettingDTO1).isEqualTo(notificationSettingDTO2);
        notificationSettingDTO2.setId("id2");
        assertThat(notificationSettingDTO1).isNotEqualTo(notificationSettingDTO2);
        notificationSettingDTO1.setId(null);
        assertThat(notificationSettingDTO1).isNotEqualTo(notificationSettingDTO2);
    }
}
