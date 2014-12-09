package br.com.animvs.engine2.graficos.loaders;

import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;

public final class AnimacaoSkeletalData {
	public SkeletonData skeletonData;
	public AnimationStateData animationStateData;
	
	public AnimacaoSkeletalData(SkeletonData skeletonData, AnimationStateData animationStateData){
		this.skeletonData = skeletonData;
		this.animationStateData = animationStateData;
	}
}
