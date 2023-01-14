package generator.ics.oop;

public class FullPredestination implements IAnimalBehaviour {

    protected final int lengthOfGenotype;

    public FullPredestination(int lengthOfGenotype) {
        this.lengthOfGenotype = lengthOfGenotype;
    }

    @Override
    public int nextGen(int latestGene) {
        return (latestGene + 1) % this.lengthOfGenotype;
    }
}
