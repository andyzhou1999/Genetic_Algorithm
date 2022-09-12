import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

class City{

    int x;
    int y;
    int z;

    City(){
        x = 0;
        y = 0;
        z = 0;
    }

    City(int x, int y, int z){

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString(){

        return x + " " + y + " " + z;
    }
}

class Path{

    List<City> path;
    double score;

    Path(){

        path = new ArrayList<>();
        score = 0;
    }

    public void addCity(City c){

        if (path.isEmpty()){

            path.add(c);
        }
        else{

            City prev = path.get(path.size() - 1);
            path.add(c);
            score += Math.sqrt(Math.pow(prev.x - c.x, 2) + Math.pow(prev.y - c.y, 2) + Math.pow(prev.z - c.z, 2));
        }
    }
}

class Pair implements Comparable{

    List<Integer> path;
    double score;

    Pair(){

    }

    Pair(List<Integer> p, double s){

        path = p;
        score = s;
    }


    @Override
    public int compareTo(Object o) {

        Pair rhs = (Pair) o;
        if (this.score < rhs.score){

            return -1;
        }
        else if (this.score > rhs.score){

            return 1;
        }
        else{

            return 0;
        }
    }
}

class TSP{

    Random random = new Random(10);
    List<City> cities;
    List<Pair> population;

    TSP(){

        Scanner scan = null;
        cities = new ArrayList<>();
        try {
            scan = new Scanner(new File("input1.txt"));

            int numCity = Integer.parseInt(scan.nextLine());

            while (numCity-- > 0){

                cities.add(new City(scan.nextInt(), scan.nextInt(), scan.nextInt()));
            }

            population = initializePopulation(10);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{

            if (scan != null){

                scan.close();
            }
        }
    }

    public void outputPath(){

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("output.txt"));
            List<City> path = getPath();
            for (City c : path){
                pw.println(c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{

            if (pw != null){

                pw.close();
            }
        }
    }

    public void approximate(){

        int generation = 1000;
        double threshold = 0.5;

        while (generation-- > 0){

            Collections.sort(population);

        }
    }

    public

    public Pair crossover(Pair p1, Pair p2, int start, int end){

        Integer[] child = new Integer[p1.path.size()];
        Set<Integer> visited = new HashSet<>();

        for (int i = start; i <= end; i++){

            child[i] = p1.path.get(i);
            visited.add(p1.path.get(i));
        }

        for (int i = 0; i < start; i++){

            for (int j : p2.path){

                if (!visited.contains(j)){

                    child[i] = j;
                    visited.add(j);
                    break;
                }
            }
        }

        for (int i = end + 1; i < p1.path.size(); i++){

            for (int j : p2.path){

                if (!visited.contains(j)){

                    child[i] = j;
                    visited.add(j);
                    break;
                }
            }
        }

        List<Integer> _child = Arrays.asList(child);
        return new Pair(_child, getScore(_child));

    }

    public double getScore(List<Integer> path){

        double score = 0;
        int i = 1;
        for (; i < path.size(); i++){

            City prev = cities.get(path.get(i - 1));
            City curr = cities.get(path.get(i));

            score += Math.sqrt(Math.pow(prev.x - curr.x, 2) + Math.pow(prev.y - curr.y, 2) + Math.pow(prev.z - curr.z, 2));
        }

        City prev = cities.get(path.get(i - 1));
        City curr = cities.get(0);
        score += Math.sqrt(Math.pow(prev.x - curr.x, 2) + Math.pow(prev.y - curr.y, 2) + Math.pow(prev.z - curr.z, 2));
        return score;
    }

    private List<City> getPath() {

        return null;
    }

    private List<Pair> initializePopulation(int size){

        //randomly pick cities if total cities is too big
        //otherwise we can generate all permutations
        List<Pair> result = new ArrayList<>();

        for (int i = 0; i < size; i++){

            recursiveHelper(result, new ArrayList<>());
        }
        return result;

    }

    private void recursiveHelper(List<Pair> result, List<Integer> curr){


        if (curr.size() == cities.size()){

            result.add(new Pair(curr, getScore(curr)));
        }
        else{

            int i = random.nextInt(0, cities.size());
            while (repeat(curr, i)){

                i = random.nextInt(0, cities.size());
            }


            System.out.println("adding valid city");
            curr.add(i);
            recursiveHelper(result, new ArrayList<>(curr));
            curr.remove(curr.size() - 1);


        }
    }

    private boolean repeat(List<Integer> curr, int i) {

        for (Integer e : curr){

            if (e == i) return true;
        }

        return false;
    }


}

public class homework {

    public static void main(String[] args){

        TSP tsp= new TSP();



    }
}
