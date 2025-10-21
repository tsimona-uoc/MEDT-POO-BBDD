package MEDT.MEDT.vista;

import MEDT.MEDT.controlador.Controlador;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Vista {
    private final Scanner sc = new Scanner(System.in);
    private final Controlador controlador = new Controlador();

    public void menu() {
        int opcion;
        do {
            System.out.println("===== MENÚ PRINCIPAL =====");
            System.out.println("1. Gestión de Artículos");
            System.out.println("2. Gestión de Clientes");
            System.out.println("3. Gestión de Pedidos");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> menuArticulos();
                case 2 -> menuClientes();
                case 3 -> menuPedidos();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void menuArticulos() {
        System.out.println("--Gestor de Artículos--");
        System.out.println("1. Añadir Artículo");
        System.out.println("2. Mostrar Artículos");
        System.out.print("Elige opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        switch (opcion) {
            case 1 -> {
                System.out.print("Codigo: ");
                String codigo = sc.nextLine();
                System.out.print("Descripción: ");
                String descripcion = sc.nextLine();
                System.out.print("Precio: ");
                double precio = Double.parseDouble(sc.nextLine());
                System.out.print("Tiempo de preparación (min): ");
                double gastosEnvio = Double.parseDouble(sc.nextLine());
                System.out.print("Gastos de envío: ");
                int tiempoPrep = Integer.parseInt(sc.nextLine());
                controlador.addArticulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
                System.out.println("Artículo añadido correctamente.");
            }
            case 2 -> controlador.getArticulos().forEach(System.out::println);
        }
    }

    private void menuClientes() {
        // similar a artículos (añadir, mostrar, etc.)
    }

    private void menuPedidos() {
        System.out.println("--Gestor de pedidos--");
        System.out.println("1. Añadir pedidos");
        System.out.println("2. Eliminar pedido");
        System.out.println("3. Mostrar pedidos pendientes de envío");
        System.out.println("4. Mostrar pedidos enviados");
        System.out.println("Elige opción: ");
        int opcion = sc.nextInt();
        switch(opcion){
            case 1 -> addPedido();
            case 2 -> eliminarPedido();
            case 3 -> mostrarPedidosPendientes();
            case 4 -> mostrarPedidosEnviados();
            case 0 -> System.out.println("Saliendo del gestor de pedidos...");
            default -> System.out.println("La opción no es válida");
        }
    }
    private void addPedido(){
        System.out.print("Número del pedido: ");
        int numPedido = sc.nextInt();
        sc.nextLine();
        System.out.print("Cantidad de unidades: ");
        int cantidad = sc.nextInt();
        sc.nextLine();
        System.out.print("Fecha de la creación del pedido (yyyy-MM-dd): ");
        String fecha_texto = sc.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fecha = LocalDate.parse(fecha_texto, formatter);
        LocalDateTime fechaHora = fecha.atStartOfDay();

        String codigo_articulo;
        while (true){
            System.out.print("Artículo, código: ");
            codigo_articulo = sc.nextLine();

            if (controlador.buscarArticulo(codigo_articulo)) {
                break;
            }
            System.out.println("El artículo no existe, introduzca otro código o pulse 0 para terminar: ");
            String opcion = sc.nextLine();
            if (opcion.equals("0")){
                return;
            }
            if (controlador.buscarArticulo(opcion)){
                codigo_articulo = opcion;
                break;
            }
        }
        Articulo articulo;
            try{
                articulo = controlador.getArticulo(codigo_articulo);
            } catch (ArticuloNoEncontradoException e){
                System.out.println(e.getMessage());
                return;
            }

        sc.skip("\\R");
        System.out.print("Cliente, nif: ");
        String nif = sc.nextLine().trim();
        Cliente cliente;
        if(controlador.buscarCliente(nif)){
            cliente = controlador.getCliente(nif);
        }else{
            System.out.println("El cliente no existe. Se debe crear un cliente antes de continuar: ");
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Domicilio: ");
            String domicilio = sc.nextLine();
            System.out.print("NIF: ");
            nif = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            int tipo_cliente;
            do{
                System.out.println("Indique el tipo de cliente: 1-Estandar, 2-Premium");
                tipo_cliente = sc.nextInt();
                sc.nextLine();
                if(tipo_cliente == 1){
                    controlador.addClienteEstandar(nombre, domicilio, nif, email);
                }else if(tipo_cliente == 2){
                    controlador.addClientePremium(nombre, domicilio, nif, email);
                }
            }while (tipo_cliente != 1 && tipo_cliente != 2);
            cliente = controlador.getCliente(nif);
        }
        controlador.addPedido(numPedido, cantidad, fechaHora, articulo, cliente);
        System.out.println("Pedido añadido correctamente");
    }
    private void eliminarPedido(){
        System.out.print("Numero del pedido a eliminar: ");
        int numero_pedido = sc.nextInt();
        if(!controlador.buscarPedido(numero_pedido)) {
            System.out.println("El pedido se encontró...");
            return;
        }
        if(!controlador.pedidoEliminable(numero_pedido)){
            System.out.println("El fue enviado y no se puede eliminar");
        }
        if(controlador.eliminarPedido(numero_pedido)){
            System.out.println("Pedido eliminado correctamente.");
        }else{
            System.out.println("No se ha podido eliminar el pedido (error inesperado)");
        }
    }
    private void mostrarPedidosPendientes(){
        System.out.print("¿Cómo desea mostrar los pedidos pendientes de envío? 1-Filtrar Por cliente, 2-Mostrar todos: ");
        int opcion = sc.nextInt();
        sc.nextLine();

        List<Pedido> lista;

        if(opcion == 1){
            System.out.print("Indique el NIF del cliente: ");
            String nif = sc.nextLine();
            lista = controlador.getPedidosPendientesCliente(nif);

        }else if(opcion == 2){
            lista = controlador.getPedidosPendientes();
        }else{
            System.out.println("Opcion no válida");
            return;
        }
        if(lista.isEmpty()){
            System.out.println("No hay pedidos pendientes para mostrar");
        }else{
            for(Pedido pedido : lista){
                System.out.println(pedido);
            }
        }
    }
    private void mostrarPedidosEnviados(){
        System.out.print("¿Cómo desea mostrar los pedidos enviados? 1-Filtrar Por cliente, 2-Mostrar todos: ");
        int opcion = sc.nextInt();
        sc.nextLine();

        List<Pedido> lista;

        if(opcion == 1) {
            System.out.print("Indique el NIF del cliente: ");
            String nif = sc.nextLine();
            lista = controlador.getPedidosEnviadosCliente(nif);
        }else if(opcion == 2){
            lista = controlador.getPedidosEnviados();
        }else {
            System.out.println("Opción no válida");
            return;
        }
        if(lista.isEmpty()){
            System.out.println("No hay pedidos enviados para mostrar");
        }else{
            for(Pedido pedido : lista){
                System.out.println(pedido);
            }
        }
    }
//    private static Scanner getScanner() {
//        return sc;
//    }
}

