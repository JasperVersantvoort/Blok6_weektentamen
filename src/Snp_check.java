import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class Snp_check {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        /**
         * Dit script Onderzoekt welke ziektes persoon 9399 van https://opensnp.org/openhumans met persoon 9637, 9978,
         * 10012 en 10068 kan doorgeven aan hun kinderen terhand van SNP's. De ziektes komen van variant_summary.txt
         * Wanneer je dit bestand nog niet gedownload of wanneer deze outdated is kan je deze downloaden.
         * @auteur Jasper Versantvoort
         * @Date 28-01-2021
         *
         */

        String ouder1 = "9399.23andme.8323";
        String ouder2 = "9637.23andme.8201";
        String ouder3 = "10012.23andme.8276";
        String ouder4 = "10068.23andme.8328";


        //variant_checker();
        Scanner sc = new Scanner(System.in);
        System.out.println("Wil je het nieuwste bestand downloaden? (30min) y/n: ");
        String antwoord = sc.nextLine();
        if (antwoord.equals("y")) {
            System.out.println("gaan we doen");
            Download_variant_sum();
        }
        System.out.println("begin oudercheck 1 en 2");
        ouder_check(ouder1, ouder2);
        System.out.println("begin oudercheck 1 en 3");
        ouder_check(ouder1, ouder3);
        System.out.println("begin oudercheck 1 en 4");
        ouder_check(ouder1, ouder4);

    }

    public static void Download_variant_sum() {
        /**
         * Het nieuwste variant_summary.txt.gz wordt binnen gehaald van
         * ftp://ftp.ncbi.nlm.nih.gov/pub/clinvar/tab_delimited/
         * Het bijbehoordende Md5 bestand woordt ook binnengehaald voor vergelijking. Daarna wordt de variant_summary
         * Uitgepakt met de funtie unzip. Daarna wordt het bestand vergeleken met de md5 via de functie variant_checker
         *
         * @Return het variant_summary.txt, variant_summary.txt.gz.md5 bestand en een text of het overeenkomt met de md5.
         */
        try (BufferedInputStream in_var = new BufferedInputStream(new URL("ftp://ftp.ncbi.nlm.nih.gov/pub/clinvar/tab_delimited/variant_summary.txt.gz").openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("variant_summary.txt.gz")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in_var.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            try (BufferedInputStream in_md5 = new BufferedInputStream(new URL("ftp://ftp.ncbi.nlm.nih.gov/pub/clinvar/tab_delimited/variant_summary.txt.gz.md5").openStream());
                 FileOutputStream fileOutputStream_md5 = new FileOutputStream("variant_summary.txt.gz.md5")) {
                byte[] dataBuffer_md5 = new byte[1024];
                int bytesRead_md5;
                while ((bytesRead_md5 = in_md5.read(dataBuffer_md5, 0, 1024)) != -1) {
                    fileOutputStream_md5.write(dataBuffer_md5, 0, bytesRead_md5);
                }
                FileInputStream gzip = new FileInputStream("variant_summary.txt.gz");
                FileOutputStream output = new FileOutputStream("variant_summary.txt");
                unzip(new GZIPInputStream(gzip), output);
            } catch (IOException e) {
                System.out.println("foute URL md5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("foute URL variant");
        }

    }

    public static void unzip(InputStream input, OutputStream output) throws IOException, NoSuchAlgorithmException {
        /**
         * Deze functie unzipt het bestand
         * @param input bestand file
         * @param output bestands_naam na uitpakken
         * @throws IOException wanneer hij het bestand niet kan vinden
         * @throws NoSuchAlgorithmException als hij algortime niet herkend
         * @see http://www.avajava.com/tutorials/lessons/how-do-i-compress-and-uncompress-a-gzip-file.html
         */

        System.out.println("start unzip");
        int oneByte;
        while ((oneByte = input.read()) != -1) {
            output.write(oneByte);
        }
        output.close();
        input.close();
        variant_checker();
    }

    public static void variant_checker() throws IOException, NoSuchAlgorithmException {

        /**
         * Deze functie kijkt of de variant_summary.txt file overeenkomt met de md5 uit het md5 bestand.
         * Hij berekend de md5 van variant_summary.txt met de functie getFileChecksum
         * @throws IOException wanneer hij het bestand niet kan vinden
         * @throws NoSuchAlgorithmException als hij algortime niet herkend
         * @see https://howtodoinjava.com/java/io/sha-md5-file-checksum-hash/
         * @return print of md5 gelijk of niet gelijk is
         */
        File file = new File("variant_summary.txt");
        String variant_summary_md5 = "variant_summary.txt.gz (2).md5";


        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        String checksum = getFileChecksum(md5Digest, file);

        System.out.println(checksum);

        BufferedReader inFile_variant_summary_md5 = new BufferedReader(new FileReader(variant_summary_md5));
        String line_sum_md5;
        while ((line_sum_md5 = inFile_variant_summary_md5.readLine()) != null) {
            System.out.println(line_sum_md5);
            if (checksum.equals(line_sum_md5.substring(0, checksum.length()))) {
                System.out.println("gelijk");
            } else {
                System.out.println("Niet gelijk");
            }

        }
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        /**
         * berekend de md5 code uit een bestand
         * @param digest md5digest van het bestand
         * @param File is het bestand waar je de md5 van wilt hebben
         * @see https://howtodoinjava.com/java/io/sha-md5-file-checksum-hash/
         * @return de md5 van het bestand
         */

        FileInputStream fis = new FileInputStream(file);

        byte[] byteArray = new byte[1024];
        int bytesCount;

        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        fis.close();

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void ouder_check(String ouderA, String ouderB) throws IOException {
        /**
         * Deze functie onderzoekt welke SNP ziektes ouderA en ouderB samen kunnen krijgen.
         * @param ouderA is de naam/path van het bestand van ouderA
         * @param ouderB is de naam/path van het bestand van ouderB
         * @throws IOException Wanneer een van de bestanden niet gevonden wordt
         * @return een tab-delimited file met daarin de het rsid, de nt combinatie, het chromosoom
         * nummer, de nt's van beide ouders en de id's van de ouders
         */

        HashMap<String, Ziekte_variant> varianten = Ziekte_variant_inchecker();
        //System.out.println(varianten);
        String bestand_naam = "";

        BufferedReader inFile_ouderA = new BufferedReader(new FileReader(ouderA));
        BufferedReader inFile_ouderB = new BufferedReader(new FileReader(ouderB));
        StringBuilder bestand_text = new StringBuilder();
        //File bestand = new File("Gevonden_ziektes.txt");
        FileWriter fw;


        String lineA;
        String lineB;
        HashMap<String, String> rs_hashmapA = new HashMap<>();
        while ((lineA = inFile_ouderA.readLine()) != null) {
            if (lineA.charAt(0) != '#') {
                rs_hashmapA.put(lineA.split("\t")[0], lineA);
            }
        }
        while ((lineB = inFile_ouderB.readLine()) != null) {

            String nuc_paar_ziek = "None";
            if (lineB.charAt(0) != '#') {
                String hashA_get = rs_hashmapA.get(lineB.split("\t")[0]);
                if (hashA_get != null && !hashA_get.contains("-") && !lineB.contains("-")) {
                    if (hashA_get.split("\t")[3].length() == 2 && lineB.split("\t")[3].length() == 2) {
                        //System.out.println(hashA_get);

                        char nucA1 = hashA_get.split("\t")[3].charAt(0);
                        char nucA2 = hashA_get.split("\t")[3].charAt(1);

                        char nucB1 = lineB.split("\t")[3].charAt(0);
                        char nucB2 = lineB.split("\t")[3].charAt(1);

                        if (nucA1 != nucA2 && nucB1 != nucB2) {
                            if (nucA1 == nucB1) {
                                nuc_paar_ziek = Ziekte_check(nucA1, nucB1, hashA_get, varianten);
                            }
                            if (nucA1 == nucB2) {
                                nuc_paar_ziek = Ziekte_check(nucA1, nucB2, hashA_get, varianten);
                            }
                            if (nucA2 == nucB1) {
                                nuc_paar_ziek = Ziekte_check(nucA2, nucB1, hashA_get, varianten);
                            }
                            if (nucA2 == nucB2) {
                                nuc_paar_ziek = Ziekte_check(nucA2, nucB2, hashA_get, varianten);
                            }

                        } else if (nucA1 == nucB1) {
                            nuc_paar_ziek = Ziekte_check(nucA1, nucB1, hashA_get, varianten);
                        } else if (nucA2 == nucB1) {
                            nuc_paar_ziek = Ziekte_check(nucA2, nucB1, hashA_get, varianten);
                        } else if (nucA2 == nucB2) {
                            nuc_paar_ziek = Ziekte_check(nucA2, nucB2, hashA_get, varianten);
                        }
                        if (!nuc_paar_ziek.equals("None")) {
                            String temp = hashA_get.split("\t")[0] + "\t" +
                                    nuc_paar_ziek + "\t" +
                                    hashA_get.split("\t")[1] + "\t" +
                                    hashA_get.split("\t")[3] + "\t" +
                                    lineB.split("\t")[3] + "\t" +
                                    ouderA.substring(0, 5) + "\t" +
                                    ouderB.substring(0, 5) + "\n";
                            bestand_text.append(temp);
                            if (bestand_naam.equals("")) {
                                bestand_naam = "Gevonden_ziektes_" +
                                        ouderA.substring(0, 5).replace(".", "") +
                                        "_" + ouderB.substring(0, 5).replace(".", "") + ".txt";
                            }

                        }
                    }
                }
            }
        }
        try {
            fw = new FileWriter(new File(bestand_naam));
            fw.write(String.valueOf(bestand_text));
            fw.write(System.lineSeparator()); //new line

            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static String Ziekte_check(char nuc1, char nuc2, String hashA_get,
                                      HashMap<String, Ziekte_variant> varianten) {
        /**
         * Kijkt of een nucleotide paar op het aangegeven chromosoom en positie voor een ziekte kunnen zorgen
         * @param nuc1 nucleotide van ouder1
         * @param nuc2 nucleotide van ouder2
         * @param hashA_get op tab gescheiden string rsid, chromosome, position, genotype
         * @param varianten een Hashmap met daar in alle mogelijke ziektes met als hashkey chroomosoom-positie
         * @return de combinatie die voor de ziekte zorgt of None als er geen ziekte is.
         */
        String nuc_paar = new String(new char[]{nuc1, nuc2});

        String hash_code = hashA_get.split("\t")[1] + "-" + hashA_get.split("\t")[2];
        if (varianten.containsKey(hash_code)) {
            if (varianten.get(hash_code).getCompare(nuc_paar)) {
                return nuc_paar;
            } else {
                return "None";
            }
        } else {
            return "None";
        }


    }


    public static HashMap<String, Ziekte_variant> Ziekte_variant_inchecker() throws IOException {
        /**
         * een functie die alle ziekte varianten uit variant_summary.txt als object aanmaakt met de juiste onderdelen
         * en daarna de SNP ziektes die niet 'benign' zijn in een hashmap zet met als hashkey chromosoom-positie
         * om zo voor iedere ziekte een apparte key te hebben.
         * @throws IOException wanneer er geen bestand gevonden wordt.
         * @return varianten --> een hashmap met als hashkey chromosoom-positie en als value het object van de
         * bijbehorende ziekte_variant
         */
        String lineA;

        HashMap<String, Ziekte_variant> varianten = new HashMap<>();


        BufferedReader variant_summary = new BufferedReader(new FileReader("variant_summary.txt"));
        String[] header = variant_summary.readLine().split("\t");
        System.out.println(Arrays.toString(header));
        while ((lineA = variant_summary.readLine()) != null) {
            if (lineA.charAt(0) != '#') {
                Ziekte_variant variant = new Ziekte_variant((Integer.parseInt(lineA.split("\t")[0])));

                variant.setType(lineA.split("\t")[java.util.Arrays.asList(header).indexOf("Type")]);
                variant.setPosition(Integer.parseInt(
                        lineA.split("\t")[java.util.Arrays.asList(header).indexOf("PositionVCF")]));
                variant.setPathogenicity(
                        lineA.split("\t")[java.util.Arrays.asList(header).indexOf("ClinicalSignificance")]);
                variant.setGeneid(Integer.parseInt(
                        lineA.split("\t")[java.util.Arrays.asList(header).indexOf("GeneID")]));
                variant.setReferanceAllele(
                        lineA.split("\t")[java.util.Arrays.asList(header).indexOf("ReferenceAlleleVCF")]);
                variant.setAlternateAllele(
                        lineA.split("\t")[java.util.Arrays.asList(header).indexOf("AlternateAlleleVCF")]);
                variant.setPhenotypeList(
                        lineA.split("\t")[java.util.Arrays.asList(header).indexOf("PhenotypeList")]);
                variant.setChromosome(lineA.split("\t")[java.util.Arrays.asList(header).indexOf("Chromosome")]);

                if (variant.getType().equals("single nucleotide variant") &&
                        !variant.getPathogenicity().equals("Benign")) {

                    String hash_input = variant.getChromosoom() + "-" + variant.getPosition();
                    varianten.put(hash_input, variant);
                }

            }
        }
        variant_summary.close();
        return varianten;
    }

}





