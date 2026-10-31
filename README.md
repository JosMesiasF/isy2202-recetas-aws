Recetas del Mundo
Seguridad y Calidad en el Desarrollo de Software
ISY2202 - Semana 2

1. Descripción del proyecto

Recetas del Mundo es una aplicación web desarrollada en Java con Spring Boot. Su objetivo es permitir que los usuarios consulten recetas de cocina, realicen búsquedas y puedan acceder a los detalles de cada preparación.

La aplicación incorpora mecanismos de autenticación y autorización mediante Spring Security y JWT, además de utilizar una base de datos MySQL para almacenar los usuarios y las recetas.

2. Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.2
- Maven
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL 8.4
- Docker
- HTML y CSS

Para el desarrollo se utilizó Visual Studio Code conectado a Ubuntu 24.04 mediante WSL 2. La administración de la base de datos se realizó con DBeaver y las pruebas de las APIs con Postman.

3. Funcionalidades de la aplicación

La aplicación cuenta con una página de inicio donde se muestran las recetas recientes y populares, además de un espacio destinado a publicidad.

También incluye un buscador que permite filtrar recetas por nombre, tipo de cocina, ingredientes, país de origen y dificultad.

Los usuarios pueden visualizar el listado de recetas sin iniciar sesión. Sin embargo, para acceder al detalle completo de cada receta deben autenticarse.

El detalle incluye una fotografía, ingredientes, instrucciones de preparación, tiempo de cocción y dificultad.

4. Seguridad y autenticación

Se implementó Spring Security para controlar el acceso a las páginas y APIs de la aplicación.

La autenticación de las APIs se realiza mediante JWT. Cuando un usuario ingresa sus credenciales correctamente, el sistema genera un token que permite acceder a las rutas privadas.

Las contraseñas de los usuarios se almacenan utilizando BCrypt.

Se configuraron dos roles: ROLE_ADMIN y ROLE_USER.

Las páginas web privadas utilizan autenticación mediante formulario y sesión, mientras que las APIs privadas requieren un token JWT válido.

5. Usuarios de prueba

El sistema cuenta con tres usuarios registrados en MySQL:

Administrador:
admin@recetas.local
Rol: ROLE_ADMIN

Usuario 1:
usuario1@recetas.local
Rol: ROLE_USER

Usuario 2:
usuario2@recetas.local
Rol: ROLE_USER

Las contraseñas se configuran mediante las variables del archivo .env. Los usuarios se crean automáticamente al iniciar la aplicación cuando todavía no existen en la base de datos.

6. Base de datos

Se utilizó MySQL 8.4 ejecutándose mediante Docker.

Configuración de conexión:

Host: 127.0.0.1
Puerto: 3307
Base de datos: recetas_db
Usuario: recetas_app

La base de datos contiene dos tablas principales:

usuarios: almacena los datos de las cuentas, contraseñas protegidas y roles.

recetas: almacena los nombres, ingredientes, instrucciones, fotografías y demás información de las recetas.

Dentro de la carpeta database se encuentran dos archivos:

schema.sql: contiene la estructura de las tablas.

data.sql: contiene seis recetas de ejemplo.

7. Configuración del proyecto

Antes de ejecutar la aplicación se debe crear un archivo .env utilizando como referencia el archivo .env.example.

En Ubuntu se puede crear mediante el comando:

cp .env.example .env

Dentro del archivo se deben configurar las contraseñas de MySQL, las contraseñas de los usuarios de prueba y la clave JWT.

Para generar una clave JWT se puede utilizar:

openssl rand -base64 32

El resultado debe asignarse a la variable APP_JWT_SECRET.

El archivo .env contiene información confidencial y no debe compartirse ni incluirse en el repositorio.

8. Iniciar la base de datos

Desde la carpeta principal del proyecto se debe ejecutar:

docker compose up -d mysql

Docker iniciará el contenedor MySQL utilizando la configuración definida en compose.yaml.

En una instalación nueva se debe comprobar que la base recetas_db esté creada y ejecutar los scripts de la carpeta database desde DBeaver, primero schema.sql y después data.sql.

Si la conexión utilizada no tiene permisos para crear bases de datos, se debe utilizar una cuenta con los permisos necesarios para esa operación.

Estos scripts no deben volver a ejecutarse sobre una base de datos que ya tenga las tablas y recetas cargadas.

9. Ejecutar la aplicación

Para iniciar la aplicación desde Ubuntu se debe abrir una terminal en la carpeta principal del proyecto.

Primero se pueden habilitar los permisos del Maven Wrapper:

chmod +x mvnw

Después ejecutar:

./mvnw spring-boot:run

Cuando Spring Boot termine de iniciarse, la aplicación estará disponible en:

http://localhost:8080/recetas

10. Rutas de la aplicación

Página de inicio:
http://localhost:8080/recetas

Buscador de recetas:
http://localhost:8080/recetas/buscar

Inicio de sesión:
http://localhost:8080/login

Detalle de receta:
http://localhost:8080/recetas/1

El inicio y la búsqueda son públicos. El detalle de las recetas requiere que el usuario haya iniciado sesión.

11. APIs implementadas

POST /api/auth/login
Permite autenticar a un usuario y generar un JWT.

GET /api/recetas
Permite consultar el listado público de recetas.

GET /api/recetas/buscar
Permite buscar recetas utilizando diferentes filtros.

GET /api/recetas/{id}
Permite consultar el detalle de una receta utilizando un JWT válido.

GET /api/auth/verificar
Permite comprobar la autenticación del usuario mediante JWT.

GET /api/auth/administrador
Permite verificar el acceso exclusivo de usuarios con rol ADMIN.

12. Pruebas realizadas

Se realizaron pruebas funcionales utilizando el navegador web y Postman.

Las pruebas incluyeron el inicio de sesión, generación de JWT, acceso a las rutas públicas, protección de rutas privadas y validación de roles.

Se comprobaron respuestas HTTP 200 para solicitudes autorizadas, HTTP 401 para solicitudes sin autenticación y HTTP 403 cuando un usuario no cuenta con los permisos necesarios.

También se verificó el funcionamiento de los cinco filtros de búsqueda y la visualización de los detalles de recetas con fotografías.

13. Estructura del proyecto

La carpeta src/main/java contiene los controladores, modelos, repositorios y configuraciones de Spring Boot.

La carpeta src/main/resources/templates contiene las páginas HTML desarrolladas con Thymeleaf.

La carpeta src/main/resources/static contiene los archivos CSS y las fotografías de las recetas.

La carpeta database contiene los scripts SQL necesarios para preparar la base de datos.

El archivo compose.yaml contiene la configuración de MySQL en Docker.

El archivo pom.xml contiene las dependencias y la configuración del proyecto Maven.

El archivo .env.example contiene las variables necesarias para configurar la aplicación.

14. Ejecución local

La aplicación fue desarrollada y probada localmente utilizando Ubuntu WSL 2 en Windows 11.

El servidor web funciona en el puerto 8080 y la base de datos MySQL utiliza el puerto 3307.

Se realizaron pruebas de funcionamiento de las páginas web, consultas a la base de datos y autenticación mediante JWT.

La evidencia del funcionamiento se presenta mediante capturas de pantalla de la aplicación y de las pruebas realizadas.

Asignatura: Seguridad y Calidad en el Desarrollo de Software
Código: ISY2202
Institución: Duoc UC
Actividad: Semana 2 - Aplicando conceptos de seguridad en el desarrollo de aplicaciones