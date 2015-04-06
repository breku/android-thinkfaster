package com.thinkfaster.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: Breku
 * Date: 24.08.14
 */
public class GameParameters {

    @JsonProperty("gameID")
    private Long gameID;

    public GameParameters() {
    }

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }
}
