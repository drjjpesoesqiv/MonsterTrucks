#ifdef GL_ES
#define LOWP lowp
#define MED mediump
precision lowp float;
#else
#define LOWP
#define MED
#endif
 
uniform sampler2D u_texture;
varying MED vec2 v_texCoord0;
 
float toonify(in float intensity) {
    if (intensity > 0.8)
        return 1.0;
    else if (intensity > 0.6)
        return 0.6;
    else if (intensity > 0.4)
        return 0.4;
    else
        return 0.2;
}
 
void main(){
    vec4 color = texture2D(u_texture, v_texCoord0);
    float factor = toonify(max(color.r, max(color.g, color.b)));
    gl_FragColor = vec4(factor*color.rgb, color.a);
}