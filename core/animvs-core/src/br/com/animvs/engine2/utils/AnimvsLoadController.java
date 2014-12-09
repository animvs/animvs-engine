package br.com.animvs.engine2.utils;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

import br.com.animvs.engine2.graficos.loaders.AnimacaoSkeletalData;
import br.com.animvs.engine2.graficos.loaders.AnimacaoSkeletalDataLoader;
import br.com.animvs.engine2.graficos.shaders.Shader;
import br.com.animvs.engine2.graficos.shaders.ShaderLoad;

public abstract class AnimvsLoadController implements Disposable {

    public static final class AnimvsLoadParameter {
        private String path;
        private Class<?> resourceClass;

        @SuppressWarnings("rawtypes")
        private AssetLoaderParameters assetLoaderParameter;

        @SuppressWarnings("rawtypes")
        public AnimvsLoadParameter(String path, Class<?> resourceClass, AssetLoaderParameters assetLoaderParameter) {
            this.path = path;
            this.resourceClass = resourceClass;
            this.assetLoaderParameter = assetLoaderParameter;
        }

        public AnimvsLoadParameter(String path, Class<?> resourceClass) {
            this.path = path;
            this.resourceClass = resourceClass;
        }
    }

    protected AssetManager getAssetManager() {
        return assetManager;
    }

    private AssetManager assetManager;

    private long loadStartTime;
    private Shader shader;

    private boolean loadStarted;

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public long getLoadTimeSinceStarted() {
        return TimeUtils.millis() - loadStartTime;
    }

    public boolean getLoading() {
        return loadStarted && assetManager.getProgress() < 1f;
    }

    protected abstract Array<AnimvsLoadParameter> getResources();

    public AnimvsLoadController() {
        assetManager = new AssetManager();
    }

    @SuppressWarnings("unchecked")
    public void load() {
        loadStarted = true;
        loadStartTime = TimeUtils.millis();

        if (shader == null)
            shader = new ShaderLoad();

        assetManager.setLoader(AnimacaoSkeletalData.class, new AnimacaoSkeletalDataLoader(new InternalFileHandleResolver()));
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        Array<AnimvsLoadParameter> resources = getResources();

        if (resources == null)
            throw new RuntimeException("The return of the method 'getResources' must be != null");

        for (int i = 0; i < resources.size; i++)
            assetManager.load(resources.get(i).path, resources.get(i).resourceClass, resources.get(i).assetLoaderParameter);
    }

    public final <T> T get(String filename, Class<T> resourceClass) {
        return assetManager.get(filename, resourceClass);
    }

    public void update() {
        if (assetManager.getProgress() < 1f)
            shader.update();

        assetManager.update();
    }

    @Override
    public void dispose() {
        if (assetManager != null)
            assetManager.dispose();
    }
}
