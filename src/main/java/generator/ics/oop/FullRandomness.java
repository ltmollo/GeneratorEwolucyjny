package generator.ics.oop;

import java.util.HashMap;
import java.util.Map;

public class FullRandomness extends AbstractGenotype{
    public FullRandomness(Settings settings){
        super(settings);
    }

    public int[] mutation(int[] genotype){
        int nbOfMutations = generateRandomNumber(this.settings.minMutations, this.settings.maxMutations);

        Map<Integer, Integer> gensToChange = new HashMap<>();
        int geneToChange;

        while (nbOfMutations > 0){
            geneToChange = generateRandomNumber(0, this.settings.lengthOfGenotype -1);
            if(!gensToChange.containsKey(geneToChange)){
                gensToChange.put(geneToChange, generateRandomNumber(0, 7));
                nbOfMutations--;
            }
        }
        gensToChange.forEach((index, newGene) ->
                genotype[index] = newGene
        );
        return genotype;
    }
}
