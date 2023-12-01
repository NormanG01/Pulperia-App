import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PulperiaApp {
    private static final String ARCHIVO_INVENTARIO = "inventario.txt";
    private static final String ARCHIVO_FACTURAS = "facturas.txt";

    private static Scanner scanner = new Scanner(System.in);
    private static List<Producto> inventario = new ArrayList<>();

    public static void main(String[] args) {
        cargarInventario();

        while (true) {
            System.out.println("\n--- Pulperia App ---");
            System.out.println("1. Administrar Inventario");
            System.out.println("2. Realizar Venta");
            System.out.println("3. Mostrar Facturas");
            System.out.println("4. Salir");

            int opcion = getIntInput("Ingrese la opción deseada: ");

            switch (opcion) {
                case 1:
                    administrarInventario();
                    break;
                case 2:
                    realizarVenta();
                    break;
                case 3:
                    mostrarFacturas();
                    break;
                case 4:
                    guardarInventario();
                    System.out.println("Gracias por usar Pulperia App. ¡Hasta luego!");
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Por favor, ingrese una opción válida.");
            }
        }
    }

    private static void cargarInventario() {
        try {
            FileReader fr = new FileReader(ARCHIVO_INVENTARIO);
            BufferedReader br = new BufferedReader(fr);

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Producto producto = new Producto(datos[0], Double.parseDouble(datos[1]), Integer.parseInt(datos[2]));
                inventario.add(producto);
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Error al cargar el inventario: " + e.getMessage());
        }
    }

    private static void administrarInventario() {
        while (true) {
            System.out.println("\n--- Administrar Inventario ---");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Mostrar Inventario");
            System.out.println("3. Volver al Menú Principal");

            int opcion = getIntInput("Ingrese la opción deseada: ");

            switch (opcion) {
                case 1:
                    agregarProducto();
                    break;
                case 2:
                    mostrarInventario();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, ingrese una opción válida.");
            }
        }
    }

    private static void agregarProducto() {
        try {
            FileWriter fw = new FileWriter(ARCHIVO_INVENTARIO, true);
            BufferedWriter bw = new BufferedWriter(fw);

            String nombre = getStringInput("Ingrese el nombre del producto: ");
            double precio = getDoubleInput("Ingrese el precio del producto: ");
            int cantidad = getIntInput("Ingrese la cantidad del producto: ");

            if (nombre.isEmpty() || precio <= 0 || cantidad <= 0) {
                System.out.println("Por favor, ingrese valores válidos.");
                return;
            }

            Producto nuevoProducto = new Producto(nombre, precio, cantidad);
            inventario.add(nuevoProducto);

            bw.write(nombre + ";" + precio + ";" + cantidad);
            bw.newLine();
            bw.close();

            System.out.println("Producto agregado correctamente al inventario.");
        } catch (IOException e) {
            System.out.println("Error al agregar el producto: " + e.getMessage());
        }
    }

    private static void mostrarInventario() {
        System.out.println("\n--- Inventario ---");
        System.out.println("Nombre\t\tPrecio\t\tCantidad");

        for (Producto producto : inventario) {
            System.out.println(producto.getNombre() + "\t\t" + producto.getPrecio() + "\t\t" + producto.getCantidad());
        }
    }

    private static void realizarVenta() {
        try {
            System.out.println("\n--- Realizar Venta ---");

            mostrarInventario();

            String nombreProducto = getStringInput("Ingrese el nombre del producto a vender: ");

            Producto productoAVender = null;
            for (Producto producto : inventario) {
                if (producto.getNombre().equalsIgnoreCase(nombreProducto)) {
                    productoAVender = producto;
                    break;
                }
            }

            if (productoAVender == null) {
                System.out.println("Producto no encontrado en el inventario.");
                return;
            }

            int cantidadAVender = getIntInput("Ingrese la cantidad del producto a vender: ");

            if (cantidadAVender <= 0 || cantidadAVender > productoAVender.getCantidad()) {
                System.out.println("Cantidad no válida. Por favor, ingrese una cantidad válida.");
                return;
            }

            productoAVender.setCantidad(productoAVender.getCantidad() - cantidadAVender);

            FileWriter facturaFw = new FileWriter(ARCHIVO_FACTURAS, true);
            BufferedWriter facturaBw = new BufferedWriter(facturaFw);

            double totalVenta = cantidadAVender * productoAVender.getPrecio();
            String factura = String.format("%s - %d unidades - Total: %.2f", productoAVender.getNombre(),
                    cantidadAVender, totalVenta);

            facturaBw.write(factura);
            facturaBw.newLine();
            facturaBw.close();

            System.out.println("Venta realizada correctamente.");

        } catch (IOException e) {
            System.out.println("Error al realizar la venta: " + e.getMessage());
        }

    }

    private static void mostrarFacturas() {

        try {
            FileReader fr = new FileReader(ARCHIVO_FACTURAS);
            BufferedReader br = new BufferedReader(fr);

            System.out.println("\n--- Facturas ---");

            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Error al mostrar las facturas: " + e.getMessage());
        }
    }

    private static int getIntInput(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int input = Integer.parseInt(scanner.nextLine());
                if (input > 0) {
                    return input;
                } else {
                    System.out.println("Por favor, ingrese un número entero positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número entero válido.");
            }
        }
    }

    private static double getDoubleInput(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double input = Double.parseDouble(scanner.nextLine());
                if (input > 0) {
                    return input;
                } else {
                    System.out.println("Por favor, ingrese un número positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
    }

    private static String getStringInput(String mensaje) {
        System.out.print(mensaje);
        String input = scanner.nextLine().trim();
        return input;
    }

    private static void guardarInventario() {
        try {
            FileWriter fw = new FileWriter(ARCHIVO_INVENTARIO);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Producto producto : inventario) {
                bw.write(producto.getNombre() + ";" + producto.getPrecio() + ";" + producto.getCantidad());
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar el inventario: " + e.getMessage());
        }
    }
}