package generator.ics.oop;

public abstract class AbstractGenotype implements IGenotype{

    Settings settings;

    AbstractGenotype(Settings settings){
        this.settings = settings;
    }

    private int section() {
        double odds = Math.random();
        if(odds < 0.5){
            return 0;
        }
        return 1;
    }

    public int generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max+1 - min)) + min);
    }

    private double divide(int energy1, int energy2) {
        return (double) energy1/(energy1 + energy2);
    }

    @Override
    public abstract int[] mutation(int[] genotype);

    @Override
    public int[] createGenotype(Animal animal1, Animal animal2) {
        int[] newGenotype = new int[this.settings.lengthOfGenotype];
        int nbOfGeneFromFirst =(int) Math.ceil(this.settings.lengthOfGenotype *(divide(animal1.getEnergy(), animal2.getEnergy())));
        int checkBeginning = section();
        int geneToAdd;
        if(checkBeginning == 0){
            for(geneToAdd = 0; geneToAdd < nbOfGeneFromFirst; geneToAdd++){
                newGenotype[geneToAdd] = animal1.getGenotype()[geneToAdd];
            }
            for(int i = geneToAdd; i < this.settings.lengthOfGenotype; i++){
                newGenotype[i] = animal2.getGenotype()[i];
            }
        }
        else{
            for(geneToAdd = this.settings.lengthOfGenotype -nbOfGeneFromFirst; geneToAdd < this.settings.lengthOfGenotype; geneToAdd++){
                newGenotype[geneToAdd] = animal1.getGenotype()[geneToAdd];
            }
            for(int i = 0; i < this.settings.lengthOfGenotype - nbOfGeneFromFirst; i++){
                newGenotype[i] = animal2.getGenotype()[i];
            }
        }
        return mutation(newGenotype);
    }

    public int[] createRandomGenotype(){                            // for the begging of simulation
        int[] newGenotype = new int[this.settings.lengthOfGenotype];
        for(int i = 0; i < this.settings.lengthOfGenotype; i++){
            newGenotype[i] = generateRandomNumber(0, 7);
        }
        return newGenotype;
    }
}
