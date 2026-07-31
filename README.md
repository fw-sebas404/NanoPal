# NanoPal — Mascota Virtual con IA Local

NanoPal es una aplicación Android nativa desarrollada en Kotlin que utiliza un modelo de lenguaje (LLM) ejecutado localmente para interactuar con el usuario. La mascota es intencionalmente ignorante y graciosa.

## Características
- **IA Local**: Ejecuta modelos GGUF (Llama 3.2 1B) sin necesidad de internet (tras la descarga inicial).
- **STT/TTS Nativo**: Usa las APIs de Google para escuchar y hablar.
- **Sistema de Memoria**: Guarda datos de la conversación en un archivo local para personalizar respuestas.
- **UI Animada**: El avatar reacciona cuando escucha y cuando habla.

---

## Cómo Generar el APK (2 Opciones)

### Opción 1: GitHub Actions (Recomendado)
1. Crea un nuevo repositorio en GitHub.
2. Sube todo el contenido de la carpeta `NanoPal` a tu repositorio.
3. Ve a la pestaña **Actions** en tu repositorio de GitHub.
4. Selecciona el workflow **Build NanoPal APK**.
5. Haz clic en **Run workflow**.
6. Una vez finalizado, descarga el archivo desde la sección **Artifacts**.

### Opción 2: Google Colab
1. Abre [Google Colab](https://colab.research.google.com/).
2. Sube el archivo `colab_build.ipynb` incluido en este paquete.
3. Comprime la carpeta `NanoPal` en un archivo llamado `NanoPal.zip`.
4. Sube `NanoPal.zip` a los archivos de Colab.
5. Ejecuta todas las celdas del notebook. El APK se descargará automáticamente al final.

---

## Reemplazo de Assets Visuales
He incluido *placeholders* (imágenes temporales) para que el proyecto compile. Antes de generar tu APK final, reemplaza los siguientes archivos en `app/src/main/res/drawable/` por tus propios diseños:

- `image_fa6dbe.png`: Logo de la app.
- `splash.png`: Imagen de fondo de carga.
- `nanopal_avatar.jpg`: Estado normal.
- `nanopal_listening.jpg`: Estado cuando el micrófono está activo.
- `nanopal_talking.jpg`: Estado cuando la mascota responde por voz.

---

## Notas Técnicas
- **Modelo**: La app descarga automáticamente `llama-3.2-1b-instruct-q4_k_m.gguf` (~800MB) al iniciar por primera vez.
- **Requisitos**: Se recomienda un dispositivo con al menos 4GB de RAM para una ejecución fluida de la IA local.
- **Privacidad**: Todas las conversaciones se procesan y guardan localmente en el dispositivo.
