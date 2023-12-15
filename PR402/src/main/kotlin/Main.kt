open class Vehiculo {
    var rueda: Int = 0
    var motor: String = ""
    var numAsientos: Int = 0
    var color: String = ""
    var modelo: String = ""

    constructor(rueda: Int, motor: String, numAsientos: Int, color: String, modelo: String) {
        this.rueda = rueda
        this.motor = motor
        this.numAsientos = numAsientos
        this.color = color
        this.modelo = modelo
    }

    class Coche(
        rueda: Int,
        motor: String,
        numAsientos: Int,
        color: String,
        modelo: String
    ) : Vehiculo(rueda, motor, numAsientos, color, modelo)

    class Triciclo(
        rueda: Int,
        numAsientos: Int,
        color: String,
        modelo: String
    ) : Vehiculo(rueda, "", numAsientos, color, modelo) {
        override fun mostrarDatos() {
            println("Triciclo(rueda=$rueda, numAsientos=$numAsientos, color='$color', modelo='$modelo')")
        }
    }

    class Patinete(
        rueda: Int,
        motor: String,
        color: String,
        modelo: String
    ) : Vehiculo(rueda, motor, 0, color, modelo)

    class Moto(
        rueda: Int,
        motor: String,
        numAsientos: Int,
        color: String,
        modelo: String
    ) : Vehiculo(rueda, motor, numAsientos, color, modelo) {
        init {
            require(rueda <= 2)
            require(numAsientos <= 2)
        }

        override fun mostrarDatos() {
            println("Moto(rueda=$rueda, motor='$motor', numAsientos=$numAsientos, color='$color', modelo='$modelo')")
        }
    }

    class Trailer(
        rueda: Int,
        motor: String,
        numAsientos: Int,
        color: String,
        modelo: String,
        val cargaMaxima: Int
    ) : Vehiculo(rueda, motor, numAsientos, color, modelo) {
        init {
            require(rueda >= 6)
        }

        override fun mostrarDatos() {
            println(
                "Trailer(rueda=$rueda, motor='$motor', numAsientos=$numAsientos, color='$color', modelo='$modelo', cargaMaxima=$cargaMaxima)"
            )
        }
    }

    class Furgoneta(
        rueda: Int,
        motor: String,
        numAsientos: Int,
        color: String,
        modelo: String,
        val cargaMaxima: Int
    ) : Vehiculo(rueda, motor, numAsientos, color, modelo) {
        init {
            require(rueda <= 6)
        }

        override fun mostrarDatos() {
            println(
                "Furgoneta(rueda=$rueda, motor='$motor', numAsientos=$numAsientos, color='$color', modelo='$modelo', cargaMaxima=$cargaMaxima)"
            )
        }
    }

    open fun mostrarDatos() {
        println(
            "Vehiculo(rueda=$rueda, motor='$motor', numAsientos=$numAsientos, color='$color', modelo='$modelo')"
        )
    }

    companion object {
        fun contarVehiculos(vehiculos: Array<Vehiculo?>, tipo: String): Int {
            return vehiculos.filterNotNull().count { it.javaClass.simpleName == tipo }
        }

        fun listarPorNombre(vehiculos: Array<Vehiculo?>) {
            println("Listado de vehículos por nombre:")
            val vehiculosOrdenados = vehiculos.filterNotNull().sortedBy { it.modelo }
            vehiculosOrdenados.forEach { it.mostrarDatos() }
        }
    }
}

fun main() {
    val vehiculos: Array<Vehiculo?> = arrayOfNulls(100)

    var index = 0

    while (true) {
        println("Seleccione el tipo de vehículo que desea crear:")
        println("1. Coche")
        println("2. Triciclo")
        println("3. Patinete")
        println("4. Trailer")
        println("5. Moto")
        println("6. Furgoneta")
        println("7. Muestra los datos ordenados por el nombre del modelo")
        println("8. Consultar el número de vehículos de cada tipo")
        println("9. Salir")

        print("Ingrese el número correspondiente al tipo de vehículo o 9 para salir: ")
        val opcion = readlnOrNull()?.toIntOrNull()

        if (opcion != null && opcion in 1..9) {
            when (opcion) {
                in 1..6 -> {
                    try {
                        print("Ingrese el número de ruedas: ")
                        val ruedas = readlnOrNull()?.toInt() ?: 0

                        print("Ingrese el tipo de motor: ")
                        val motor = readlnOrNull() ?: ""

                        print("Ingrese el número de asientos: ")
                        val numAsientos = readlnOrNull()?.toInt() ?: 0

                        print("Ingrese el color: ")
                        val color = readlnOrNull() ?: ""

                        print("Ingrese el modelo: ")
                        val modelo = readlnOrNull() ?: ""

                        val vehiculo = when (opcion) {
                            1 -> Vehiculo.Coche(ruedas, motor, numAsientos, color, modelo)
                            2 -> Vehiculo.Triciclo(ruedas, numAsientos, color, modelo)
                            3 -> Vehiculo.Patinete(ruedas, motor, color, modelo)
                            4 -> {
                                print("Ingrese la carga máxima del Trailer: ")
                                val cargaMaxima = readlnOrNull()?.toInt() ?: 0
                                Vehiculo.Trailer(ruedas, motor, numAsientos, color, modelo, cargaMaxima)
                            }

                            5 -> Vehiculo.Moto(ruedas, motor, numAsientos, color, modelo)
                            6 -> {
                                print("Ingrese la carga máxima de la Furgoneta: ")
                                val cargaMaxima = readlnOrNull()?.toInt() ?: 0
                                Vehiculo.Furgoneta(ruedas, motor, numAsientos, color, modelo, cargaMaxima)
                            }

                            else -> null
                        }

                        vehiculo?.mostrarDatos()
                        vehiculos[index++] = vehiculo
                    } catch (e: NumberFormatException) {
                        println("Error: Ingrese un valor numérico válido.")
                    } catch (e: IllegalArgumentException) {
                        println("Error: ${e.message}")
                    }
                }

                7 -> {
                    Vehiculo.listarPorNombre(vehiculos)
                }

                8 -> {
                    println("Consulta de vehículos por tipo:")
                    println("Coches: ${Vehiculo.contarVehiculos(vehiculos, "Coche")}")
                    println("Triciclos: ${Vehiculo.contarVehiculos(vehiculos, "Triciclo")}")
                    println("Patinetes: ${Vehiculo.contarVehiculos(vehiculos, "Patinete")}")
                    println("Trailers: ${Vehiculo.contarVehiculos(vehiculos, "Trailer")}")
                    println("Motos: ${Vehiculo.contarVehiculos(vehiculos, "Moto")}")
                    println("Furgonetas: ${Vehiculo.contarVehiculos(vehiculos, "Furgoneta")}")
                }

                9 -> {
                    println("Programa terminado.")
                    return
                }
            }
        } else {
            println("Opción inválida. Intente nuevamente.")
        }
    }
}

fun readlnOrNull(): String? {
    return readLine()
}
