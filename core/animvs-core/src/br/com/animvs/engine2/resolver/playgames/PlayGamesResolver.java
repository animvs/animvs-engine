package br.com.animvs.engine2.resolver.playgames;

public interface PlayGamesResolver {
	boolean getSignedInGPGS();

	void loginGPGS();

	void submitScoreGPGS(long score);

	void unlockAchievementGPGS(String achievementId);

	void getLeaderboardGPGS();

	void getAchievementsGPGS();
}
