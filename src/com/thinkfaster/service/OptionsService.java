package com.thinkfaster.service;

import com.thinkfaster.service.database.DatabaseHelper;

/**
 * Created by brekol on 07.05.15.
 */
public class OptionsService extends BaseService{

    DatabaseHelper databaseHelper = new DatabaseHelper(activity);

    public String getUsername() {
        return databaseHelper.getUsername();
    }

    public void updateUsername(String username) {
        databaseHelper.updateUsername(username);
    }
}
