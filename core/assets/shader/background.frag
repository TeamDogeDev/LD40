#version 120

//varying vec4 v_color;
varying vec2 v_texCoord0;

//uniform sampler2D u_sampler2D;
//uniform float redLevel;
//uniform vec3 camPosition;
//uniform vec2 scroll;
//uniform vec2 resolution;
//uniform float cloudsize;

uniform float iTime;

// UNIFORMS
vec2 iResolution = vec2(1280, 768);

float uDistanceFactor = 250.0;
float uStarSize = 9.0;
const int uNumStars = 100;
vec4 uDominantColor = vec4(0.8,0.5,0.4,1.0);

float rand(vec2 co){
  return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

vec4 getColor(vec2 seed)
{
   vec4 color;

   color.r = uDominantColor.r * rand(seed*134.23);
   color.g = uDominantColor.g * rand(seed*235.43);
   color.b = uDominantColor.b * rand(seed*-32.89);

   return color;
}

float makeStar(vec2 fragCoord, float dist, float speed, float size)
{
   vec2 currPos = iResolution.xy * 0.5;
   currPos.x += dist * sin(iTime*speed);
   currPos.y += dist * cos(iTime*speed);
   return size / (1.0+length(currPos - fragCoord.xy));
}

void main() {
   vec2 fragCoord = vec2(v_texCoord0.x * iResolution.x, v_texCoord0.y * iResolution.y);

   vec4 final = vec4(0);

   vec2 derpseed = vec2(0.44,2.12);

   for(int i = 0; i < uNumStars; i++) {
      vec4 color = getColor(derpseed);
      float dist = rand(derpseed) * uDistanceFactor;
      float speed = rand(derpseed*3.99);
      float size = rand(derpseed*225.22) * uStarSize;
      final += color * makeStar(fragCoord,dist,speed,size);

      derpseed.x += 1.54;
      derpseed.y += 0.24;
   }

   final.a = 1.0;
   gl_FragColor = final;
}