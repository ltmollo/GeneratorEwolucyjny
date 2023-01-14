package generator.ics.oop;

public class SomeMadness extends FullPredestination {

    public SomeMadness(int lengthOfGenotype) {
        super(lengthOfGenotype);
    }

    private boolean blendNextGene() {
        int blend = (int) (Math.random() * 101);
        return blend > 20;
    }


    @Override
    public int nextGen(int latestGene) {
        if (blendNextGene()) {
            return super.nextGen(latestGene);
        }
        int newGene = (int) (Math.random() * this.lengthOfGenotype);
        return newGene;
    }
}
