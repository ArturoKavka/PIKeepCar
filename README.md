# 🚗 KeepCar v3.0

**Gestión de mantenimiento vehicular** — Proyecto Intermodular DAM 2DAM 2025/26

> Arturo Kavka · Amael Silva

---

## Descripción

KeepCar es una aplicación web para el seguimiento del mantenimiento de vehículos. Permite a los usuarios controlar cambios de aceite, ITV, estado de neumáticos y recibir alertas automáticas. Incluye un panel de administración en tiempo real y un asistente mecánico con IA.

---

## Requisitos previos

| Herramienta | Versión mínima |
|---|---|
| Java JDK | 17 |
| Apache Maven | 3.8 |
| IntelliJ IDEA (recomendado) | 2023.x |

> No es necesario instalar ninguna base de datos. El proyecto usa **H2** en memoria.

---

## Instalación y arranque

### Opción A — IntelliJ IDEA (recomendada)

1. Clona o descarga el repositorio
2. Abre IntelliJ: `File → Open` → selecciona la carpeta `keepcar/` (donde está `pom.xml`)
3. Espera a que Maven descargue las dependencias automáticamente (~1-2 min)
4. Ejecuta `KeepCarApplication.java` (botón ▶ verde)
5. Abre el navegador en `http://localhost:8080`

### Opción B — Línea de comandos

```bash
# Clonar el repositorio
git clone <URL_DEL_REPO>
cd keepcar

# Compilar y arrancar
./mvnw spring-boot:run
# o en Windows:
mvnw.cmd spring-boot:run
```

Abre `http://localhost:8080` en el navegador.

---

## Credenciales de acceso

### Administradores

| Usuario | Contraseña |
|---|---|
| `arturo` | `Arturo@Admin1` |
| `amael` | `Amael@Admin2` |

### Usuarios de prueba

| Usuario | Contraseña | Nombre |
|---|---|---|
| `norly` | `Norly@2025!` | Norly García |
| `denise` | `D3nise@Garr1do` | Denise Garrido Tamarit |
| `juan` | `Juan#Garc1a` | Juan García Torres |
| `marcos` | `Marc0s!2025` | Marcos López Ruiz |
| `manu` | `Manu.H3rn` | Manu Hernández Gil |
| `rosa` | `Rosa&Fl0res` | Rosa Martínez Vega |
| `lucia` | `Lucia*Cars25` | Lucía Fernández Ortiz |
| `pedro` | `P3dro!Motor` | Pedro Sánchez Díaz |
| `sofia` | `S0fia@Drive` | Sofía Ruiz Moreno |

---

## URLs de la aplicación

| URL | Descripción |
|---|---|
| `http://localhost:8080` | Aplicación principal |
| `http://localhost:8080/admin.html` | Panel de administración (solo admins) |
| `http://localhost:8080/h2-console` | Consola H2 (base de datos) |
| `http://localhost:8080/api` | Información de la API REST |

**Consola H2:**
- JDBC URL: `jdbc:h2:mem:keepcardb`
- Usuario: `sa`
- Contraseña: *(vacío)*

---

## Configuración de funcionalidades opcionales

### 1. Chat IA con Groq

El asistente mecánico usa la API de **Groq** (gratuita). Para activarlo:

1. Regístrate en [https://console.groq.com](https://console.groq.com)
2. Crea una API key (empieza por `gsk_...`)
3. Edita `src/main/resources/application.properties`:
   ```properties
   grok.api.key=gsk_TU_API_KEY_AQUI
   ```
4. Reinicia la aplicación

También puedes introducir la key directamente en la app: `Ajustes → Chat IA (Groq)`.

### 2. Login con Google OAuth2

Para activar el login con Google necesitas registrar la app en Google Cloud Console:

1. Ve a [https://console.cloud.google.com](https://console.cloud.google.com)
2. Crea un proyecto → APIs y Servicios → Credenciales → Crear credencial OAuth 2.0
3. Tipo: **Aplicación web**
4. URI de redirección autorizado: `http://localhost:8080/login/oauth2/code/google`
5. Copia el **Client ID** y **Client Secret**
6. Edita `application.properties`:
   ```properties
   spring.security.oauth2.client.registration.google.client-id=TU_CLIENT_ID
   spring.security.oauth2.client.registration.google.client-secret=TU_CLIENT_SECRET
   ```
7. Reinicia la aplicación

---

## Funcionalidades principales

- **Control de aceite:** alertas en tiempo real según kilómetros recorridos
- **ITV:** seguimiento de fechas y alertas por vencimiento (OK / Próxima / Urgente / Vencida)
- **Neumáticos:** alertas por dibujo (<3mm → cambio inmediato) y antigüedad (>6 años)
- **Historial de mantenimiento:** registro completo por vehículo
- **Notificaciones:** marcar como leída/no leída, filtrado por tipo
- **Asistente IA:** estimación de costes de piezas y mano de obra en España
- **Panel admin:** estadísticas en tiempo real, activity log con polling, búsqueda y filtros
- **Login con Google:** OAuth2 con creación automática de usuario

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Spring Boot 3.2.5 |
| Lenguaje | Java 17 |
| ORM | Spring Data JPA / Hibernate |
| Base de datos | H2 (in-memory) |
| Seguridad | Spring Security + OAuth2 |
| Frontend | HTML + CSS + JavaScript (SPA) |
| IA | Groq API — llama3-8b-8192 |

---

## Estructura del proyecto

```
keepcar/
├── pom.xml
└── src/main/
    ├── java/com/keepcar/
    │   ├── config/
    │   │   └── SecurityConfig.java       # Spring Security + OAuth2
    │   ├── controller/
    │   │   ├── AdminController.java      # Panel admin (solo ADMIN)
    │   │   ├── ApiInfoController.java    # Info de la API en /api
    │   │   ├── AuthController.java       # Login, logout, OAuth2
    │   │   ├── GrokController.java       # Proxy Groq AI
    │   │   └── VehiculoController.java   # CRUD vehículos, alertas, notificaciones
    │   ├── model/
    │   │   ├── AuditLog.java
    │   │   ├── Notificacion.java
    │   │   ├── RegistroMantenimiento.java
    │   │   ├── Usuario.java
    │   │   └── Vehiculo.java             # ITV + neumáticos
    │   ├── repository/                   # Spring Data JPA repositories
    │   └── service/
    │       ├── AuditService.java
    │       └── MantenimientoService.java # Lógica de alertas
    └── resources/
        ├── application.properties
        ├── data.sql                      # Datos iniciales (11 usuarios, 21 vehículos)
        └── static/
            ├── index.html               # SPA principal
            └── admin.html               # Panel de administración
```

---

## API REST — Referencia rápida

Todos los endpoints (excepto auth) requieren los headers:
```
X-User-Id: <id>
X-User-Name: <nombre>
X-User-Rol: USER | ADMIN
```

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Login con usuario/contraseña |
| GET | `/api/auth/google/callback` | Callback OAuth2 Google |
| GET | `/api/vehiculos` | Listar vehículos del usuario |
| POST | `/api/vehiculos` | Crear vehículo |
| PUT | `/api/vehiculos/{id}` | Editar vehículo |
| PUT | `/api/vehiculos/{id}/km` | Actualizar kilómetros |
| GET | `/api/alertas` | Resumen de alertas (aceite, ITV, ruedas) |
| GET | `/api/notificaciones` | Listar notificaciones |
| PUT | `/api/notificaciones/{id}/leer` | Marcar como leída |
| PUT | `/api/notificaciones/{id}/no-leer` | Marcar como no leída |
| POST | `/api/ai/chat` | Chat con asistente IA |
| GET | `/api/admin/dashboard` | Stats globales (solo ADMIN) |
| GET | `/api/admin/logs` | Registro de actividad (solo ADMIN) |
| GET | `/api/admin/usuarios` | Listar usuarios (solo ADMIN) |

---

## Licencia

Proyecto educativo — IES Batoi 2025/26. No para uso en producción.
