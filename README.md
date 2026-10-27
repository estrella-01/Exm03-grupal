# Proyecto Examen - Desarrollo Móvil (KMP)
- Said Acosta Revollo
- Melanny Estrella Sandoval Arnez
- Angel Fernando Belmonte Escalante
  
Este proyecto está construido con Kotlin Multiplatform (KMP), Jetpack Compose y Firebase. Demuestra la integración de bases de datos en la nube, notificaciones locales, persistencia local y reactividad en la UI.

##  Ejercicio 1: E50-5 · Firestore + Notificación interna (50 pts)

### Configuración en Firebase
- Se creó una base de datos en **Cloud Firestore** en modo de prueba (reglas: `allow read, write: if true;`).
- Colección: `tareas`.
- Campos: `titulo` (String), `completada` (Boolean), `timestamp` (Number/Int64).

### ¿Cómo funciona?
1. En el `TaskViewModel`, se usa la librería **GitLive** (`dev.gitlive.firebase.firestore`) para escuchar la colección `tareas` mediante un Flow (`.snapshots.collect`).
2. Se filtran los cambios usando `ChangeType.ADDED` para detectar solo documentos nuevos, guardando los IDs anteriores para evitar notificar la carga inicial.
3. Al detectar una nueva tarea, extrae el campo `titulo` y llama a `TaskNotifier.showNotification()`.
4. `NotificationHelper` (en `androidMain`) crea un canal de notificación con `IMPORTANCE_HIGH` y verifica el permiso `POST_NOTIFICATIONS` en Android 13+.
5. Se usa un `PendingIntent` con `FLAG_IMMUTABLE` para que al tocar la notificación se abra la app. El botón de silencio persiste en `SharedPreferences`.

### ¿Cómo verificarlo?
1. Abre la app en el emulador y acepta el permiso de notificaciones.
2. Ve a la consola de Firebase > Firestore > Colección `tareas` > **Agregar documento** (con los campos mencionados).
3. Guarda el documento y desliza la cortina de notificaciones del emulador hacia abajo. Verás la notificación **"Nueva tarea: [Tu título]"**.
4. *Prueba de silencio:* Activa el botón "Silenciar notificaciones" en la app. Vuelve a agregar un documento y comprueba que esta vez **no** llega la notificación.

---

##  Ejercicio 2: E25-2 · Room como fuente de verdad (25 pts)

### ¿Cómo funciona?
1. Se define la entidad `ConfigItem` (id, clave, valor, timestamp) y un `ConfigDao` con métodos para insertar, borrar y obtener un `Flow<List<ConfigItem>>`.
2. La clase `AppDatabase` (Room) se instancia de forma segura (Singleton) para proveer el DAO.
3. El `RoomViewModel` expone el flujo del DAO usando `stateIn`, logrando que la UI sea 100% reactiva.
4. En el `init` del ViewModel se siembran 3 registros de ejemplo en el primer arranque.
5. La pantalla en Compose (`RoomScreen`) recolecta el flujo con `collectAsState` y permite agregar o borrar elementos en tiempo real.

### ¿Cómo verificarlo?
1. Navega a la pestaña de **Room** en la app.
2. Verás los 3 registros sembrados automáticamente al iniciar (Tema, Idioma, Notificaciones).
3. Escribe una nueva "Clave" y "Valor", presiona **Agregar** y verás el nuevo ítem aparecer instantáneamente en la lista (sin recargar).
4. Presiona **Borrar Todo** y la lista se vaciará reactivamente.

---
