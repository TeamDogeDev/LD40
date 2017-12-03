#version 120

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;
uniform float iIntensity; // = 0.25;
const float PI = 3.1415926535;

int blurSize = 2;
void main() {
    vec4 sum = vec4(0);
   //vec2 texcoord = vec2(gl_TexCoord[0]);
    vec2 texcoord = v_texCoord0;
    int j;
    int i;

    for( i= -4 ;i < 4; i++) {
        for(j=-3;j<3;j++){
            sum += texture2D(u_sampler2D, texcoord + vec2(j, i)*0.004) * iIntensity;
        }
    }
    if (texture2D(u_sampler2D, texcoord).r < 0.3) {
        gl_FragColor = sum*sum*0.012 + texture2D(u_sampler2D, texcoord);
    } else {
        if (texture2D(u_sampler2D, texcoord).r < 0.5) {
            gl_FragColor = sum*sum*0.009 + texture2D(u_sampler2D, texcoord);
        } else {
            gl_FragColor = sum*sum*0.0075 + texture2D(u_sampler2D, texcoord);
        }
    }

//    gl_FragColor = texture2D(u_sampler2D,v_texCoord0.st) * vec4(0, 0, 1, v_color.a);
}