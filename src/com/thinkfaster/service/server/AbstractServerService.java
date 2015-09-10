package com.thinkfaster.service.server;

import com.thinkfaster.util.ContextConstants;

import static com.thinkfaster.util.ContextConstants.HOST;
import static com.thinkfaster.util.ContextConstants.PORT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by brekol on 12.05.15.
 */
public abstract class AbstractServerService {

    private static final String LINK_PATTERN = "%s:%s/%s";

    protected String getLink(String link) {
        return String.format(LINK_PATTERN, HOST, PORT, link);
    }

}
