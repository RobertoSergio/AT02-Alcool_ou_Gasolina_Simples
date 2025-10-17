# AT02 - Álcool ou Gasolina

Aplicativo Android simples que calcula qual combustível é mais vantajoso: álcool ou gasolina.

## Funcionalidades

- Cálculo baseado na relação de preços entre álcool e gasolina
- Permite alternar entre 70% e 75% como percentual de referência
- Aceita entrada com vírgula ou ponto decimal
- Validação de entrada de dados

## Como funciona

O cálculo é baseado na regra dos 70% (ou 75%):
- Se o preço do álcool for menor ou igual a 70% (ou 75%) do preço da gasolina, vale a pena usar álcool
- Caso contrário, vale a pena usar gasolina

## Tecnologias

- Kotlin
- Android SDK
- Material Design Components
- SharedPreferences para persistência de dados

## Estrutura do projeto

```
app/src/main/
├── java/com/example/myapplication/
│   └── MainActivity.kt
└── res/
    └── layout/
        └── activity_main.xml
```

## Requisitos

- Android Studio Narwhal ou superior
- Gradle 8.x
- Android SDK mínimo: verificar em `build.gradle.kts`

## Build

```bash
./gradlew assembleDebug
```