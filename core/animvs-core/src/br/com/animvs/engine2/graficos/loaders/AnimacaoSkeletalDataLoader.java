package br.com.animvs.engine2.graficos.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

/**
 * Loader para animações esqueletais 2D
 * 
 * @author Daldegan
 * 
 */
public final class AnimacaoSkeletalDataLoader extends SynchronousAssetLoader<AnimacaoSkeletalData, AnimacaoSkeletalDataLoader.AnimacaoSkeletalDataParameter> {

	public AnimacaoSkeletalDataLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public AnimacaoSkeletalData load(AssetManager assetManager, String fileName, FileHandle file, AnimacaoSkeletalDataParameter parameter) {
		boolean binario = false;

		if (parameter != null)
			binario = parameter.binario;

		SkeletonData skeletonData = null;

		if (binario)
			skeletonData = new SkeletonBinary(assetManager.get(getAtlasPath(file, parameter), TextureAtlas.class)).readSkeletonData(file);
		else
			skeletonData = new SkeletonJson(assetManager.get(getAtlasPath(file, parameter), TextureAtlas.class)).readSkeletonData(file);

		AnimationStateData animationStateData = new AnimationStateData(skeletonData);

		return new AnimacaoSkeletalData(skeletonData, animationStateData);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AnimacaoSkeletalDataParameter parameter) {
		Array<AssetDescriptor> dependencias = new Array<AssetDescriptor>();

		dependencias.add(new AssetDescriptor(getAtlasPath(file, parameter), TextureAtlas.class));

		return dependencias;
	}

	private final String getAtlasPath(FileHandle file, AnimacaoSkeletalDataParameter parametro) {
		if (parametro == null || parametro.caminhoAtlas == null)
			return file.nameWithoutExtension() + ".atlas";
		else
			return parametro.caminhoAtlas;
	}

	/**
	 * Parâmetros para carga (load) de animações esqueletais 2D
	 * 
	 * @author Daldegan
	 * 
	 */
	static public class AnimacaoSkeletalDataParameter extends AssetLoaderParameters<AnimacaoSkeletalData> {

		/**
		 * Se a animação deve ser carregada utilizando formato binário, caso
		 * contrário utilizará o formato JSON
		 **/
		public boolean binario = false;
		public String caminhoAtlas;

		public AnimacaoSkeletalDataParameter() {
		}

		public AnimacaoSkeletalDataParameter(boolean binario, String caminhoAtlas) {
			this.binario = binario;
			this.caminhoAtlas = caminhoAtlas;
		}
	}
}
