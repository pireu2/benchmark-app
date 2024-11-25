attribute vec3 vPosition;
attribute vec3 vNormal;
attribute vec2 vTexCoord;

varying vec3 fNormal;
varying vec3 fPosition;
varying vec2 fTexCoord;

uniform mat4 uModelMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;

void main() {
    gl_Position = uProjectionMatrix * uViewMatrix * uModelMatrix * vec4(vPosition, 1.0);
    fNormal = vNormal;
    fPosition = vec3(uModelMatrix * vec4(vPosition, 1.0));
    fTexCoord = vTexCoord;
}