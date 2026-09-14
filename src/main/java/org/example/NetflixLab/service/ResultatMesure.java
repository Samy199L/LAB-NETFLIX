package org.example.NetflixLab.service;

public class ResultatMesure {

        private final String nomAlgorithme;
        private final String complexiteTheorique;
        private final long dureeMillisecondes;

        public ResultatMesure(String nomAlgorithme, String complexiteTheorique, long dureeMillisecondes) {
            this.nomAlgorithme = nomAlgorithme;
            this.complexiteTheorique = complexiteTheorique;
            this.dureeMillisecondes = dureeMillisecondes;
        }

        public String getNomAlgorithme() { return nomAlgorithme; }
        public String getComplexiteTheorique() { return complexiteTheorique; }
        public long getDureeMillisecondes() { return dureeMillisecondes; }

    @Override
    public String toString() {
        return "resultatMesure{" +
                "nomAlgorithme='" + nomAlgorithme + '\'' +
                ", complexiteTheorique='" + complexiteTheorique + '\'' +
                ", dureeMillisecondes=" + dureeMillisecondes +
                '}';
    }
}

