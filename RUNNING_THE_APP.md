# JungTarot - Instrucciones de Ejecución

## ✅ Estado de Compilación
La aplicación ha sido compilada exitosamente en modo Debug:
- **APK generado**: `app/build/outputs/apk/debug/app-debug.apk` (12.79 MB)
- **Configuración**: Android API 36, `x86_64`

## 📱 Opciones para Ejecutar la App

### Opción 1: Android Emulator (Recomendado para PC sin GPU dedicada)

#### Paso 1: Iniciador del Emulador
```powershell
cd c:\Users\yo\Desktop\dev\new-tarot\jungtarot

# Iniciar emulador (sin animaciones de boot para mejor rendimiento)
$env:ANDROID_SDK_ROOT="$env:LOCALAPPDATA\Android\Sdk"
& "$env:LOCALAPPDATA\Android\Sdk\emulator\emulator.exe" -avd Pixel5_Android14 -no-audio -no-boot-anim -gpu swiftshader_indirect
```

#### Paso 2: Instalar la APK En la Terminal (nueva ventana):
```powershell
cd c:\Users\yo\Desktop\dev\new-tarot\jungtarot

# Instalar app en emulador
.\gradlew installDebug

# O directamente con adb:
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" install app/build/outputs/apk/debug/app-debug.apk
```

#### Paso 3: Lanzar la aplicación  
```powershell
# Iniciar la app
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" shell am start -n com.example.tarot/.MainActivity

# Ver logs en tiempo real (opcional)
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" logcat | findstr tarot
```

### Opción 2: Android Studio (Si está instalado)

1. Abrir Android Studio
2. `File > Open` → Seleccionar la carpeta `jungtarot`
3. Click en ▶️ (Run) o `Shift+F10`
4. Seleccionar el emulador creado o crear uno nuevo

### Opción 3: Dispositivo Android Físico

1. Conectar dispositivo Android con cable USB
2. Habilitar "Depuración USB" en Configuración del dispositivo
3. La terminal de PowerShell debería detectarlo con: `adb devices`
4. Luego ejecutar: `.\gradlew installDebug`

---

## 🎯 Características de la App

- **Lecturas Tarot**: Selecciona cartas y recibe interpretaciones psicológicas
- **Cartas Invertidas**: Cada carta puede estar derecha o invertida, con significados diferentes
- **Spreads disponibles**:
  - 1 carta (consejo rápido)
  - 2 cartas (pregunta-respuesta)
  - 3 cartas (pasado-presente-futuro)
- **Interpretaciones Duales**:
  - Inteligencia Artificial Gemini (si API disponible)
  - Motor local (fallback automático)
- **Bilingüe**: Español e Inglés

---

## 🔧 Comandos Útiles

```powershell
# Ver dispositivos/emuladores activos
adb devices

# Instalar APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Desinstalar app
adb uninstall com.example.tarot

# Ejecutar app
adb shell am start -n com.example.tarot/.MainActivity

# Detener app
adb shell am force-stop com.example.tarot

# Ver logs
adb logcat -s tarot

# Clear app data
adb shell pm clear com.example.tarot
```

---

## ⚠️ Notas Importantes

### Si el Emulador No Arranca:
- Los drivers de GPU pueden causar problemas → usa `-gpu swiftshader_indirect` (más lento pero estable)
- Si eso no funciona, usa `-gpu off` (CPU virtual)
- En Windows, a veces necesitas ejecutar como Administrador

### Si la App no Se Instala:
```powershell
# Limpiar caché de Android
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" shell pm clear com.example.tarot

# Reinstalar
.\gradlew installDebug
```

### Errores de API Key Gemini:
- La app está configurada para usar fallback automático a motor local
- Sin API key válido, usará interpretaciones pre-generadas

---

## 📝 Modificaciones Recientes

✅ Feature: **Cartas Invertidas (Reversals)**
- Cada carta puede estar "Derecha" (upright) o "Invertida" (reversed)
- Significados psicológicos únicos para cada orientación
- El AI Gemini recibe información de orientación en el prompt
- Interfaz con switches toggle para cada carta seleccionada

✅ UI Improvements:
- Enhanced top bar con imagen de fondo semi-transparente
- Mejor layout de botones (Random & Start lado a lado)
- Toggle switches en CardPicker para reversal control

---

## 📞 Soporte Técnico

Si encuentras problemas:

1. **Limpiar build**:
   ```powershell
   .\gradlew clean
   .\gradlew assembleDebug -x lint
   ```

2. **Reconectar emulador**:
   ```powershell
   adb kill-server
   adb start-server
   adb devices
   ```

3. **Ver log completo de compilación**:
   ```powershell
   .\gradlew assembleDebug
   ```

