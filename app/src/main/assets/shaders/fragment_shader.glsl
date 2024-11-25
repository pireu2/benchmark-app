precision mediump float;

varying vec3 fPosition;
varying vec3 fNormal;


uniform vec4 uObjectColor;

uniform mat4 uModelMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uNormalMatrix;

uniform vec3 uLightPos;
uniform vec3 uLightColor;

vec3 ambient;
vec3 diffuse;
vec3 specular;

float ambientStrength = 0.2;
float specularStrength = 0.5;

void computeLight(){
    vec3 normal = normalize(vec3(uNormalMatrix * vec4(fNormal, 0.0)));
    vec3 lightDir = normalize(uLightPos - fPosition);
    vec3 viewDir = normalize(-fPosition);
    vec3 reflectDir = reflect(-lightDir, normal);

    float diff = max(dot(normal, lightDir), 0.0);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 128.0);

    ambient = ambientStrength * uLightColor;
    diffuse = diff * uLightColor;
    specular = specularStrength * spec * uLightColor;
}

void main() {
    computeLight();
    vec3 color = min((ambient + diffuse) * uObjectColor.rgb + specular, vec3(1.0));
    gl_FragColor = vec4(color ,1.0);
}