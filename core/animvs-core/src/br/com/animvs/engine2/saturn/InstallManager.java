package br.com.animvs.engine2.saturn;

import br.com.animvs.engine2.net.Http;
import br.com.animvs.engine2.staurn.to.InstallationTO;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public final class InstallManager {

	private InstallationTO installTO;
	private String gameName;
	private int versionCode;
	private String versionName;
	private Json json;

	private boolean desktopVersionOnlyForDevelopers;

	public String getVersionName() {
		return versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public InstallationTO getInstallTO() {
		return installTO;
	}

	public InstallManager(String gameName, String versionName, int vesionCode, boolean desktopVersionOnlyForDevelopers) {
		this.gameName = gameName;
		this.versionCode = vesionCode;
		this.desktopVersionOnlyForDevelopers = desktopVersionOnlyForDevelopers;
		this.versionName = versionName;

		json = new Json();

		load(versionCode);

		if (installTO.getSerial() == null)
			generateSaturn();
		else
			Gdx.app.log("ANIMVS CONNECT", "Saturn found: " + installTO.getSerial());
	}

	private final void generateSaturn() {
		Gdx.app.log("ANIMVS CONNECT", "Creating saturn ...");

		String service = gameName;

		if (Gdx.app.getType() == ApplicationType.Desktop) {
			if (desktopVersionOnlyForDevelopers)
				service += "_DEVELOPER";
			else
				service += "_DESKTOP";
		} else if (Gdx.app.getType() == ApplicationType.Android)
			service += "_ANDROID";
		else if (Gdx.app.getType() == ApplicationType.iOS)
			service += "_IOS";

		String headers[] = new String[2];
		headers[0] = "service";
		headers[1] = "version";

		String values[] = new String[2];
		values[0] = service;
		values[1] = String.valueOf(versionCode);

		HttpResponseListener responseListener = new HttpResponseListener() {

			@Override
			public final void handleHttpResponse(HttpResponse httpResponse) {
				String[] resultado = httpResponse.getResultAsString().split(":");

				if (resultado[0].equals("OK")) {
					installTO.setVersion(versionCode);
					installTO.setApplicationType(Gdx.app.getType());
					installTO.setSerial(resultado[1]);
					save();
					Gdx.app.log("ANIMVS CONNECT", "Saturn processed sucessfully. Serial: " + installTO.getSerial());
				} else
					Gdx.app.log("ANIVMS CONECT", "Saturn refused. Status: " + resultado[0] + " Message: " + resultado[1]);
			}

			@Override
			public final void failed(Throwable t) {
				Gdx.app.log("SATURN", "Saturn generation post has failed", t);
			}

			@Override
			public void cancelled() {
				Gdx.app.log("SATURN", "Saturn generation post was canceled");

			}
		};

		Http.enviaRequest("http://www.animvs.com.br/sys/reg.php", headers, values, responseListener);
	}

	private final void load(int versionCode) {
		FileHandle file;

		// if (Gdx.app.getType() != ApplicationType.Desktop)
		// arquivo = new FileHandle(IOController.getDiretorioRuntime() +
		// "/sys/saturn.srl");
		// else
		file = Gdx.files.local("sys/saturn.srl");

		if (!file.exists())
			installTO = InstallationTO.generateInstallationTO();
		// else if (Gdx.app.getType() == ApplicationType.iOS) {
		// Gdx.app.log("ANIMVS CONNECT",
		// "FIX iOS - Deserializando saturn do caminho: " +
		// arquivo.file().getAbsolutePath());
		// instalacaoTO =
		// Engine.getSerializadorJson().deserializaLocalIOS(InstallationTO.class,
		// "sys/saturn.srl");
		else {
			json.setIgnoreUnknownFields(true);
			installTO = json.fromJson(InstallationTO.class, file);

			if (installTO == null)
				installTO = InstallationTO.generateInstallationTO();
			else if (versionCode != installTO.getInstallFileVersion()) {
				installTO.setVersion(versionCode);
				save();
			}
		}
	}

	private final void save() {
		FileHandle file;

		// if (Gdx.app.getType() != ApplicationType.Desktop)
		// arquivo = new FileHandle(IOController.getDiretorioRuntime() +
		// "/sys/saturn.srl");
		// else
		file = Gdx.files.local("sys/saturn.srl");

		file.parent().mkdirs();
		file.writeString(json.toJson(installTO), false, "UTF-8");
	}
}
