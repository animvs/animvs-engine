#ifdef GL_ES
    precision mediump float;
#endif

/*varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_power;
uniform float u_time;

void main(void) {
    vec3 luminanceVector = vec3(0.2125, 0.7154, 0.0721);
    vec4 sample = texture2D(u_texture, v_texCoords);

    float luminance = dot(luminanceVector, sample.rgb);
    luminance = max(0.0, luminance - u_brightPassThreshold);
    sample.rgb *= sign(luminance);
    sample.a = 1.0;

    gl_FragColor = sample;
}*/

varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_power;

void main() {
    vec4 sample0, sample1, sample2, sample3;
    float step = u_power / 100.0;
    sample0 = texture2D(u_texture, vec2(v_texCoords.x - step, v_texCoords.y - step));
    sample1 = texture2D(u_texture, vec2(v_texCoords.x + step, v_texCoords.y + step));
    sample2 = texture2D(u_texture, vec2(v_texCoords.x + step, v_texCoords.y - step));
    sample3 = texture2D(u_texture, vec2(v_texCoords.x - step, v_texCoords.y + step));

    gl_FragColor = (sample0 + sample1 + sample2 + sample3) / 4.0;
}