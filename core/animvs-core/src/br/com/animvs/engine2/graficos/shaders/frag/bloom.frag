#ifdef GL_ES
    precision lowp float;
#endif

uniform sampler2D u_texture;
uniform float u_power;

varying vec2 v_texCoords;

void main()
{
   	vec4 sum = vec4(0);
  	//vec2 v_texCoords = vec2(gl_v_texCoords[0]);
   	int j;
   	int i;

   	for( i= -4 ;i < 4; i++)
        for (j = -3; j < 3; j++)
            sum += texture2D(u_texture, v_texCoords + vec2(j, i)*0.004) * 0.25 * u_power;

   	if (texture2D(u_texture, v_texCoords).r < 0.3)
       gl_FragColor = sum*sum*0.012 + texture2D(u_texture, v_texCoords);
    else {
        if (texture2D(u_texture, v_texCoords).r < 0.5)
            gl_FragColor = sum*sum*0.009 + texture2D(u_texture, v_texCoords);
        else
            gl_FragColor = sum*sum*0.0075 + texture2D(u_texture, v_texCoords);
    }
}