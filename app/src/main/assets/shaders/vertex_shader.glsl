attribute vec3 vPosition;
attribute vec3 vNormal;

varying vec3 fNormal;
varying vec3 fPosition;

uniform mat4 uModelMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;



void main() {
    gl_Position = uProjectionMatrix * uViewMatrix * uModelMatrix * vec4(vPosition, 1.0);
    fNormal = vNormal;
    fPosition = vPosition;
}