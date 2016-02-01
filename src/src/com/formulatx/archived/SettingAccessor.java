package com.formulatx.archived;

import com.formulatx.archived.database.helper.SettingsHelper;

public final class SettingAccessor {
    private SettingsHelper sh;
    private long Se;

    private SettingsHelper getSettingsHelper() {
        if (sh == null) {
            sh = new SettingsHelper(FormulaTXApplication.getDatabaseOpenHelper());
        }
        return sh;
    }

    private String getParameter(String parameter) {
        return getSettingsHelper().getStringParameter(parameter);
    }

    private void setParameter(String parameter, String value) {
        getSettingsHelper().setParameter(parameter, value);
    }

    private void setParameter(String parameter, Long value) {
        getSettingsHelper().setParameter(parameter, value);
    }

    private long castToLong(String parameter) {
        return Long.parseLong(parameter);
    }

    private long castToLong(String parameter, long defValue) {
        try {
            return Long.parseLong(parameter);
        } catch (Exception ex) {
            return defValue;
        }
    }

    private String castToString(String parameter) {
        return parameter;
    }

    public long getUserId() {
        return castToLong(getParameter(Settings.USER_ID));
    }

    public void setUserId(long value) {
        setParameter(Settings.USER_ID, value);
    }

    public String getUserName() {
        return castToString(getParameter(Settings.USER_NAME));
    }

    public void setUserName(String value) {
        setParameter(Settings.USER_NAME, value);
    }

    public String getUserAvatar() {
        return castToString(getParameter(Settings.USER_AVATAR));
    }

    public void setUserAvatar(String value) {
        setParameter(Settings.USER_AVATAR, value);
    }

    public long getLowCommentIdentifier() {
        long result = -1;
        try {
            result = castToLong(getParameter(Settings.LOW_COMMENT_IDENTIFIER));
            setParameter(Settings.LOW_COMMENT_IDENTIFIER, result - 1);
        } catch (Exception ex) {
            setParameter(Settings.LOW_COMMENT_IDENTIFIER, -1L);
        }
        return result;
    }
}
