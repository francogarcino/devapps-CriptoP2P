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