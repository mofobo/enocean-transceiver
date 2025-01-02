package ch.mofobo

import purejavacomm.CommPortIdentifier
import purejavacomm.SerialPort
import purejavacomm.SerialPortEvent
import purejavacomm.SerialPortEventListener
import java.io.InputStream

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please provide the port name as an argument.")
        return
    }
    val portName = args[0] // Get the port name from the command-line arguments
    val portId = CommPortIdentifier.getPortIdentifier(portName)
    val port = portId.open("EnoceanReader", 2000) as SerialPort

    port.setSerialPortParams(
        57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE
    )

    // Clear the buffer before starting to listen
    clearSerialPortBuffer(port)
    port.addEventListener(object : SerialPortEventListener {
        override fun serialEvent(event: SerialPortEvent) {
            if (event.eventType == SerialPortEvent.DATA_AVAILABLE) {
                val inputStream: InputStream = port.inputStream
                val buffer = ByteArray(1024)
                val len = inputStream.read(buffer)
                if (len > 0) {
                    println("Received: ${buffer.copyOf(len).joinToString(" ") { "%02X".format(it) }}")
                }
            }
        }
    })
    port.notifyOnDataAvailable(true)

    // Prevent program from exiting
    Thread.currentThread().join()
}

fun clearSerialPortBuffer(serialPort: SerialPort) {
    val inputStream: InputStream = serialPort.inputStream
    val buffer = ByteArray(1024)
    while (inputStream.available() > 0) {
        inputStream.read(buffer)
    }
}
