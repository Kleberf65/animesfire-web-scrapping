package br.kleberf65.scrapping.enums;

public enum VideoQuality {
        sd, hd, fhd, empty;

        public static VideoQuality fromLabel(String label) {
            if (label.equals("360p")) return sd;
            if (label.equals("720p")) return hd;
            if (label.equals("1080p")) return fhd;
            return empty;
        }
    }