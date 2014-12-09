uniform sampler2D u_texture;

uniform vec2 iResolution;

void main()                             
{
	//NOTE: hold mouse down to make her move.
	vec2 lightPosition = vec2(10.0,10.0);//iMouse.xy;
	float radius = 850.0;

    float distance  = length( lightPosition - gl_FragCoord.xy );

    float maxDistance = pow( radius, 0.20);
    float quadDistance = pow( distance, 0.23);

    float quadIntensity = 1.0 - min( quadDistance, maxDistance )/maxDistance;

	vec4 texture = texture2D(u_texture, gl_FragCoord.xy / iResolution.xy );

	gl_FragColor = texture * vec4(quadIntensity);
}