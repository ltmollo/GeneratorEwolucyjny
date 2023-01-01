package generator.ics.oop;

import org.testng.internal.collections.Pair;

import java.util.ArrayList;
import java.util.List;


public class ToxicCorpse extends AbstractPlantGrow{

    public int index = 0;
    private final List<Pair<Vector2d, Integer>> deadAnimals = new ArrayList<>();

    public ToxicCorpse(Jungle jungle, Vector2d lowerLeftBoarder, Vector2d upperRightBoarder){
        super(lowerLeftBoarder, upperRightBoarder);

        for(int i = lowerLeftBoarder.x; i <= upperRightBoarder.x; i++){
            for(int j= lowerLeftBoarder.y; j <= upperRightBoarder.y; j++){
                Pair<Vector2d, Integer> place = new Pair<>(new Vector2d(i, j), 0);
                deadAnimals.add(place);         // No dead animals at the beginning
            }
        }
    }

    @Override
    public Vector2d placeGrass(boolean placeInTheJungle, boolean canPlaceInSteppe) {
        if(!placeInTheJungle && !canPlaceInSteppe){
            return null;
        }
        if(!placeOnThePrefer()){
            return super.placeGrass(placeInTheJungle, canPlaceInSteppe);
        }
        return this.deadAnimals.get(index).first();
    }

    public void addDeadAnimal(Vector2d position){
        for(int nbOfPair = 0; nbOfPair < this.deadAnimals.size(); nbOfPair++){
            if(this.deadAnimals.get(nbOfPair).first().equals(position)){
                Integer newDead = this.deadAnimals.get(nbOfPair).second();
                newDead++;
                this.deadAnimals.remove(nbOfPair);
                Pair<Vector2d, Integer> newPair = new Pair<>(position, newDead);
                addAtCorrectIndex(newPair);
                break;
            }
        }
        System.out.println(deadAnimals);
    }

    private void addAtCorrectIndex(Pair<Vector2d, Integer> pair){

        int min = 0;
        int max = this.deadAnimals.size()-1;
        int mid = (max+min)/2;
        while(min <= max){
            mid = (max+min)/2;
            if(this.deadAnimals.get(mid).second() <= pair.second() && mid + 1 < deadAnimals.size() && pair.second() <= deadAnimals.get(mid+1).second()){
                break;
            }
            if(this.deadAnimals.get(mid).second() > pair.second()){
                max = mid-1;
            }
            if(this.deadAnimals.get(mid).second() < pair.second()){
                min = mid+1;
            }
        }
        if(max == -1){      // add at the beginning
            mid = -1;
        }
        this.deadAnimals.add(mid+1, pair);
    }

    public void restartIndex(){
        this.index = 0;
    }

    public void increaseIndex(){
        this.index++;
    }

}
