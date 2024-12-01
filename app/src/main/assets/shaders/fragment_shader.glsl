precision mediump float;

varying vec3 fPosition;
varying vec3 fNormal;
varying vec2 fTexCoord;

uniform vec4 uObjectColor;

uniform mat4 uModelMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uNormalMatrix;

uniform vec3 uLightPos[10];
uniform vec3 uLightColor[10];

vec3 ambient;
vec3 diffuse;
vec3 specular;

float ambientStrength = 0.2;
float specularStrength = 0.5;
float occlusionStrength = 0.3;

void computeLight(){
    vec3 normal = normalize(vec3(uNormalMatrix * vec4(fNormal, 0.0)));
    vec3 viewDir = normalize(-fPosition);
    ambient = vec3(0.0);
    diffuse = vec3(0.0);
    specular = vec3(0.0);

    for (int i = 0; i < 10; i++) {
        vec3 lightDir = normalize(uLightPos[i] - fPosition);
        vec3 reflectDir = reflect(-lightDir, normal);

        float diff = max(dot(normal, lightDir), 0.0);
        float spec = pow(max(dot(viewDir, reflectDir), 0.0), 128.0);

        ambient += ambientStrength * uLightColor[i];
        diffuse += diff * uLightColor[i];
        specular += specularStrength * spec * uLightColor[i];
    }

    // Ambient occlusion effect
    float occlusion = 1.0 - occlusionStrength * (1.0 - dot(normal, viewDir));
    ambient *= occlusion;
    diffuse *= occlusion;
    specular *= occlusion;
}

void main() {
    computeLight();
    vec3 color = min((ambient + diffuse) * uObjectColor.rgb + specular, vec3(1.0));
    gl_FragColor = vec4(color ,1.0);
}