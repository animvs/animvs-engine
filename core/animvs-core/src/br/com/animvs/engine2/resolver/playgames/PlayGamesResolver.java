package br.com.animvs.engine2.resolver.playgames;

public interface PlayGamesResolver {
	public boolean getSignedInGPGS();

	public void loginGPGS();

	public void submitScoreGPGS(long score);

	public void unlockAchievementGPGS(String achievementId);

	public void getLeaderboardGPGS();

	public void getAchievementsGPGS();
}
