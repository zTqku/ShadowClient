# Shadow Client (Android)

Proyecto inicial de una aplicación Android independiente que exporta un paquete `.mcpack` para importarlo en Minecraft Bedrock.

## Qué hace esta versión
- Muestra una interfaz inicial estilo oscuro/minimalista.
- Empaqueta los archivos de `app/src/main/assets/pack/` en `ShadowClient.mcpack`.
- Intenta abrir el paquete con una aplicación compatible (normalmente Minecraft).

## Qué NO hace todavía
- No es un cliente nativo que inyecte código en Minecraft.
- No implementa todavía Fullbright, zoom, CPS, FPS ni módulos PvP funcionales.
- No modifica el APK de Minecraft ni los mundos.
- No garantiza logros: Minecraft determina la elegibilidad según el mundo, los ajustes y los recursos/modificaciones activos.

## Compilar sin PC usando GitHub
1. Crea un repositorio nuevo en GitHub llamado `ShadowClient`.
2. Sube todos los archivos y carpetas de este proyecto a la raíz del repositorio.
3. Abre la pestaña **Actions** y permite ejecutar workflows si GitHub lo solicita.
4. El workflow `Build Shadow Client APK` compila automáticamente al hacer push a `main`.
5. Entra en la ejecución terminada y descarga el artifact `ShadowClient-debug`.
6. Extrae el ZIP descargado e instala `app-debug.apk` en Android.

También puedes ejecutar el workflow manualmente desde **Actions → Build Shadow Client APK → Run workflow**.

## Requisitos de desarrollo
- JDK 17
- Gradle 8.9
- Android Gradle Plugin 8.7.3
- Android SDK Platform 35

## Nota sobre el paquete
El paquete inicial solo contiene el manifiesto, icono e idioma. Es una base válida para seguir incorporando recursos Bedrock compatibles. Un Resource Pack no puede implementar por sí solo todas las funciones que requieren modificar el comportamiento interno del cliente.
