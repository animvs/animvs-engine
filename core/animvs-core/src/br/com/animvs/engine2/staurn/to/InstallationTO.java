package br.com.animvs.engine2.staurn.to;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

public class InstallationTO {

	public static final int LAST_VERSION = 1;

	/**
	 * Vers�o do arquivo; para manter compatibilidade em vers�es futuras.
	 */
	private int installFileVersion;

	private ApplicationType applicationType;

	/**
	 * Serial único atribuído pelos sistemas da Animvs (remotamente). Pode ser
	 * nulo, caso a aplicaçãoo não tenha conseguido conectar aos servidores
	 */
	private String serial;

	public int getInstallFileVersion() {
		return installFileVersion;
	}

	public void setVersion(int installFileVersion) {
		this.installFileVersion = installFileVersion;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public InstallationTO() {
	}

	public static final InstallationTO generateInstallationTO() {
		InstallationTO installationTO = new InstallationTO();

		installationTO.setVersion(LAST_VERSION);
		installationTO.setApplicationType(Gdx.app.getType());

		return installationTO;
	}
}
