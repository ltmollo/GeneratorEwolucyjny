package generator.ics.oop;

import java.util.HashMap;
import java.util.Map;

public class EffortlessAdjustment extends AbstractGenotype { // nieczytelna nazwa

    public EffortlessAdjustment(Settings settings) {
        super(settings);
    }

    public int[] mutation(int[] genotype) {
        int nbOfMutations = generateRandomNumber(this.settings.minMutations, this.settings.maxMutations);
        Map<Integer, Integer> gensToChange = new HashMap<>();
        int geneToChange;
        int newGene;

        while (nbOfMutations > 0) {
            geneToChange = generateRandomNumber(0, this.settings.lengthOfGenotype - 1);
            if (!gensToChange.containsKey(geneToChange)) {

                double upperGene = Math.random();
                if (upperGene <= 0.5) {
                    newGene = (genotype[geneToChange] + 1) % 8;
                } else {
                    newGene = genotype[geneToChange] - 1;
                    if (newGene == -1) {
                        newGene = 7;
                    }
                }
                gensToChange.put(geneToChange, newGene);
                nbOfMutations--;
            }
        }
        gensToChange.forEach((index, gene) ->
                genotype[index] = gene
        );
        return genotype;
    }
}
