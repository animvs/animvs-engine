#ifdef GL_ES
	precision highp float; 
#endif 

const float pi = 3.14159265358979323846264 / 3.0;
const float eyeRadius = 0.02;
const float innerRadius = 0.25;
const float outerRadius = 0.3;
const float offset = 0.125;

uniform vec2 iResolution;
uniform float iGlobalTime;

void main() {
	
	vec2 aspect = vec2(iResolution.x/iResolution.y, 1.0);
  	vec2 uv = aspect*(gl_FragCoord.xy * 1.5 / iResolution);
	vec2 circleOrigin = aspect*vec2(0.75, 0.75);
	vec2 posRelCircle = uv-circleOrigin;

	float d = distance(circleOrigin, uv);
	
	vec4 finalColor = vec4(0.0);
	
	if (d < eyeRadius) {
		finalColor.rgb = vec3(mix(1.0, 0.0, d / eyeRadius));
	} else if (innerRadius < d && d < outerRadius) {
		float angle = mod(atan(posRelCircle.y, posRelCircle.x)/(2.0*pi) + iGlobalTime, 1.0);
		float c;
		if (angle < offset)
			c = 0.0;
		else
			c = ((1.0 - angle) - offset)/(1.0 - offset);
		finalColor.rgb = vec3(c);
	} else
		finalColor.rgb = vec3(0.0);

	finalColor.a = 1.0;
	gl_FragColor = finalColor;
}
