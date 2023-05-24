---------------------------------------------------------------------
TAG 1.1.0
---------------------------------------------------------------------
NEW FEATURES (lo que están entregando y está funcionando):
* Endpoints para:
  * Expresar intenciones de compra o venta
  * Procesar la transancción entre dos usuarios
  * Informar el volumen de criptoactivos operados por un usuario entre dos fechas
  * Listar las intenciones activas (ie, disponibles para operar)
  * Listar los usuarios con su reputación y cantidad de transacciones completadas (servicio extra del enunciado)
* Integración de HSQLDB (particularmente H2)
* Bootstrap de datos en la base al levantar la app
* Problema del deploy resuelto

NOTES (ej: funcionalidad que falta, alguna consideración especial):
* Integración de la API de Binance sin terminar, dada la configuración necesaria del ambiente. Por ende el listado de cotizaciones no puede realizarse aún

KNOWN ISSUES (ej: funcionalidad que saben que están funcionando diferente, algún error conocido en la funcionalidad terminada, etc) :
* Se deben refactorizar algunos DTOs, pues se utiliza el mismo DTO tanto para las request como para las responses, cuando deberian de ser 2 DTOs distintos.

---------------------------------------------------------------------
TAG 1.0.0
---------------------------------------------------------------------
NEW FEATURES (lo que están entregando y está funcionando):
* Endpoint para registrar usuarios dentro del sistema
* Modelo de usuarios, intenciones y transacciones
* Test del modelo previamente mencionado

NOTES (ej: funcionalidad que falta, alguna consideración especial):
* El deploy en Railways esta sin completar, dado que falla a la hora de deployear la build
* Los GitHub Actions poseen datos de acceso a la base de datos debido a tests para que funcionen. Al eliminarlos, el CI rompia, por ende, se dejaron

KNOWN ISSUES (ej: funcionalidad que saben que están funcionando diferente, algún error conocido en la funcionalidad terminada, etc) :
* Se verifica que quien transfiera dinero o libera las cryptos sea un usuario de la transacción, más no necesariamente el que deba enviar el dinero o liberar los activos puntualmente
* Posibles refactors menores
