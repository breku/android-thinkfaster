package com.thinkfaster.service.server;

import org.junit.Test;

import static com.thinkfaster.util.ContextConstants.HOST;
import static com.thinkfaster.util.ContextConstants.PORT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.fest.assertions.Assertions.assertThat;

public class AbstractServerServiceTest {

    private AbstractServerService uut = new AbstractServerService() {
        @Override
        protected String getLink(String link) {
            return super.getLink(link);
        }
    };

    @Test
    public void shouldReturnCorrectUrl() {
        // given

        // when
        String result = uut.getLink("get/aaa");

        // then
        assertThat(result).isEqualTo(HOST + ":" + PORT + "/get/aaa");
    }


    @Test
    public void shouldNotCrashForEmptyUrl() {
        // given

        // when
        String result = uut.getLink(EMPTY);

        // then
        assertThat(result).isEqualTo(HOST + ":" + PORT + "/");
    }

}