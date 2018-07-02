package work.player;

import work.action.IPlayerVoiceAction;

/**
 * 2018/6/29
 * @author dylan.
 * Home: http://www.devdylan.cn
 */
public class Player implements IPlayerVoiceAction {
	/**
	 * player user identifier
	 */
	private String userId;
	/**
	 * if player not online, use machine do his options
	 */
	private Boolean isOnline;
	/**
	 * Next player
	 */
	private Player nextPlayer;

	private Player() {}

	public Player(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getOnline() {
		return isOnline;
	}

	public void setOnline(Boolean online) {
		isOnline = online;
	}

	public Player getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(Player nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	@Override
	public void openVoice() {

	}

	@Override
	public void closeVoice() {

	}
}
