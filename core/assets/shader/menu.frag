uniform float iTime;
// uniform vec2 mouse;
vec2 resolution = vec2(1280, 768);

void main() {

    vec2 p =(gl_FragCoord.xy*2.-resolution.xy)/resolution.y;

    vec4 c = vec4(0.);

	 c =  	// color
		 vec4(p*.5+.5,0.,1)
		// rect
		 / ((abs((abs( p.x+p.y )+abs (p.x-p.y) )/1.5-1.)/.15)*2.1-vec4(.2))

		// animation
		-abs(p.x*sin(iTime));

	gl_FragColor =c;
}