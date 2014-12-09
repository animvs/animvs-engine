package br.com.animvs.engine2.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public abstract class IO {
	private IO(){
	}
	
	public static FileHandle[] listaArquivosDiretorioInterno(String diretorio) {
		// if (Gdx.app.getType() == ApplicationType.Desktop) {
		// // Hack para desktop
		// if (!rodandoVersaoJAR())
		// diretorio = "./bin/" + diretorio;
		// } else {
		// throw new
		// RuntimeException("Método para listar arquivos de diretório inexistente para o tipo de aplicação: "
		// + Gdx.app.getType().toString());
		// }
		
		FileHandle diretorioHandle = Gdx.files.internal(diretorio);
		
		//Altera o diretporio para funcionar em modo DESENVOLVIMENTO
		if (!diretorioHandle.exists() || !diretorioHandle.isDirectory())
			diretorioHandle = Gdx.files.absolute("./bin/" + diretorio);

		if (!diretorioHandle.exists() || !diretorioHandle.isDirectory())
			throw new RuntimeException("Diretório Não encontrado: " + diretorio);
		
		Array<FileHandle> arquivosArray = new Array<FileHandle>();
		
		FileHandle[] arquivos = diretorioHandle.list();
		
		for (int i = 0; i < arquivos.length; i++) {
			if (!arquivos[i].isDirectory())
				arquivosArray.add(arquivos[i]);
		}

		arquivos = new FileHandle[arquivosArray.size];
		
		for (int i = 0; i < arquivosArray.size; i++)
			arquivos[i] = arquivosArray.get(i);
		
		return arquivos;
	}
}
