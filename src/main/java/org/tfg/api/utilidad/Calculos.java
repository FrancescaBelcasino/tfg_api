package org.tfg.api.utilidad;

import java.util.List;

public class Calculos {
    private static final double R = 6378137;

    public static double calcularAreaEnMetrosCuadrados(List<List<Double>> coordenadas) {
        double area = 0.0;

        for (int i = 0; i < coordenadas.size(); i++) {
            List<Double> p1 = coordenadas.get(i);
            List<Double> p2 = coordenadas.get((i + 1) % coordenadas.size());

            double lon1 = Math.toRadians(p1.get(1));
            double lat1 = Math.toRadians(p1.get(0));
            double lon2 = Math.toRadians(p2.get(1));
            double lat2 = Math.toRadians(p2.get(0));

            area += (lon2 - lon1) * (2 + Math.sin(lat1) + Math.sin(lat2));
        }

        area = area * R * R / 2.0;
        return Math.abs(area);
    }

    public static double convertirMetrosCuadradosAHectareas(double metrosCuadrados) {
        return metrosCuadrados / 10_000.0;
    }

    public static Superficie calcularSuperficie(List<List<Double>> coordenadas) {
        double m2 = calcularAreaEnMetrosCuadrados(coordenadas);
        double ha = convertirMetrosCuadradosAHectareas(m2);

        double metrosCuadradosRedondeados = Math.round(m2);
        double hectareasRedondeadas = Math.round(ha);

        return new Superficie(metrosCuadradosRedondeados, hectareasRedondeadas);
    }

    public record Superficie(double metrosCuadrados, double hectareas) {}
}