public class Ziekte_variant {
    /**
     * Een object class van de ziekte_varianten die mogelijk zijn
     * @author Jasper Versantvoort
     * Elke ziekte_variant bevat een allelID, Type, Position, pahtogenicity,geneIDref en alternate allele
     * disease en chromosome waar de ziekte op licht
     */
    private int alleleid;
    private String type;
    private int position;
    private String pathogenicity;
    private int geneid;
    private String referanceAllele;
    private String alternateAllele;
    private String phenotypeList; //disease
    private String chromosome;


    public Ziekte_variant(int i) {
        this.alleleid = i;
    }

    public void setType(String n) {
        type = n;
    }

    public void setPosition(int i) {
        position = i;
    }

    public void setPathogenicity(String n) {
        pathogenicity = n;
    }

    public void setGeneid(int i) {
        geneid = i;
    }

    public void setReferanceAllele(String n) {
        referanceAllele = n;
    }

    public void setAlternateAllele(String n) {
        // 2 keer omdat op beide allelen de SNP moet voorkomen
        alternateAllele = n + n;
    }

    public void setPhenotypeList(String n) {
        phenotypeList = n;
    }

    public void setChromosome(String n) {
        chromosome = n;
    }

    public boolean getCompare( String nuc_paar) {
        /**
         * Kijkt of het ingevoerde nuc_paar gelijk is aan de alternateallele wat voor de ziekte zorgt
         * @param nucpaar het nuc paar waar de vergelijking mee wordt gedaan
         * @return boolean (true/false) of het wel of niet overeenkomt met de ziekte
         */
        return getAlternateAllele().equals(nuc_paar);
    }
    public boolean getComparebel(String chroom, int pos) {
        /**
         * Kijkt of het object vergelijkbaar
         * @param chroom is het chromosoom
         * @param pos is de positie van de ziekte op het chromosoom
         * @return boolen (true/false) of er met de ziekte vergeleken kan worden of niet
         */
        return getChromosoom().equals(chroom) &&
                getPosition() == pos;

    }

    public String getChromosoom() {
        return chromosome;
    }
    public String getPhenotypeList() {
        return phenotypeList;
    }
    public int getAlleleid(){
        return alleleid;
    }

    public int getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public String getReferanceAllele() {
        return referanceAllele;
    }

    public String getAlternateAllele() {
        return alternateAllele;
    }

    public String getPathogenicity() {
        return pathogenicity;
    }
    public int getGeneid(){
        return geneid;
    }
    public String toString(){
        /**
         * Geeft de string van het object met alle variabelen
         * @return alle variable van het object
         */
        return getAlleleid() + "\t" + getType() + "\t" + getPathogenicity() + "\t" +getPhenotypeList() + "\t"
                + getChromosoom() + getPosition() + "\t" + getGeneid()+"\t" + getReferanceAllele() +
                "\t" + getAlternateAllele();
    }
}



